package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class UserRegister extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "EmailPassword";

    private EditText mEmailField;
    private EditText mPasswordField;
    private EditText mFirstNameField;
    private EditText mLastNameField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseDatabase firebaseDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        // Views
        mFirstNameField = findViewById(R.id.firstName);
        mLastNameField = findViewById(R.id.lastName);
        mEmailField = findViewById(R.id.registerUser);
        mPasswordField = findViewById(R.id.registerPass);

        // Buttons
        findViewById(R.id.register).setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        firebaseDB = FirebaseDatabase.getInstance();
    }

    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            Toast.makeText(UserRegister.this, "Registration Successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            addUserNameToUser(user);

                            // updateUI(user);
                            startActivity(new Intent(UserRegister.this, UserLogin.class));



                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(UserRegister.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }

    private void addUserNameToUser(FirebaseUser user) {
        String username = (mFirstNameField.getText().toString() + " " + mLastNameField.getText().toString());

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.register) {
            createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }
}
