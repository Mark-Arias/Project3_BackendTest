package com.example.project3_backendtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class CarDetailsActivity extends AppCompatActivity {

    private ArrayList<HashMap<String,String>> vehiclesList; // local storage for transmitted data from main activity
    private TextView makeModelTextView;
    private TextView priceTextView;
    private TextView descriptionTextView;
    private TextView vinTextView;
    private TextView lastUpdateTextView;

    private ImageView carImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        // bind UI components to code
        vehiclesList = new ArrayList<>();   // init empty list
        makeModelTextView = findViewById(R.id.makeModelTextView);
        priceTextView = findViewById(R.id.priceTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        vinTextView = findViewById(R.id.vinTextView);
        lastUpdateTextView = findViewById(R.id.lastUpdateTextView);
        carImageView = findViewById(R.id.carImageView);

        // Use intent to retrieve data hashmap
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
        String make = vehiclesList.get(positionNum).get("vehicle_make");
        String model = vehiclesList.get(positionNum).get("model");



        // set UI components with passed in data
        String temp = make + " | " + model;
        makeModelTextView.setText(temp);
        String temp2 = "$ " + price;
        priceTextView.setText(temp2);
        descriptionTextView.setText(veh_description);
        vinTextView.setText(vin);
        lastUpdateTextView.setText(created_at);

        assert model != null;
        if(model.equals("Model S")) {                   // Tesla's
            setCarImage("tesla_model_s");

        } else if (model.equals("Model X")) {
            setCarImage("tesla_model_x");
        }

        if(model.equals("DB11")) {                      // Aston Martin's
            setCarImage("tesla_model_s");
        } else if(model.equals("V12 Vantage")) {
            setCarImage("tesla_model_s");
        }





    }


    private void setCarImage(String modelName) {
        int drawableResourceId = getResources().getIdentifier(modelName, "drawable",getPackageName());
        carImageView.setImageResource(drawableResourceId);
    }



}