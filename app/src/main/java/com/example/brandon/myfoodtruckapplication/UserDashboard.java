package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class UserDashboard extends AppCompatActivity implements View.OnClickListener {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    TextView dashboard_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);

        /* Buttons */
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button10).setOnClickListener(this);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        dashboard_Title = findViewById(R.id.textView2);
        dashboard_Title.setText(mAuth.getCurrentUser().getDisplayName());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button7) {
            startActivity(new Intent(this, MapsActivity.class));
        } else if (i == R.id.button8) {
            startActivity(new Intent(this, UserReview.class));
        } else if (i == R.id.button10) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
