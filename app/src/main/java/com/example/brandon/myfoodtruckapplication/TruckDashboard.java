package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TruckDashboard extends AppCompatActivity implements View.OnClickListener {

    TextView dashboard_Title;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_dashboard);

        /* Buttons */
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);


        // [START initialize_auth]
        // Initialize Firebase Auth
        // [END initialize_auth]
        dashboard_Title = findViewById(R.id.textView3);
        // dashboard_Title.setText(user.getDisplayName());
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            System.out.println("User's Name: " + name);
            dashboard_Title.setText(name);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button3) {
            startActivity(new Intent(this, TruckOpen.class));
        } else if (i == R.id.button5) {
            startActivity(new Intent(this, TruckEdit.class));
        } else if (i == R.id.button6) {
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
