package com.example.project3_backendtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Activity in charge of render and display of information in the details view
 *  that emerges when a user selects a specific car from the list of cars.
 */
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
        String position = intent.getStringExtra("position");   // get position index
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
        descriptionTextView.setText("Vehicle Info:\n\n" + veh_description);
        vinTextView.setText("VIN: " + vin + "  |  " + "Mileage: " + mileage);
        lastUpdateTextView.setText("Last Update: " + created_at);


        renderCarInUI(model);

    }


    /**
     * Method renders car image for inputted car model in UI
     * @param model The car model to find image of
     */
    private void renderCarInUI(String model) {
        assert model != null;
        if(model.equals("Model S")) {                   // Tesla's
            setCarImage("tesla_model_s");

        } else if (model.equals("Model X")) {
            setCarImage("tesla_model_x");
        }

        if(model.equals("DB11")) {                      // Aston Martin's
            setCarImage("db11");
        } else if(model.equals("V12 Vantage")) {
            setCarImage("v12vantage");
        }

        if(model.equals("Continental")) {               // Bentley's
            setCarImage("continental");
        }

        if(model.equals("M6")) {                        // BMW's
            setCarImage("m6");
        }

        if(model.equals("360")) {                       // Ferrari's
            setCarImage("f360");
        } else if(model.equals("F430")) {
            setCarImage("f430");
        }

        if(model.equals("XJ")) {                        // Jaguars
            setCarImage("xj");
        }

        if(model.equals("Aventador")) {                 // Lamborghini's
            setCarImage("aventador");
        } else if (model.equals("Huracan")) {
            setCarImage("huracan");
        } else if (model.equals("Urus")) {
            setCarImage("urus");
        }

        if(model.equals("GranTurismo")) {               // Maserati's
            setCarImage("gran_turismo");
        } else if(model.equals("Levante")) {
            setCarImage("levante");
        }

        if(model.equals("Boxter")) {                    // Porsche's
            setCarImage("boxter");
        } else if(model.equals("Cayman")) {
            setCarImage("cayman");
        }

    }


    /**
     * Method finds res id of desired car image for app rendering
     * @param modelName string of car name
     */
    private void setCarImage(String modelName) {
        int drawableResourceId = getResources().getIdentifier(modelName, "drawable",getPackageName());
        carImageView.setImageResource(drawableResourceId);
    }



}