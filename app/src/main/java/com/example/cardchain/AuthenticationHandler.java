package com.example.cardchain;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationHandler{

    private String email, password = "";
    FirebaseAuth mAuth;
    private FirebaseUser user;

    public Task<AuthResult> signUp(String email, String password){
        mAuth = FirebaseAuth.getInstance();
        String msg = "";
        this.email = email;
        this.password = password;
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public String forgotPassword(String email){
        mAuth = FirebaseAuth.getInstance();
        String msg = "";
        this.email = email;

        return msg;
    }


}
