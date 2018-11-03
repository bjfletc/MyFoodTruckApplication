package com.example.brandon.myfoodtruckapplication;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String addressForMarker;

    FirebaseFirestore db;
    FirebaseFirestoreSettings settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        db = FirebaseFirestore.getInstance();
        settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        db.setFirestoreSettings(settings);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng tulsa = new LatLng(36.1636, -95.9879);
        mMap.addMarker(new MarkerOptions().position(tulsa).title("Marker in Tulsa"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tulsa, 10));

        DocumentReference user = db.collection("TruckDatabase").document("TruckOwner");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("Address: ").append(doc.get("Address"));
                    addressForMarker = fields.toString();
                    addressForMarker = addressForMarker.substring(8);
                    System.out.println(addressForMarker);
                    // fields.append(" Truck Name: ").append(doc.get("Truck Name"))
                }
                System.out.println("FLAG BEFORE GEOPOINT");
                GeoPoint pointFromAddress = getLocationFromAddress(addressForMarker);
                System.out.println("FLAG AFTER GEOPOINT");
                Double lat = pointFromAddress.getLatitude();
                System.out.println("FLAG AFTER LAT INIT");
                System.out.println(Double.toString(lat));
                Double lng = pointFromAddress.getLongitude();
                System.out.println(Double.toString(lng));
                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("The Taco King"));
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 10));
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    /*
    private void ReadSingleTruck() {
        DocumentReference user = db.collection("TruckDatabase").document("TruckOwner");
        user.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    StringBuilder fields = new StringBuilder("");
                    fields.append("Address: ").append(doc.get("Address"));
                    addressForMarker = fields.toString();
                    addressForMarker = addressForMarker.substring(8);
                    System.out.println(addressForMarker);
                    // fields.append(" Truck Name: ").append(doc.get("Truck Name"))
                }
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }
    */

    public GeoPoint getLocationFromAddress(String strAddress){

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        GeoPoint p1 = null;

        try {
            address = coder.getFromLocationName(strAddress,5);
            if (address==null) {
                return null;
            }
            Address location=address.get(0);
            location.getLatitude();
            System.out.println(Double.toString(location.getLatitude()));
            location.getLongitude();
            System.out.println(Double.toString(location.getLongitude()));

            p1 = new GeoPoint((double) (location.getLatitude()),
                    (double) (location.getLongitude()));

            return p1;
        } catch (java.io.IOException e) {
            return null;
        }
    }

}
