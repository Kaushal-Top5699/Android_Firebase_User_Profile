package com.example.this_is_kaushal.a1920plust2.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.this_is_kaushal.a1920plust2.R;
import com.example.this_is_kaushal.a1920plust2.Utils.UserInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private EditText mUsername;

    private EditText mEmail;

    private EditText mPassword;

    private Button btnSignUp;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    private String userID;

    private String email;

    private String name;

    private String password;

    private String photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mUsername = (EditText) findViewById(R.id.username);

        mEmail = (EditText) findViewById(R.id.emailID);

        mPassword = (EditText) findViewById(R.id.passWord);

        btnSignUp = (Button) findViewById(R.id.btnSignUp);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = mFirebaseDatabase.getReference();

        setupFirebaseAuth();

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = mEmail.getText().toString();

                name = mUsername.getText().toString();

                password = mPassword.getText().toString();

                photo = "none";

                if (!email.equals("") || !password.equals("") || !name.equals("")) {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();

                                        userID = mAuth.getCurrentUser().getUid();

                                    }
                                }
                            });

                } else {

                    Toast.makeText(RegisterActivity.this, "Fields are empty, try again", Toast.LENGTH_SHORT).show();

                    return;

                }

            }
        });
    }

    /**
     * -------------------------------------Friebase Setup---------------------------------------------------*
     */

    //Method to setup firebase authentication

    private void setupFirebaseAuth() {

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                final FirebaseUser user = firebaseAuth.getCurrentUser();

                mFirebaseDatabase = FirebaseDatabase.getInstance();

                myRef = mFirebaseDatabase.getReference();

                if (user != null) {
                    // User is signed in
                    Toast.makeText(RegisterActivity.this, "Signed In", Toast.LENGTH_SHORT).show();

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            addNewUser(name, email, photo);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    // User is signed out
                    Toast.makeText(RegisterActivity.this, "Signed Out", Toast.LENGTH_SHORT).show();
                }
                // ...
            }
        };

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
    }/**
     * -------------------------------------Friebase Setup---------------------------------------------------*
     */


    //This method add new user to the database
    public void addNewUser(String name, String email, String photo) {

        /*OtherUsers otherUsers = new OtherUsers(name);
        myRef.child("users_names")
                .child(userID)
                .setValue(otherUsers); */

        //This is to update the users_account_settings node in database
        //For this we created a UserAccountSettings class
        UserInformation userInformation = new UserInformation(name, email, photo); //pass the default values of the user
        myRef.child("users")
                .child(userID)
                .setValue(userInformation); //Pass the object name

    }


}
