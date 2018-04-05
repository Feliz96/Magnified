package com.example.feliz.checked_in;


import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Category extends ActionBarActivity
{
    private String TAG = Category.class.getSimpleName();
    private ImageView Restaurant;
    private ImageView Movies;
    private ImageView Club;
    private ImageView Academic;
    private ImageView  Fashion;
    private ImageView  Social;
    public ArrayList<HashMap<String, String>> eList;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.categories);
        eList = new ArrayList<>();
        Restaurant = (ImageView)findViewById(R.id.imgRestaurant);
        Movies = (ImageView)findViewById(R.id.imgMovies);
        Club = (ImageView)findViewById(R.id.imgClub);
        Academic = (ImageView)findViewById(R.id.imgAcademic);
        Fashion = (ImageView)findViewById(R.id.imgFash);
        Social = (ImageView)findViewById(R.id.imgSocial);

        Restaurant.setClickable(true);
        Movies.setClickable(true);
        Club.setClickable(true);
        Academic.setClickable(true);
        Fashion.setClickable(true);
        Social.setClickable(true);

        Restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), restaurants.class);
                startActivity(intent);
            }
        });

        Movies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), movie.class);
                startActivity(intent);
            }
        });

        Club.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), club.class);
                startActivity(intent);

            }
        });
        Academic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), academic.class);
                startActivity(intent);
            }
        });
        Fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), fashion.class);
                startActivity(intent);
            }
        });
        Social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), social.class);
                startActivity(intent);

            }
        });
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
                    JSONArray category = jsonObj.getJSONArray("server_response");

                    // looping through All Contacts
                    for (int i = 0; i < category.length(); i++) {
                        JSONObject c = category.getJSONObject(i);
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
                        String eventtype = c.getString("type");
                        String eventrsvp = c.getString("rsvp");
                        String eventCategory = c.getString("category");

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
                        eve.put("type",eventtype);
                        eve.put("rsvp",eventrsvp);
                        eve.put("category",eventCategory);

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