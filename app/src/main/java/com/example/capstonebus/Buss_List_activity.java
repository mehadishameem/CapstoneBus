package com.example.capstonebus;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Buss_List_activity extends AppCompatActivity {

    private ListView busList;
    private  String[] bus_name;
    private  String[] start;
    private  String[] end;

    private  String[] stoppages;
    private  String[] distances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_buss_list);

        busList = findViewById(R.id.busList);

        bus_name = getResources().getStringArray(R.array.bus_name);
        start = getResources().getStringArray(R.array.start);
        end = getResources().getStringArray(R.array.end);

        stoppages = getResources().getStringArray(R.array.stoppages);
        distances = getResources().getStringArray(R.array.distances);

        CustomAdapter c_adapter = new CustomAdapter(this,bus_name,start,end,stoppages,distances);
        busList.setAdapter(c_adapter);




    }
}