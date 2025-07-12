package com.example.capstonebus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

      String[] bus_name;
      String[] start;
     String[] end;

      String[] stoppages;
      String[] distances;
    Context context;
    private LayoutInflater inflater;

    CustomAdapter(Context context, String[] bus_name, String[] start, String[] end, String[] stoppages,String[] distances){

        this.context=context;
        this.bus_name=bus_name;
        this.start = start;
        this.end = end;
        this.stoppages=stoppages;
        this.distances = distances;
    }
    @Override
    public int getCount() {
        return bus_name.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
           inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
           convertView= inflater.inflate(R.layout.buses_row,parent,false);
        }

        TextView textView1= convertView.findViewById(R.id.busName);
        TextView textView2= convertView.findViewById(R.id.startTime);
        TextView textView3= convertView.findViewById(R.id.endTime);
        TextView textView4= convertView.findViewById(R.id.placeName);
        TextView textView5= convertView.findViewById(R.id.distance);

        textView1.setText(bus_name[position]);
        textView2.setText(start[position]);
        textView3.setText(end[position]);
        textView4.setText(stoppages[position]);
        textView5.setText(distances[position]);

        return convertView;
    }
}
