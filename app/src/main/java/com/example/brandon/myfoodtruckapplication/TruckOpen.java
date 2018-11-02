package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TruckOpen extends AppCompatActivity implements View.OnClickListener {

    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String ADDRESS_KEY = "Address";
    EditText address;

    FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_open);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        /* Buttons */
        findViewById(R.id.Add).setOnClickListener(this);

        address = findViewById(R.id.Address);

    }


    private void addNewTruck() {
        Map<String, Object> newTruck = new HashMap<>();
        newTruck.put(NAME_KEY, mAuth.getCurrentUser().getDisplayName());
        newTruck.put(EMAIL_KEY, mAuth.getCurrentUser().getEmail());
        newTruck.put(ADDRESS_KEY, address.getText().toString());
        db.collection("TruckDatabase").document("TruckOwner").set(newTruck)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TruckOpen.this, "Truck Registered",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(TruckOpen.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.Add) {
            addNewTruck();
            startActivity(new Intent(this, TruckDashboard.class));
            address.setText("");
        } else if (i == R.id.Close) {
            mAuth.signOut();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
