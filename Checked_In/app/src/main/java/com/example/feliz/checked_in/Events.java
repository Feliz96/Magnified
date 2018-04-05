package com.example.feliz.checked_in;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
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

/**
 * Created by Feliz on 2017/09/14.
 */

public class Events extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private String TAG = Events.class.getSimpleName();
    private TextView txtinfo,txtevent,txtdescription;
    private ImageView txtaddress;
    private Button btnCheck;
    public Constants c;

    String data;
    public ArrayList<HashMap<String, String>> eList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upcoming_event);
        txtevent = (TextView) findViewById(R.id.txtEventName);
        txtaddress= (ImageView) findViewById(R.id.txtAddress);
        txtinfo = (TextView) findViewById(R.id.txtInfo);
        txtdescription = (TextView)findViewById(R.id.txtDescription);


        ArrayList<Object> object = (ArrayList<Object>) getIntent().getSerializableExtra("arraylist");
        //   ArrayList<HashMap<String, String>> arl = new  ArrayList<HashMap<String, String>>();
        JSONArray arr = new JSONArray(object);
        List<String> list =  new ArrayList<String>();
        for(int i =0;i<arr.length();i++){
            String tile = getIntent().getExtras().getString("title");

            try {

                if(arr.getJSONObject(i).getString("eventname").equals(tile))
                {
                    txtevent.setText("Event Name: " +arr.getJSONObject(i).get("eventname") + "\n" + " At Club : " + arr.getJSONObject(i).get("clubName")  +"\n" +arr.getJSONObject(i).get("address") );
                    txtdescription.setText("Description: " +arr.getJSONObject(i).getString("eventdescription")+"\n"+arr.getJSONObject(i).getString("eventdate") +"\n"+arr.getJSONObject(i).getString("eventtime")  );
                    txtinfo.setText("Ticket Price: " +arr.getJSONObject(i).getString("Ticketprice") +"\n" );
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
            //eList = new ArrayList<>(); //inst


        }
        String size = Integer.toString(arr.length());

        Toast.makeText(this,getIntent().getExtras().getString("title"),Toast.LENGTH_LONG).show();



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
            c = new Constants();
            String url = Constants.URL_EVENT_DETAILS;
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
                        String id = c.getString("id");
                        String userID = c.getString("userID");
                        String eventID = c.getString("eventID");
                        String Ticketname = c.getString("Ticketname");
                        String Ticketdescription = c.getString("Ticketdescription");
                        String Ticketprice = c.getString("Ticketprice");
                        String numTickets = c.getString("numTickets");
                        String numSold = c.getString("numSold");
                        String expiryDate = c.getString("expiryDate");
                        String eventid = c.getString("eventid");
                        String eventclub = c.getString("eventclub");
                        String eventname = c.getString("eventname");
                        String eventdescription = c.getString("eventdescription");
                        String eventdate = c.getString("eventdate");
                        String eventtime = c.getString("eventtime");

                        // tmp hash map for single contact
                        HashMap<String, String> eve = new HashMap<>();

                        // adding each child node to HashMap key => value
                        eve.put("id", id);
                        eve.put("userID", userID);
                        eve.put("eventID",eventID);
                        eve.put("Ticketname",Ticketname);
                        eve.put("Ticketdescription",Ticketdescription);
                        eve.put("Ticketprice",Ticketprice);
                        eve.put("numTickets",numTickets);
                        eve.put("numSold",numSold);
                        eve.put("expiryDate",expiryDate);
                        eve.put("eventid",eventid);
                        eve.put("eventclub",eventclub);
                        eve.put("eventname",eventname);
                        eve.put("eventdescription",eventdescription);
                        eve.put("eventdate",eventdate);
                        eve.put("eventtime",eventtime);
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

}



