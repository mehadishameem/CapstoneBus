package com.example.capstonebus;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.capstonebus.databinding.ActivityCurrentLocationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;


import android.Manifest;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class CurrentLocationActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private final int FINE_PERMISSION_CODE=1;

    Button passengerLocation;

    Location currentLocation;

    LatLng newLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private ActivityCurrentLocationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCurrentLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        passengerLocation = findViewById(R.id.passengerLocation);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


        passengerLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CurrentLocationActivity.this,Search_activity.class);
                i.putExtra("currentLocation",newLocation);
                startActivity(i);
            }
        });

    }

    private void getLastLocation(){
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},FINE_PERMISSION_CODE);

            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location !=null){
                    currentLocation =location;
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(CurrentLocationActivity.this);
                }
            }
        });
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

        // Add a marker at the current location
        newLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(newLocation));

        MarkerOptions options = new MarkerOptions()
                .position(newLocation)
                .title("I am here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));

        // Add marker to the map
        Marker marker = mMap.addMarker(options);

        // Set an OnMarkerClickListener
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker clickedMarker) {
                // Show a Toast with the latitude and longitude of the marker
                LatLng position = clickedMarker.getPosition();
                String message = "Latitude: " + position.latitude + ", Longitude: " + position.longitude;
                System.out.println(message);
                Toast.makeText(CurrentLocationActivity.this, message, Toast.LENGTH_SHORT).show();
                return false; // Return false to allow the default behavior (camera move)
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, int deviceId) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId);
        if(requestCode == FINE_PERMISSION_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else{
                Toast.makeText(this, "Location permission is denied,please allow the permission to access", Toast.LENGTH_SHORT).show();
            }
        }
    }
}