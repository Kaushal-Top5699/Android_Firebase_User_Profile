package com.example.this_is_kaushal.a1920plust2.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.this_is_kaushal.a1920plust2.Profile.ProfileActivity;
import com.example.this_is_kaushal.a1920plust2.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;

    private EditText mPassword;

    private Button btnSignIn, btnSignUp, btnProfile, btnSignOut;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.emailID);

        mPassword = (EditText) findViewById(R.id.passWord);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        btnProfile = (Button) findViewById(R.id.btnProfile);

        btnSignOut = (Button) findViewById(R.id.btnSignOut);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    // User is signed in

                    Toast.makeText(LoginActivity.this, "User: "+user.getEmail(), Toast.LENGTH_SHORT).show();

                } else {

                    // User is signed out

                    Toast.makeText(LoginActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();

                }
                // ...
            }
        };

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();

                String password = mPassword.getText().toString();

                if (!email.equals("") || !password.equals("")) {

                    mAuth.signInWithEmailAndPassword(email, password);

                } else {

                    Toast.makeText(LoginActivity.this, "All fills required", Toast.LENGTH_SHORT).show();

                }

            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);

                startActivity(intent);

            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(LoginActivity.this, ProfileActivity.class);

                startActivity(intent2);

            }
        });

    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
