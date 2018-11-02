package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class UserDashboard extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button7) {
            startActivity(new Intent(this, UserLogin.class));
        } else if (i == R.id.button8) {
            // Review
        } else if (i == R.id.button10) {
            // Sign Out and Close
        }
    }
}
