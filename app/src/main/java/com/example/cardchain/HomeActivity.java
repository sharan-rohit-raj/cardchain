package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.integration.android.IntentIntegrator;

import MainFragments.FragmentList;
import MainFragments.FragmentSlide;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;


public class HomeActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    final static int STORAGE_PERMISSION_CODE = 1011;
    private static final int CAMERA_PERMISSION_CODE=101;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db;
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
    SharedPreferences sharedPreferences;
    WeatherDialog dialog;
    ConnectivityManager cm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(checkConnection()){
            dialog=new WeatherDialog(this);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

        
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        // Check if we need to display our OnboardingActivity
        if (!sharedPreferences.getBoolean(
                OnBoardingActitivity.onBoardingCompleted, false)) {
            // The user hasn't seen the OnboardingActivity yet, so show it
            startActivityForResult(new Intent(this, OnBoardingActitivity.class), 11);
        }

        HomeAct = this;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        toolBar = findViewById(R.id.tool_inc);
        meowNav = findViewById(R.id.bottom_nav);
        logoutBtn = toolBar.findViewById(R.id.log_out);
        navView = findViewById(R.id.navigationView);
        navView.setItemIconTintList(null);
        drawer_Layout = findViewById(R.id.drawer_layout);

        //Assign the user name to the drawer header
        View header = navView.getHeaderView(0);
        drawerName = header.findViewById(R.id.drawer_name);
        if (user != null && user.getDisplayName() != null) {
            drawerName.setText(user.getDisplayName());
        } else {
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
                if(!checkConnection()){
                    errorDialog(getString(R.string.connectivity_err));
                    return false;
                }
                switch (item.getItemId()) {
                    case R.id.profile_menu:
                        Intent editProfIntent = new Intent(HomeActivity.this, EditProfile.class);
                        startActivity(editProfIntent);
                        return true;
                    case R.id.delete_acc_menu:
                        deleteFirebaseAccount();
                        return true;
                    case R.id.weather_menu:
                        dialog=new WeatherDialog(HomeActivity.this);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();
                        return true;
                    case R.id.about_us:
//                        Toast.makeText(HomeActivity.this, "About", Toast.LENGTH_SHORT).show();
//                        ad = new AboutDialog();
//                        ad.show(getSupportFragmentManager(),"About dialog");
                        aboutDialog();
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
        if (user == null) {
            finish();
            return;
        }

        meowNav.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                return null;
            }
        });

        meowNav.show(SLIDE_ID, true);
        meowNav.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment select_fragment = null;
                switch (model.getId()) {
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment, "CurFrag").commit();
                return null;
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()){
                    Intent intent = new Intent(HomeActivity.this, AddCardActivity.class);
                    startActivity(intent);
                }else{
                    errorDialog(getString(R.string.connectivity_err));
                }

            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()){
                    auth.signOut();
                }else{
                    errorDialog(getString(R.string.connectivity_err));
                }

            }

        });


        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        }
    }


    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int code) {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, code);
        }
    }


    private boolean checkConnection() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    private void deleteFirebaseAccount() {
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.reauthenticate);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_button = dialog.findViewById(R.id.confirm_btn);
        Button dialog_cancel = dialog.findViewById(R.id.cancel_Btn);
        final EditText passTxt = dialog.findViewById(R.id.passField);
        dialog.setCanceledOnTouchOutside(false);
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkConnection()){
                    String password = passTxt.getText().toString();
                    if (password.equals("")) {
                        Toast.makeText(HomeActivity.this, "Password Is Blank", Toast.LENGTH_SHORT).show();
                    }else{
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);
                        user.reauthenticate(credential).

                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("HomeActivity", "User re-authenticated.");
                                            deleteAllCards();
                                            db.collection("users").document(user.getUid()).delete();
                                            user.delete();
                                        } else {
                                            Toast.makeText(HomeActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                            Log.d("HomeActivity", "Wrong Password");
                                        }
                                    }
                                });
                    }
                }else{
                    errorDialog(getString(R.string.connectivity_err));
                }

            }
        });
        dialog_cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
                dialog.show();

    }
    public boolean deleteAllCards(){
        Query docu = db.collection("users").document(user.getUid()).collection("cards").whereEqualTo("tag","all");
        docu.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete();
                            }
                        } else {
                            Toast.makeText(HomeActivity.this,"Error Deleting",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().addAuthStateListener(this);
    }
//
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        if(firebaseAuth.getCurrentUser() == null && !alreadyCalled){
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
        if(requestCode == 11 && resultCode == 11){
            SharedPreferences.Editor sharedPrefEditor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            sharedPrefEditor.putBoolean(OnBoardingActitivity.onBoardingCompleted, true);
            sharedPrefEditor.apply();
        }else{
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("CurFrag");
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, intent);
            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("CurFrag");
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (fragment != null) {
                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    public void errorDialog(String title){
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.error_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_button = dialog.findViewById(R.id.err_ok_btn);
        TextView dialog_text = dialog.findViewById(R.id.err_dialog_txt);
        dialog.setCanceledOnTouchOutside(false);
        dialog_text.setText(title.trim());
        dialog_text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void aboutDialog(){
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button dialog_button = dialog.findViewById(R.id.about_ok_btn);
        dialog.setCanceledOnTouchOutside(false);

        dialog_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}