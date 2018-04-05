package com.example.feliz.checked_in;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import static com.example.feliz.checked_in.CreateEvent.REQUEST_LOCATION;

public class Event_Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String TAG = Event_Profile.class.getSimpleName();
    private TextView txtDate,txtTitle,txtTime,txtDuration,txtLocation,txtDescription,txtNumber;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private Button btnCheck;
    private Event mEvent;
    public  Double lat, lng;
    public Double elat, elng;


    String data;
    public ArrayList<HashMap<String, String>> eList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setContentView(R.layout.activity_event_details);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate= (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDuration = (TextView)findViewById(R.id.txtDuration);
        txtLocation = (TextView)findViewById(R.id.txtLocation);
        txtDescription = (TextView)findViewById(R.id.txtDescription);
        txtNumber =(TextView)findViewById(R.id.txtNumber);
        btnCheck = (Button)findViewById(R.id.btnCheck);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        ArrayList<Object> object = (ArrayList<Object>) getIntent().getSerializableExtra("arraylist");
     //   ArrayList<HashMap<String, String>> arl = new  ArrayList<HashMap<String, String>>();
        JSONArray arr = new JSONArray(object);
       List<String> list =  new ArrayList<String>();
        for(int i =0;i<arr.length();i++){
            String tile = getIntent().getExtras().getString("title");

            try {

                if(arr.getJSONObject(i).getString("Name").equals(tile))
                {

                   // txtevent.setText(arr.getJSONObject(i).get("Name").toString());
                    txtTitle.setText(arr.getJSONObject(i).getString("name"));
                    txtLocation.setText(arr.getJSONObject(i).get("Name") +" " +arr.getJSONObject(i).get("address"));
                    String num1 = arr.getJSONObject(i).get("longitude").toString();
                    String num2 = arr.getJSONObject(i).get("latitude").toString();
                    elng = Double.parseDouble(num1);
                    elat = Double.parseDouble(num2);
                    txtDescription.setText(arr.getJSONObject(i).getString("description"));
                    txtTime.setText(arr.getJSONObject(i).getString("Time"));
                    txtDate.setText(arr.getJSONObject(i).getString("date"));
                    txtDuration.setText("2 Hours");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        //eList = new ArrayList<>(); //inst
            getLocation();

            }
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String  latitude1, longitude1,latitude2, longitude2;
                latitude1 = elat.toString();
                longitude1 = elng.toString();

                latitude2 = lat.toString();
                longitude2 = lng.toString();

                float lat1 = Float.parseFloat(latitude1);
                float lng1 = Float.parseFloat(longitude1);
                float lat2 = Float.parseFloat(latitude2);
                float lng2 = Float.parseFloat(longitude2);
                float distance = distFrom(lat1,lng1,lat2,lng2);

               float dist = distance;
                if (dist <1)
                {

                    // Insert checkin to db Checkin()
                    Toast.makeText(getApplicationContext(), "You have successfully checked in ", Toast.LENGTH_LONG).show();
                    String num = txtNumber.getText().toString();
                    int number = Integer.parseInt(num);
                    int result = number +1;
                    String res = String.valueOf(result);
                    txtNumber.setText(res);
                    btnCheck.setVisibility(View.GONE);

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Unable to checkin", Toast.LENGTH_LONG).show();
                }

            }
        });


    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6372.8f; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
          //  Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
            //startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_tickets) {

            Intent intent = new Intent(getApplicationContext(), Tickets.class);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(getApplicationContext(), FeedsActivity.class);
            startActivity(intent);


        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class JSONTask extends AsyncTask<String,String, List<EventModel> > {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<EventModel> doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response

            String url = Constants.URL_Data;
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);
            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray events = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);
                        String longitude = c.getString("longitude");
                        String latitude = c.getString("latitude");
                        String name = c.getString("name");
                        String address = c.getString("address");
                        String Name = c.getString("Name");
                        String description = c.getString("description");
                        String Time =c.getString("Time");
                        String date =c.getString("date");


                        // tmp hash map for single contact
                        HashMap<String, String> eve = new HashMap<>();

                        // adding each child node to HashMap key => value
                        eve.put("longitude", longitude);
                        eve.put("latitude", latitude);
                        eve.put("Name",Name);
                        eve.put("address",address);
                        eve.put("name",name);
                        eve.put("description",description);
                        eve.put("Time",Time);
                        eve.put("date",date);

                        // adding contact to contact list
                        eList.add(eve);
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }

            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(final List<EventModel> result) {
            super.onPostExecute(result);
        }

    }
    public Event getEvent() {
        return mEvent;

    }
    void getLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        }
        else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null){
                lat = location.getLatitude();
                lng = location.getLongitude();


            }
        }

    }
}



