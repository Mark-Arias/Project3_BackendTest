package com.example.project3_backendtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //----------------------------------------------------------------------------------------------
    // UI components for the main activity
    private String TAG = MainActivity.class.getSimpleName();    // get simple name of the class in the source code

    private ProgressDialog pDialog;
    private ListView lv;
    private Spinner spinner;
    private Spinner modelSpinner;

    private CarDetails carDetailFragment;

    //----------------------------------------------------------------------------------------------
    // URL's for connection to specified remote servers
    //
    private static String carMakes = "https://thawing-beach-68207.herokuapp.com/carmakes";  // link to car makes(car brand)
    private static String url = carMakes;

    // proper model makes are retrieved by appending the id of that car model to the end of this url
    StringBuilder carModelsURLString = new StringBuilder("https://thawing-beach-68207.herokuapp.com/carmodelmakes/");   // using String builder to exploit mutability
    private static String carModelsURL = "https://thawing-beach-68207.herokuapp.com/carmodelmakes/";   // invalid link without makeID appended to the end

    // link to retreivable vehicles
    // format of appending string /<make>/<model>/<zipcode>
    // zipcode = 92603 (hardcoded zip)
    //TODO: the url in part c is sending data as a json object and not an array!
    // 3/23 are teslas
    StringBuilder availableVehicleURLSting = new StringBuilder("https://thawing-beach-68207.herokuapp.com/cars/10/20/92603");
    private static String availableVehicleURL = "https://thawing-beach-68207.herokuapp.com/cars/";
    private static String zipCode = "92603";

    //----------------------------------------------------------------------------------------------
    // local storage
    //ArrayList<HashMap<String, String>> contactList;
    private ArrayList<HashMap<String, String>> carMakesList;
    private ArrayList<String>  makeArray;
    private ArrayList<HashMap<String,String>> carModelsList;
    private ArrayList<String> modelArray;
    private ArrayList<HashMap<String,String>> vehiclesList;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //contactList = new ArrayList<>();    // init arraylist
        carMakesList = new ArrayList<>();   // init arraylist
        carModelsList = new ArrayList<>();
        vehiclesList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);    // init list view var to UI listview

        spinner = (Spinner) findViewById(R.id.make_spinner);    // init spinner
        spinner.setOnItemSelectedListener(this);

        modelSpinner = (Spinner) findViewById(R.id.model_spinner);
        modelSpinner.setOnItemSelectedListener(this);

        makeArray = new ArrayList<>();
        modelArray = new ArrayList<>();

        new GetMake().execute();    // create a new thread to acquire contacts as JSON from a remote server
        //new GetModel().execute();
        //new GetAvailableVehicles().execute();



        //TODO: This is where i need to put in some work
        // click listener for the list view, lets me get the selected item and info about that item listed inside the listView
        // Use the harvest information as done below to populate local variables with the needed info to create a new fragment
        // position can be used to get info about said ith item in the list. that is also the ith object in the vehicle list array
        // figure out how to create a new fragment, populate it with this data, and i am on the home stretch!
        lv.setOnItemClickListener((AdapterView<?> parent, View view, int position, long id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position).toString();   // this item is the hashmap with the values i need
            // to create a new fragment populated with all the right info
            // so fragment creation should happen here
           //System.out.println("User selected: " + selectedItem);
            System.out.println("harvested info");

            System.out.println(vehiclesList.get(position).get("vin_number"));
            System.out.println(vehiclesList.get(position).get("veh_description"));
            System.out.println(vehiclesList.get(position).get("price"));
            System.out.println(vehiclesList.get(position).get("created_at"));
            System.out.println(vehiclesList.get(position).get("image_url"));
            System.out.println(vehiclesList.get(position).get("vehicle_url"));

            /*
            for(int i = 0; i < vehiclesList.size(); i++ ){
                System.out.println(vehiclesList.get(i).get("vin_number"));
                System.out.println(vehiclesList.get(i).get("veh_description"));
                System.out.println(vehiclesList.get(i).get("price"));
                System.out.println(vehiclesList.get(i).get("created_at"));
                System.out.println(vehiclesList.get(i).get("image_url"));
                System.out.println(vehiclesList.get(i).get("vehicle_url"));

            }
             */
            // create a new fragment to display car info
            //carDetailFragment = CarDetails.newInstance("random","text");    // pass in a hashmap object with above info as a paramter to this fragment

            //ConstraintLayout fl = (ConstraintLayout) findViewById(R.id.parent);   // find Gallery fragment
            //fl.removeAllViews();    // remove views on fragment specified
            //((ViewGroup)lv.getParent()).removeView(lv);
            //FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();   // transaction object
            //transaction.replace(R.id.parentLayout, carDetailFragment);  // swap and replace in second param. into first param
            //transaction.addToBackStack(null);
            //transaction.commit();


            Intent myIntent = new Intent(MainActivity.this, CarDetailsActivity.class);
            myIntent.putExtra("data", vehiclesList); //send over the hashmap to the activity
            String temp = Integer.toString(position);
            myIntent.putExtra("position",temp); // send the position of the selected item in the list
            MainActivity.this.startActivity(myIntent);

        });

    }



    //----------------------------------------------------------------------------------------------
    // methods implemented to add item selection abilities to the spinners
    // method is passed in parent spinner, and uses position to identify the position selected by the user
    // and the corresponding data held by spinner at that location
    // parent param. lets this listener work on multiple spinners and distinguish between them
    //TODO:refactor case statements with a local method that encapsulates the replicated code
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        //String item = parent.getItemAtPosition(position).toString();
        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

        // case items below checking for user selection of a car make
        String item = parent.getItemAtPosition(position).toString();    // get string of selected car make( vehicle brand)
        switch(item) {      // find which make was selected to display the related models in that car brand
            case "Jaguar":  // id 2
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("2");
                new GetModel().execute();
                break;

            case "Tesla":   // id 3
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("3");
                new GetModel().execute();
                //lv.clearChoices();
                //new GetAvailableVehicles().execute();
                break;

            case "Lamborghini":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("4");
                new GetModel().execute();
                break;

            case "Ferrari":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("5");
                new GetModel().execute();
                break;

            case "Porsche":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("6");
                new GetModel().execute();
                break;

            case "Bugatti":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("7");
                //System.out.println("Test");
                //System.out.println(carModelsURLString.toString());
                new GetModel().execute();
                break;

            case "Maserati":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("8");
                new GetModel().execute();
                break;

            case "BMW":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("9");
                new GetModel().execute();
                break;

            case "Aston Martin":
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("10");
                new GetModel().execute();
                break;

            case "Bentley":     // id 11
                carModelsURLString.replace(0,carModelsURLString.length(),carModelsURL);
                carModelsURLString.append("11");
                new GetModel().execute();
                break;

            default:
                //System.out.println("Invalid input");

        }



        // cases below checking for user selction of a specific model in the modelSpinner



        // TODO: an ideal todo would be to try and refactor this code, and do less hardcoding and retrieve the make and model id info from the relavant hashmap objects
        // will need to play around a bit to tinker with that and get that up and running
        // Tesla Cases, make id = 3
        switch (item) {
            case "Model X": // model id = 23
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                availableVehicleURLSting.replace(0,availableVehicleURLSting.length(),availableVehicleURL);
                //lv.clearChoices();
                vehiclesList.clear();   // clear out old vehicle list data
                availableVehicleURLSting.append("3/23/");
                availableVehicleURLSting.append(zipCode);
                new GetAvailableVehicles().execute();
                break;
            case "Model S": //model id = 3
                Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
                availableVehicleURLSting.replace(0,availableVehicleURLSting.length(),availableVehicleURL);
                //lv.clearChoices();
                vehiclesList.clear();   // clear out old vehicle list data
                availableVehicleURLSting.append("3/3/");
                availableVehicleURLSting.append(zipCode);
                new GetAvailableVehicles().execute();
                break;
            default:
        }


        // Lamborghini Cases, make id = 4
        switch (item) {
            case "Aventador": // model id = 8
                populateList("4/8/");
                break;
            case "Huracan": //model id = 6
                populateList("4/6/");
                break;
            case "Urus": //model id = 7
                populateList("4/7/");
                break;
            default:
        }


        // Aston Martin Cases, make id = 10
        switch (item) {
            case "DB11": // model id = 21
                populateList("10/21/");
                break;
            case "V12 Vantage": //model id = 20
                populateList("10/20/");
                break;
            default:
        }


        // Bentley Cases, make id = 11
        if ("Continental".equals(item)) { // model id = 22
            populateList("11/22/");
        }

        // BMW Cases, make id = 9
        if("M6".equals(item)) { // model id = 19
            populateList("9/19/");
        }

        // Bugatti Cases, make id = 7       //TODO: case does not work
        if("Chiron".equals(item)) { // model id = 12
            populateList("7/12/");
        }

        // Maserati Cases, make id = 8
        switch (item) {
            case "GranTurismo": // model id = 13
                populateList("8/13/");
                break;
            case "Levante": //model id = 14
                populateList("8/14/");
                break;
            case "spyder": //model id = 15          //TODO: case does not work
                populateList("8/15/");
                break;
            default:
        }


        // Ferrari Cases, make id = 5
        switch (item) {
            case "360": // model id = 4
                populateList("5/4/");
                break;
            case "F430": //model id = 5
                populateList("5/5/");
                break;
            default:
        }


        // Jaguar Cases, make id = 2
        if("XJ".equals(item)) { // model id = 2
            populateList("2/2/");
        }


        // Porsche Cases, make id = 6
        switch (item) {
            case "911": // model id = 9             //TODO: case does not work
                populateList("6/9/");
                break;
            case "Boxter": //model id = 10
                populateList("6/10/");
                break;
            case "Cayman": //model id = 11
                populateList("6/11/");
                break;
            default:
        }

        /*
        System.out.println(carMakesList.size());
        System.out.println("specif node info");
        System.out.println(carMakesList.get(0));
        System.out.println(carMakesList.get(1));
        System.out.println(carMakesList.get(2));
        System.out.println(carMakesList.get(3));

        for(int i = 0; i < carMakesList.size(); i++) {
            if(carMakesList.get(i).containsValue("BMW")) {
                System.out.println("Value:");
                System.out.println(carMakesList.get(i).get("BMW"));
            }
        }
        carMakesList.get(0).get("BMW");
         */

    }

    private void populateList(String modelInfo) {
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
        availableVehicleURLSting.replace(0,availableVehicleURLSting.length(),availableVehicleURL);
        //lv.clearChoices();
        vehiclesList.clear();   // clear out old vehicle list data
        availableVehicleURLSting.append(modelInfo);
        availableVehicleURLSting.append(zipCode);
        new GetAvailableVehicles().execute();
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //----------------------------------------------------------------------------------------------

    /**
     * Class performs an asyncrhonous remote server request for json data about
     * the available cars for a specified car brand(make) and car model
     */
    private class GetAvailableVehicles extends AsyncTask<Void,Void,Void> {
        /**
         * actions to execute before invoking background thread
         */
        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

         */


        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();     // create new httphandler instance
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(availableVehicleURLSting.toString());
            Log.e(TAG, "Response from url: " + jsonStr);    // log the response from url to the terminal

            if (jsonStr != null) {  // if not null, then connection made, and data was passed from service call to the url
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray lists = jsonObj.getJSONArray("lists");    // convert json object to json array with specified name

                    //vehiclesList.clear();   // clear out the hashmap for each new run of this code,

                    // otherwise the list view gets cluttered with old calls info


                    // looping through All list of vehicles
                    for (int i = 0; i < lists.length(); i++) {
                        JSONObject c = lists.getJSONObject(i);   // create a temp json object c, and set it the ith json object in lists

                        // extract local fields from the json object
                        String color = c.getString("color");
                        String created_at = c.getString("created_at");
                        String id = c.getString("id");
                        String image_url = c.getString("image_url");
                        String mileage = c.getString("mileage");
                        String model = c.getString("model");
                        String price = c.getString("price");
                        String veh_description = c.getString("veh_description");
                        String vehicle_make = c.getString("vehicle_make");
                        String vehicle_url = c.getString("vehicle_url");
                        String vin_number = c.getString("vin_number");
                        //System.out.println(id);
                        //System.out.println(model);
                        //System.out.println(vehicle_make_id);

                        // tmp hashmap for storing a single available vehicles list
                        HashMap<String,String>  availableVehiclesList = new HashMap<>();

                        // add each child node to HashMap key => value
                        availableVehiclesList.put("color",color);
                        availableVehiclesList.put("created_at",created_at);
                        availableVehiclesList.put("id",id);
                        availableVehiclesList.put("image_url",image_url);
                        availableVehiclesList.put("mileage",mileage);

                        availableVehiclesList.put("model",model);
                        availableVehiclesList.put("price",price);
                        availableVehiclesList.put("veh_description",veh_description);
                        availableVehiclesList.put("vehicle_make",vehicle_make);
                        availableVehiclesList.put("vehicle_url",vehicle_url);
                        availableVehiclesList.put("vin_number",vin_number);

                        vehiclesList.add(availableVehiclesList);

                        // create a individual array later if needed to get simple access to some subfield info
                        //modelArray.add(model);    // create an arraylist of only the models

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());    // invalid json received from url
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { // execute code snippet inside the main thread
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show(); // create notific. toast
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");   // connection was not sucesfully established
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }


        /**
         * Actions to perform after background thread has finished
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            /**
             * Updating parsed JSON data into ListView
             * */


            //lv.clearChoices();

            ListAdapter adapter = new SimpleAdapter(getApplicationContext(),vehiclesList,R.layout.list_item, new String []{"model", "price", "vin_number"},
                    new int[]{R.id.name,R.id.email,R.id.mobile});
            lv.setAdapter(adapter);
            //vehiclesList.clear();

            //ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, modelArray); //selected item will look like a spinner set from XML
            //spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //modelSpinner.setAdapter(spinnerArrayAdapter2);

        }

    }




    /**
     * Retrieves the car model information stored on the remote server
     */
    private class GetModel extends AsyncTask<Void,Void,Void> {

        /**
         * actions to execute before invoking background thread
         */
        /*
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

         */


        @Override
        protected Void doInBackground(Void... arg0) {

            HttpHandler sh = new HttpHandler();     // create new httphandler instance

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(carModelsURLString.toString());

            Log.e(TAG, "Response from url: " + jsonStr);    // log the response from url to the terminal


            if (jsonStr != null) {  // if not null, then connection made, and data was passed from service call to the url
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray carModelsArray = new JSONArray(jsonStr);    // getting a JSONArray from the website, so use inbound jsonStr as input param. to create it

                    // Getting JSON Array node
                    //JSONArray carmakes = jsonObj.getJSONArray("carmakes");
                    modelArray.clear(); // clear oout the model array to prevent the spinner from being over populated

                    // looping through All carMakes
                    for (int i = 0; i < carModelsArray.length(); i++) {
                        JSONObject c = carModelsArray.getJSONObject(i);   // create a temp json object c, and set it the ith json object in carModels



                        String id = c.getString("id");
                        //System.out.println(id);
                        String model = c.getString("model");
                        //System.out.println(model);
                        String vehicle_make_id = c.getString("vehicle_make_id");
                        //System.out.println(vehicle_make_id);

                        HashMap<String,String>  carModels = new HashMap<>();

                        carModels.put("vehicle_make_id",vehicle_make_id);
                        carModels.put("model",model);

                        carModelsList.add(carModels);

                        modelArray.add(model);    // create an arraylist of only the models

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());    // invalid json received from url
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { // execute code snippet inside the main thread
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show(); // create notific. toast
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");   // connection was not sucesfully established
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }


        /**
         * Actions to perform after background thread has finished
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            /**
             * Updating parsed JSON data into ListView
             * */


            //modelSpinner.setAdapter(null);

            ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, modelArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            modelSpinner.setAdapter(spinnerArrayAdapter2);

        }
    }



    /**
     * Async task class to get json by making HTTP call
     * http call is made on a background thread by using this class extension
     *  retrieves the Make car information stored on the remote server
     */
    private class GetMake extends AsyncTask<Void, Void, Void> {

        /**
         * actions to execute before invoking background thread
         */

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        /**
         * Actions to execute in the background thread
         */
        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();     // create new httphandler instance

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);    // log the response from url to the terminal


            if (jsonStr != null) {  // if not null, then connection made, and data was passed from service call to the url
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);
                    JSONArray carMakesArray = new JSONArray(jsonStr);    // getting a JSONArray from the website, so use inbound jsonStr as input param. to create it

                    // Getting JSON Array node
                    //JSONArray carmakes = jsonObj.getJSONArray("carmakes");

                    // looping through All carMakes
                    for (int i = 0; i < carMakesArray.length(); i++) {
                        JSONObject c = carMakesArray.getJSONObject(i);   // create a temp json object c, and set it the ith json object in carMakes


                        String id = c.getString("id");
                        System.out.println(id);
                        String vehicle_make = c.getString("vehicle_make");
                        System.out.println(vehicle_make);

                        HashMap<String,String>  carMakes = new HashMap<>();

                        carMakes.put("id",id);
                        carMakes.put("vehicle_make",vehicle_make);

                        carMakesList.add(carMakes);

                        makeArray.add(vehicle_make);    // create an arraylist of only the makes

                    }


                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());    // invalid json received from url
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() { // execute code snippet inside the main thread
                            Toast.makeText(getApplicationContext(), "Json parsing error: " + e.getMessage(), Toast.LENGTH_LONG).show(); // create notific. toast
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");   // connection was not sucesfully established
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Couldn't get json from server. Check LogCat for possible errors!", Toast.LENGTH_LONG).show();
                    }
                });

            }

            return null;
        }

        /**
         * Actions to perform after background thread has finished
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            //ListAdapter adapter = new SimpleAdapter(MainActivity.this, carMakesList, R.layout.list_item, new String[]{"id", "vehicle_make"},
            //        new int[]{R.id.name, R.id.email});

            //ListAdapter adapter2 = new SimpleAdapter(MainActivity.this,carMakesList,R.layout.support_simple_spinner_dropdown_item,
             //       new String [] {"vehicle_make"}, new int[] {R.id.make_spinner});
            //spinner.setAdapter(adapter2);
            String colors[] = {"Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, makeArray); //selected item will look like a spinner set from XML
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerArrayAdapter);

            // Create an ArrayAdapter using the string array and a default spinner layout
            //ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getApplicationContext(), R.array.planets_array, android.R.layout.simple_spinner_item);
                    //.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            //spinner.setAdapter(adapter2);

            //lv.setAdapter(adapter);
        }

    }
}
