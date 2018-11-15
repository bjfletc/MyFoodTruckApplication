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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String addressForMarker;
    private String tmpTruckName;
    ArrayList<String> addresses = new ArrayList<String>();
    ArrayList<String> truckNames = new ArrayList<String>();
    private MarkerOptions options = new MarkerOptions();
    private ArrayList<LatLng> latlngs = new ArrayList<>();
    private FirebaseAuth mAuth;

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

        mAuth = FirebaseAuth.getInstance();

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


        // Add a marker in Tulsa and move the camera
        LatLng tulsa = new LatLng(36.1636, -95.9879);
        mMap.addMarker(new MarkerOptions().position(tulsa).title("OSU Tulsa").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tulsa, 10));
        // Add a marker in Stillwater
        LatLng stillwater = new LatLng(36.1270, -97.0737);
        mMap.addMarker(new MarkerOptions().position(stillwater).title("Oklahoma State University").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));


        /* Attempt to Get All Food Trucks */
        Task user = db.collection("TruckDatabase").get();
        user.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> myListOfDocuments = task.getResult().getDocuments();

                    for (DocumentSnapshot doc : myListOfDocuments) {
                        StringBuilder fields = new StringBuilder("");
                        StringBuilder trucks = new StringBuilder("");
                        // this if now fixes the bug if someone does not add an address or truck name!
                        if (doc.get("Address") != "" && doc.get("Truck Name") != "") {
                            // System.out.println(doc.get("Truck Name:").toString() + "");
                            fields.append("Address: ").append(doc.get("Address"));
                            trucks.append("Truck Name: ").append(doc.get("Truck Name"));
                            addressForMarker = fields.toString();
                            addressForMarker = addressForMarker.substring(8);
                            addresses.add(addressForMarker);
                            tmpTruckName = trucks.toString();
                            tmpTruckName = tmpTruckName.substring(11);
                            truckNames.add(tmpTruckName);
                        } else {
                            System.out.println("Either the truck has no address or no name... skip and proceed");
                            continue;
                        }

                        // fields.append(" Truck Name: ").append(doc.get("Truck Name"))
                    }

                }
                /*
                    Iterate through and generate latlngs out of my addresses from FireStore
                 */
                for (String add : addresses) {
                    GeoPoint pointFromAddress = getLocationFromAddress(add);
                    Double lat = pointFromAddress.getLatitude();
                    Double lng = pointFromAddress.getLongitude();
                    latlngs.add(new LatLng(lat, lng));
                }

                /*
                    Iterate through latlngs and display food trucks on map.
                 */
                for (int i = 0; i < latlngs.size(); i++) {
                    options.position(latlngs.get(i));
                    options.title(truckNames.get(i));
                    options.snippet("someDesc");
                    mMap.addMarker(options);
                }
            }
        });
    }

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
