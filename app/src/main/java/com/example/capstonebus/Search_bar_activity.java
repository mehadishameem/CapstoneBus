package com.example.capstonebus;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;  // Corrected import

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search_bar_activity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView fromList;
    private SearchView whereFromGo;  // Using the correct SearchView class
    ArrayList<BusRoute> busRouteList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> locationList;

    DatabaseReference databaseReference;

    String touch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        //unnecessary we will create a signletone class in search_activity or clreate local database so that we can fetch data


        databaseReference = FirebaseDatabase.getInstance().getReference("Routes");


        // Initialize UI elements
        fromList = findViewById(R.id.fromList);
        whereFromGo = findViewById(R.id.whereFromGo);

        touch = getIntent().getStringExtra("etTo");

        // Sample Data
        String[] locations = {
                "Rampura", "Gulistan", "Sonirakhra", "Dhanmondi", "Mirpur", "Uttara", "Banani",
                "Mohammadpur", "Motijheel", "Tejgaon", "Shahbagh", "Farmgate", "Gulshan", "Kawran Bazar"
        };
        locationList = new ArrayList<>(Arrays.asList(locations));

        // Setup Adapter
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, locationList);
        fromList.setAdapter(arrayAdapter);
        fromList.setOnItemClickListener(this);

        // Fix SearchView (Expand by default)
        whereFromGo.setIconifiedByDefault(false);

        // Implement Search Functionality
        whereFromGo.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String value = arrayAdapter.getItem(position);
        Toast.makeText(this, "Selected: " + value, Toast.LENGTH_SHORT).show();

        Intent resultIntent = new Intent();
        if ("etTo".equalsIgnoreCase(touch)) {
            resultIntent.putExtra("selected2", value);
        } else {
            resultIntent.putExtra("selected", value);
        }

        setResult(RESULT_OK, resultIntent);
        finish(); // Close this activity and return to Search_activity
    }
    /*private void fetchRoutesData() {
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


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any errors that occur while fetching data
                Toast.makeText(Search_activity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }*/

}
