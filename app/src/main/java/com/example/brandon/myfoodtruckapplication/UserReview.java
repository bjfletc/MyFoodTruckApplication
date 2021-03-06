package com.example.brandon.myfoodtruckapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserReview extends AppCompatActivity implements View.OnClickListener {

    private static final String TRUCK_NAME_KEY = "Truck Name";
    private static final String REVIEW_KEY = "Review";


    EditText review;
    private FirebaseAuth mAuth;

    ArrayList<String> truckNames = new ArrayList<String>();
    private String tmpTruckName;
    private Spinner truckSpinner;

    FirebaseFirestore db;
    FirebaseFirestoreSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_review);

        db = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        mAuth = FirebaseAuth.getInstance();

        /* Buttons */
        findViewById(R.id.button12).setOnClickListener(this);
        findViewById(R.id.share).setOnClickListener(this);

        review = findViewById(R.id.review);

        /* Attempt to Get All Food Trucks */
        Task user = db.collection("TruckDatabase").get();
        user.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();

                    for (DocumentSnapshot doc : myListOfDocuments) {
                        StringBuilder trucks = new StringBuilder("");
                        trucks.append("Truck Name: ").append(doc.get("Truck Name"));
                        tmpTruckName = trucks.toString();
                        tmpTruckName = tmpTruckName.substring(11);
                        System.out.println(tmpTruckName);
                        truckNames.add(tmpTruckName);
                    }
                    addItemsOnTruckSpinner();
                }
            }
        });
    }

    private void addNewRreview() {
        Map<String, Object> newReview = new HashMap<>();
        newReview.put(TRUCK_NAME_KEY, String.valueOf(truckSpinner.getSelectedItem()));
        newReview.put(REVIEW_KEY, review.getText().toString());
        db.collection("ReviewDatabase").document(mAuth.getCurrentUser().getDisplayName()).set(newReview)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(UserReview.this, "Review Registered to Map Description",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UserReview.this, "ERROR" + e.toString(),
                                Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }

    // add items into spinner dynamically
    public void addItemsOnTruckSpinner() {

        truckSpinner = (Spinner) findViewById(R.id.food_truck_spinner);
        List<String> list = new ArrayList<String>();

        for (String truck : truckNames) {
            list.add(truck);
            System.out.println(truck);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        truckSpinner.setAdapter(dataAdapter);
    }

    private void shareText(String textToShare) {
        String mimeType = "text/plain";

        String title = "MyFoodTruckApplication Review";

        ShareCompat.IntentBuilder
                /* The from method specifies the Context from which this share is coming from */
                .from(this)
                .setType(mimeType)
                .setChooserTitle(title)
                .setText(textToShare)
                .startChooser();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button12) {
            addNewRreview();
            startActivity(new Intent(this, UserDashboard.class));
        } else if (i == R.id.share) {
            System.out.println("Time to Implement Share...");

            String selectedFoodTruckToReview = String.valueOf(truckSpinner.getSelectedItem());
            String reviewOfSelectedFoodTruck = review.getText().toString();

            String textThatYouWantToShare = selectedFoodTruckToReview + " : " + reviewOfSelectedFoodTruck;

            shareText(textThatYouWantToShare);
        }
    }

}
