package com.example.feliz.checked_in;

/**
 * Created by Feliz on 2017/10/05.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Category_Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String TAG = Event_Profile.class.getSimpleName();
    private TextView txtDate,txtTitle,txtTime,txtDuration,txtLocation,txtDescription;

    private Button btnCheck;
    private Event mEvent;

    String tile;

    String data;
    public ArrayList<HashMap<String, String>> eList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtDate= (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtDuration = (TextView)findViewById(R.id.txtDuration);
        txtLocation = (TextView)findViewById(R.id.txtLocation);
        txtDescription = (TextView)findViewById(R.id.txtDescription);



        ArrayList<Object> object = (ArrayList<Object>) getIntent().getSerializableExtra("Array");
        //   ArrayList<HashMap<String, String>> arl = new  ArrayList<HashMap<String, String>>();

        JSONArray arr = new JSONArray(object);
        for(int i =0;i<arr.length();i++){
            tile = getIntent().getExtras().getString("name");

            try {

                if(arr.getJSONObject(i).getString("Name").equals(tile))
                {
                    txtTitle.setText(arr.getJSONObject(i).getString("name"));
                    txtLocation.setText(arr.getJSONObject(i).get("Name") +" " +arr.getJSONObject(i).get("address"));

                    txtDescription.setText(arr.getJSONObject(i).getString("description"));
                    txtTime.setText(arr.getJSONObject(i).getString("Time"));
                    txtDate.setText(arr.getJSONObject(i).getString("date"));
                    txtDuration.setText("2 Hours");
                }
                else
                {
                    Toast.makeText(this,"No MAtch",Toast.LENGTH_LONG).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //eList = new ArrayList<>(); //inst


        }
        String size = Integer.toString(arr.length());




    }

    public void Display()
    {
        Toast.makeText(this,tile,Toast.LENGTH_LONG).show();
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
                    for (int i = 0; i < events.length(); i++)
                    {
                        JSONObject c = events.getJSONObject(i);
                        String longitude = c.getString("longitude");
                        String latitude = c.getString("latitude");
                        String name = c.getString("name");
                        String address = c.getString("address");
                        String Name = c.getString("Name");
                        String description = c.getString("description");
                        String Time =c.getString("Time");
                        String date =c.getString("date");

                        // temporary hash map for single contact
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

}



