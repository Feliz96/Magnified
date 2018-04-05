package com.example.feliz.checked_in;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.feliz.checked_in.AppController;
import com.example.feliz.checked_in.Constants;
import com.example.feliz.checked_in.FeedItem;
import com.example.feliz.checked_in.FeedListAdapter;
import com.example.feliz.checked_in.R;
import com.example.feliz.checked_in.Tickets;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FeedsActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener{
    public ArrayList<HashMap<String, String>> eList;
    private static final String TAG = FeedsActivity.class.getSimpleName();
    private ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;
    private String URL_FEED = Constants.URL_EVENT_DETAILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feeds);

        listView = (ListView) findViewById(R.id.list);
        eList = new ArrayList<>();
        feedItems = new ArrayList<>();

        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Object obj = listView.getAdapter().getItem(position);

                TextView txtname = (TextView)findViewById(R.id.name);
                String name = txtname.getText().toString();

                Intent intent = new Intent(getApplicationContext(), Buy_Ticket.class);
                intent.putExtra("title",name);
                intent.putExtra("arraylist",eList);
                startActivity(intent);
            }
        });

        // We first check for cached request
        Cache cache = AppController.getInstance().getRequestQueue().getCache();
        Entry entry = cache.get(URL_FEED);
        if (entry != null) {
            // fetch the data from cache
            try {
                String data = new String(entry.data, "UTF-8");
                try {
                    parseJsonFeed(new JSONObject(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            // making fresh volley request and getting json
            JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                    URL_FEED, null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    VolleyLog.d(TAG, "Response: " + response.toString());
                    if (response != null) {
                        parseJsonFeed(response);
                    }
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }
            });

            // Adding request to volley request queue
            AppController.getInstance().addToRequestQueue(jsonReq);
        }

    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("server_response");

                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject c = feedArray.getJSONObject(i);
                    FeedItem item = new FeedItem();
                    item.setId(c.getString("id"));

                    item.setName(c.getString("eventname"));

                    // Image might be null sometimes
                    //String image = feedObj.isNull("image") ? null : feedObj
                    //      .getString("image");

                    item.setImge("@drawable/upcoming.jpg");

                    item.setStatus("Event Description : " +c.getString("eventdescription") +"\n");

                    item.setProfilePic("@drawable/profile.png");
                    item.setTimeStamp(c.getString("eventdate")+"\n"+c.getString("eventtime"));

                    feedItems.add(item);


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
                    eve.put("expiryDate",eventtime);
                    eve.put("type",eventtype);
                    eve.put("rsvp",eventrsvp);
                    eve.put("category",eventCategory);

                    // adding contact to contact list
                    eList.add(eve);
                }

            }

        catch (final JSONException e) {
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

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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
}
