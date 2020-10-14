package com.example.cardchain;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthenticationHandler{

    private String email, password = "";
    FirebaseAuth mAuth;
    private FirebaseUser user;

    public Task<AuthResult> signUp(String email, String password){
        mAuth = FirebaseAuth.getInstance();
        this.email = email;
        this.password = password;
        return mAuth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signIn(String email, String password){
        mAuth = FirebaseAuth.getInstance();
        this.email = email;
        this.password = password;
        return mAuth.signInWithEmailAndPassword(email, password);
    }

    public Task<Void> forgotPassword(String email){
        mAuth = FirebaseAuth.getInstance();
        this.email = email;
        return mAuth.sendPasswordResetEmail(email);
    }


}
