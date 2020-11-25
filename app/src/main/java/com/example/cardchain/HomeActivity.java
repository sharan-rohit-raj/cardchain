package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import MainFragments.FragmentList;
import MainFragments.FragmentSlide;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class HomeActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    FirebaseAuth auth;
    FirebaseUser user;
    ImageButton logoutBtn;
    private Toolbar toolBar;
    ActionBarDrawerToggle drawerToggle;
    private static final int SLIDE_ID = 2;
    private static final int ADD_CARD_ID = 1;
    private static final int LIST_CARD_ID = 3;
    public static Activity HomeAct;
    private boolean alreadyCalled;
    MeowBottomNavigation meowNav;
    NavigationView navView;
    DrawerLayout drawer_Layout;
    TextView drawerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        HomeAct = this;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        toolBar = findViewById(R.id.tool_inc);
        meowNav = findViewById(R.id.bottom_nav);
        logoutBtn = toolBar.findViewById(R.id.log_out);
        navView = findViewById(R.id.navigationView);
        navView.setItemIconTintList(null);
        drawer_Layout = findViewById(R.id.drawer_layout);

        //Assign the user name to the drawer header
        View header = navView.getHeaderView(0);
        drawerName = header.findViewById(R.id.drawer_name);
        if(user != null && user.getDisplayName() != null){
            drawerName.setText(user.getDisplayName());
        }else{
            drawerName.setText(getString(R.string.user));
        }

        setSupportActionBar(toolBar);
        toolBar.setNavigationIcon(R.drawable.ic_tool_menu);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_Layout.openDrawer(GravityCompat.START);
            }
        });

        //Setting the menu for drawer
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.profile_menu:
                        Intent editProfIntent = new Intent(HomeActivity.this, EditProfile.class);
                        startActivity(editProfIntent);
                        return true;
                    case R.id.delete_acc_menu:
                        Toast.makeText(HomeActivity.this, "Delete Account", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.about_us:
                        Toast.makeText(HomeActivity.this, "About", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.contact_us_drawer:
                        Intent contactIntent = new Intent(HomeActivity.this, ContactUs.class);
                        startActivity(contactIntent);
                    default:
                        return false;
                }
            }
        });

        //Setting Bottom navigation Bar
//        meowNav.add(new MeowBottomNavigation.Model(ADD_CARD_ID, R.drawable.ic_nav_add));
        meowNav.add(new MeowBottomNavigation.Model(SLIDE_ID, R.drawable.ic_nav_card));
        meowNav.add(new MeowBottomNavigation.Model(LIST_CARD_ID, R.drawable.ic_nav_list));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSlide()).commit();

        alreadyCalled = false;
        if(user == null){
            finish();
            return;
        }

        meowNav.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Log.d("nav_bar","Model ID: "+model.getId());
                return null;
            }
        });

        meowNav.show(SLIDE_ID, true);
        meowNav.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment select_fragment = null;
                switch (model.getId()){
                    case SLIDE_ID:
                        select_fragment = new FragmentSlide();
                        break;
                    case LIST_CARD_ID:
                        select_fragment = new FragmentList();
                        break;
//                    case ADD_CARD_ID:
//
//                        select_fragment = new FragmentAdd();
//                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment,"CurFrag").commit();
                return null;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AddCardActivity.class);
                startActivity(intent);
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
            }

        });



    }

        @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("FirebaseHome","In Stop");
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }
//
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d("FirebaseHome","Calling AuthState");
        if(firebaseAuth.getCurrentUser() == null && !alreadyCalled){
            Log.d("FirebaseHome","Calling start login");
            alreadyCalled = true;
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CurFrag");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CurFrag");
        if (fragment != null) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}