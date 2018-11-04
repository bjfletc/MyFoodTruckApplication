package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class TruckEdit extends AppCompatActivity implements View.OnClickListener {

    private static final String NAME_KEY = "Name";
    private static final String EMAIL_KEY = "Email";
    private static final String PHONE_KEY = "Phone Number";
    private static final String TRUCK_NAME_KEY = "Truck Name";

    EditText firstName;
    EditText lastName;
    EditText truckName;
    EditText phoneNumber;
    EditText email;

    FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truck_edit);

        /* Buttons */
        findViewById(R.id.button11).setOnClickListener(this);

        /* Edit Text */
        truckName = findViewById(R.id.editTextTruckName);
        firstName = findViewById(R.id.editTextOwnerFirstName);
        lastName = findViewById(R.id.editTextOwnerLastName);
        phoneNumber = findViewById(R.id.editTextOwnerPhoneNumber);
        email = findViewById(R.id.editTextEmail);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    private void UpdateData() {
        DocumentReference truck = db.collection("TruckDatabase").document(mAuth.getCurrentUser().getDisplayName());
        truck.update(NAME_KEY, (firstName.getText().toString() + " " + lastName.getText().toString()));
        truck.update(EMAIL_KEY, email.getText().toString());
        truck.update(PHONE_KEY, phoneNumber.getText().toString());
        truck.update(TRUCK_NAME_KEY, truckName.getText().toString())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TruckEdit.this, "Updated Successfully",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button11) {
            UpdateData();
            startActivity(new Intent(this, TruckDashboard.class));
        }
    }
}
