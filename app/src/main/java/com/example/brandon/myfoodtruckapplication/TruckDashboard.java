package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class TruckDashboard extends AppCompatActivity implements View.OnClickListener {

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    TextView dashboard_Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_dashboard);

        /* Buttons */
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);


        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        dashboard_Title = findViewById(R.id.textView3);
        dashboard_Title.setText(mAuth.getCurrentUser().getDisplayName());
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button3) {
            startActivity(new Intent(this, TruckOpen.class));
        } else if (i == R.id.button5) {
            System.out.println("Open Edit...");
        } else if (i == R.id.button6) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
