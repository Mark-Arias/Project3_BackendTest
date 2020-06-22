package com.example.project3_backendtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CarDetailsActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> vehiclesList; // local storage for transmitted data from main activity
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        vehiclesList = new ArrayList<>();   // init empty list
        text1 = findViewById(R.id.textView);
        text2 = findViewById(R.id.textView2);
        text3 = findViewById(R.id.textView3);
        text4 = findViewById(R.id.textView4);


        Intent intent = getIntent();    // create intent for data retrieval
        vehiclesList = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("data");   // get vehicles list

        System.out.println("*********");
        String position = intent.getStringExtra("position");   // get position index
        System.out.println(position);
        System.out.println("*********");

        int positionNum = Integer.parseInt(position);   // convert to int

        // extract needed car info

        String vin = vehiclesList.get(positionNum).get("vin_number");
        String price = vehiclesList.get(positionNum).get("price");
        String created_at = vehiclesList.get(positionNum).get("created_at");
        String veh_description = vehiclesList.get(positionNum).get("veh_description");
        String image_url = vehiclesList.get(positionNum).get("image_url");
        String mileage = vehiclesList.get(positionNum).get("mileage");
        String make = vehiclesList.get(positionNum).get("veh_make");
        String model = vehiclesList.get(positionNum).get("model");




        text1.setText(vin);
        text2.setText(veh_description);
        text3.setText(mileage);
        String temp = make + " " + model;
        text4.setText(temp);



    }
}