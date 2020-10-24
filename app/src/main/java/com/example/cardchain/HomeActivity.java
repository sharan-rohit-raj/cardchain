package com.example.cardchain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import MainFragments.FragmentAdd;
import MainFragments.FragmentList;
import MainFragments.FragmentSlide;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomeActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    TextView welcome;
    FirebaseAuth auth;
    ImageButton logoutBtn;
    private Toolbar toolBar;
    private static final int SLIDE_ID = 2;
    private static final int ADD_CARD_ID = 1;
    private static final int LIST_CARD_ID = 3;
    public static Activity HomeAct;
    private boolean alreadyCalled;
    MeowBottomNavigation meowNav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        HomeAct = this;
        toolBar = findViewById(R.id.tool_inc);
        meowNav = findViewById(R.id.bottom_nav);
        logoutBtn = toolBar.findViewById(R.id.log_out);
        setSupportActionBar(toolBar);


        //Setting Bottom navigation Bar
        meowNav.add(new MeowBottomNavigation.Model(ADD_CARD_ID, R.drawable.ic_nav_add));
        meowNav.add(new MeowBottomNavigation.Model(SLIDE_ID, R.drawable.ic_nav_card));
        meowNav.add(new MeowBottomNavigation.Model(LIST_CARD_ID, R.drawable.ic_nav_list));
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FragmentSlide()).commit();
        auth = FirebaseAuth.getInstance();
        alreadyCalled = false;
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            finish();
            return;
        }

        meowNav.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            Intent nav_intent;
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Log.d("nav_bar","Model ID: "+model.getId());
                return null;
            }
        });

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
                    case ADD_CARD_ID:
                        select_fragment = new FragmentAdd();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, select_fragment).commit();
                return null;
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
        if(firebaseAuth.getCurrentUser() == null && alreadyCalled == false){
            Log.d("FirebaseHome","Calling start login");
            alreadyCalled = true;
            startLoginActivity();
            return;
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}