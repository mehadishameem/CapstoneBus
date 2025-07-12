package com.example.capstonebus;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.google.gson.Gson;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RouteDB extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME = "Routes.db";

    private static final String TABLE_NAME ="Routes_List";

    private static final int VERSION_NUMBER=1;

    private Context context;

    public RouteDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE  "+TABLE_NAME+" ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "route_name TEXT,"
                +"route_info TEXT"
                + ")";

        try{
            db.execSQL(sql);

            System.out.println("oncreat called for sqlite database creation");

        }catch (Exception e){
            Toast.makeText(context,"Exception :"+e,Toast.LENGTH_LONG).show();
        }


    }

    public void insertRoutes(Map<String, List<BusRoute>> map) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cols = new ContentValues();
        for(Map.Entry<String,List<BusRoute>> entry : map.entrySet()){
            String mapkey = entry.getKey();

            List<BusRoute> routes = entry.getValue();



            Gson gson = new Gson();
            String routesJson = gson.toJson(routes);
            cols.put("mapkey",mapkey);
            cols.put("routes_info",routesJson);
            db.insert(TABLE_NAME, null ,  cols);

        }

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {



    }
}
