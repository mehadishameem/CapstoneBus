package com.example.capstonebus;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Search_activity extends AppCompatActivity {
    Map<String, List<BusRoute>> map2 = new HashMap<>();
    private Button btnSearch;
    private TextView etFrom,etTo;

    LatLng currentlocation;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        databaseReference = FirebaseDatabase.getInstance().getReference("Routes");

        etFrom = findViewById(R.id.etFrom);
        etTo = findViewById(R.id.etTo);

        btnSearch = findViewById(R.id.btnSearch);
        String selected = getIntent().getStringExtra("selected");
        String selected2 = getIntent().getStringExtra("selected2");


        fetchRoutesData();





       /* Map<String ,List<BusRoute>> map  = new HashMap<>();

        // Create list for route1
        List<BusRoute> route1 = new ArrayList<>();
        route1.add(new BusRoute(1, 23.7808875, 90.2792371, "Moderate", "Heavy", "Light", "Moderate", "Farmgate"));
        route1.add(new BusRoute(2, 23.7850, 90.2800, "Light", "Moderate", "Moderate", "Heavy", "Dhanmondi"));

        // Create list for route2
        List<BusRoute> route2 = new ArrayList<>();
        route2.add(new BusRoute(3, 23.7500, 90.3600, "Heavy", "Light", "Moderate", "Heavy", "Mohakhali"));
        route2.add(new BusRoute(4, 23.7550, 90.3700, "Moderate", "Moderate", "Heavy", "Moderate", "Gulshan"));

        // Add pairs to routesList: Route name and corresponding BusRoute list
        map.put("saydabadtogazipur",route1);
        map.put("motijheeltosonirakhra",route2);

        String key = databaseReference.push().getKey();

        databaseReference.child("saydabadtogazipur").setValue(route1);
        databaseReference.child("motijheeltosonirakhra").setValue(route2);
        System.out.println("hi testing");*/







        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Search_activity.this,CurrentLocationActivity.class);
                i.putExtra("etTo","etFrom");
                startActivityForResult(i, 1);



            }
        });


        Intent i = getIntent();
        currentlocation= i.getParcelableExtra("currentLocation");

        if (currentlocation != null) {
            System.out.println("Current location from search_activity: " + currentlocation.latitude + ", " + currentlocation.longitude);


        } else {
            // Handle the case where currentlocation is null (e.g., show an error message or set default values)
            System.out.println("Current location is null!");



        }



        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Search_activity.this,Search_bar_activity.class);
                i.putExtra("etTo","etTo");
                startActivityForResult(i, 1);

            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Search_activity.this, Buss_List_activity.class);
                startActivity(i);
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.hasExtra("selected")) {
                etFrom.setText(data.getStringExtra("selected"));
            }
            if (data.hasExtra("selected2")) {
                etTo.setText(data.getStringExtra("selected2"));
            }
        }
    }

    private void fetchRoutesData() {
        // Retrieve the data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // New map to store the retrieved data


                // Loop through all routes
                for (DataSnapshot routeSnapshot : snapshot.getChildren()) {
                    // Get the route name (key)
                    String routeName = routeSnapshot.getKey();

                    // Create a list to hold the bus routes for this route
                    List<BusRoute> routeInfo = new ArrayList<>();

                    // Loop through each bus route under this route
                    for (DataSnapshot busRouteSnapshot : routeSnapshot.getChildren()) {
                        // Deserialize the bus route object
                        BusRoute busRoute = busRouteSnapshot.getValue(BusRoute.class);

                        // Add the bus route to the list
                        if (busRoute != null) {
                            routeInfo.add(busRoute);
                        }
                    }

                    // Put the route name and corresponding list of bus routes into the map
                    if (routeName != null) {
                        map2.put(routeName, routeInfo);
                    }
                }

               //printing entiredatabase
                /*for (Map.Entry<String, List<BusRoute>> entry : map2.entrySet()) {
                    String routeName = entry.getKey();
                    List<BusRoute> busRoutes = entry.getValue();

                    System.out.println("Route: " + routeName);
                    for (BusRoute route : busRoutes) {
                        System.out.println("BusRoute ID: " + route.getSerialNo() +
                                ", Place: " + route.getPlaceName() +
                                ", Traffic at 8 AM: " + route.getTraffic8am());
                    }
                }
*/
                // After loading the data, call findClosestBusRoute
                if (currentlocation != null) {
                    findClosestBusRoute(currentlocation);
                } else {
                    System.out.println("Current location is null!");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while fetching data
                Toast.makeText(Search_activity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void findClosestBusRoute(LatLng currentLocation) {
        double closestDistance = Double.MAX_VALUE;
        BusRoute closestRoute = null;

        if (map2.isEmpty()) {
            System.out.println("map2 is empty");
        }


        // Loop through each route and its corresponding bus stops
        for (Map.Entry<String, List<BusRoute>> entry : map2.entrySet()) {
            String routeName = entry.getKey();
            List<BusRoute> busRoutes = entry.getValue();

            //System.out.println("Route: " + routeName); // Printing the route name

            for (BusRoute route : busRoutes) {
               /* System.out.println("BusRoute ID: " + route.getSerialNo() +
                        ", Place: " + route.getPlaceName() +
                        ", Traffic at 8 AM: " + route.getTraffic8am()); // Printing route details*/


                String routeLatStr = route.getLatitude();
                String routeLngStr = route.getLongitude();

                try {

                    double routeLat = Double.parseDouble(routeLatStr);
                    double routeLng = Double.parseDouble(routeLngStr);

                    // Create LatLng object for the bus route
                    LatLng routeLatLng = new LatLng(routeLat, routeLng);

                    double distance = manhattanDistance(currentLocation, routeLatLng);


                    if (distance < closestDistance) {
                        closestDistance = distance;
                        closestRoute = route;
                    }
                } catch (NumberFormatException e) {

                    System.out.println("Error parsing latitude/longitude: " + e.getMessage());
                }
            }
        }

        // Show the closest bus route information or handle accordingly
        if (closestRoute != null) {
            etFrom.setText(closestRoute.getPlaceName());
            System.out.println("Closest Bus Route: " + closestRoute.getPlaceName() + " with distance: " + closestDistance);
            // Optionally, display this information in your UI, or pass it to another activity
        } else {
            System.out.println("No closest route found.");
        }
    }


    private double manhattanDistance(LatLng point1, LatLng point2) {
        // Calculate the Manhattan distance between two points
        return Math.abs(point1.latitude - point2.latitude) + Math.abs(point1.longitude - point2.longitude);
    }
}