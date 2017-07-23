package com.example.this_is_kaushal.a1920plust2.Profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.this_is_kaushal.a1920plust2.R;
import com.example.this_is_kaushal.a1920plust2.Utils.UserInformation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.jar.Attributes;

/**
 * Created by this_is_kaushal on 7/22/2017.
 */

public class EditProfileActivity extends AppCompatActivity {

    private static final int GALLARY_ACCESS = 5699;

    private EditText mName;

    private EditText mEmail;

    private Button btnUpdateData;

    private ImageButton imageButton;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    private StorageReference storageReference;

    private Uri mImageUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mName = (EditText) findViewById(R.id.username);

        mEmail = (EditText) findViewById(R.id.emailID);

        btnUpdateData = (Button) findViewById(R.id.btnDoneEdit);

        imageButton = (ImageButton) findViewById(R.id.imageBtn);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        myRef = FirebaseDatabase.getInstance().getReference().child("users");

        storageReference = FirebaseStorage.getInstance().getReference().child("profile_img");

        FirebaseUser user = mAuth.getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.i("info", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.i("info", "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("info", "onAuthStateChanged:signed_in:" + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUpdateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = mName.getText().toString();

                final String email = mEmail.getText().toString();

                final String userID = mAuth.getCurrentUser().getUid();

                if (!name.equals("") || !email.equals("") || mImageUri != null) {

                    StorageReference filepath = storageReference.child(mImageUri.getLastPathSegment());

                    filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            String downloadUrl = taskSnapshot.getDownloadUrl().toString();

                            myRef.child(userID).child("name").setValue(name);

                            myRef.child(userID).child("email").setValue(email);

                            myRef.child(userID).child("photo").setValue(downloadUrl);

                            Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();


                        }
                    });

                   /* UserInformation userInformation = new UserInformation(name, email);

                    myRef.child("users").child(userID).setValue(userInformation);

                    Toast.makeText(EditProfileActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

                    mName.setText("");

                    mEmail.setText(""); */


                } else {

                    Toast.makeText(EditProfileActivity.this, "Fields are Empty, Try again", Toast.LENGTH_SHORT).show();

                }


            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);

                galleryIntent.setType("image/*");

                startActivityForResult(galleryIntent, GALLARY_ACCESS);

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLARY_ACCESS && resultCode == RESULT_OK) {

            mImageUri = data.getData();

            imageButton.setImageURI(mImageUri);
        }
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
