package com.example.feliz.checked_in;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Feliz on 2017/10/04.
 */

public class restaurants extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> eList;
    public String TAG = academic.class.getSimpleName();
    int Images[] = {R.drawable.restaurant};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academic);
        eList = new ArrayList<>();
        ListView listview = (ListView) findViewById(R.id.academic);

        new JSONTask().execute();

        CustomAdapter customAdapter = new CustomAdapter();
        listview.setAdapter(customAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                TextView txtname = (TextView)findViewById(R.id.txtCname);
                String name = txtname.getText().toString();
                //Toast.makeText(getApplicationContext(),name,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), Category_Profile.class);

                intent.putExtra("name",name);
                intent.putExtra("Array",eList);
                startActivity(intent);
            }
        });





    }

    class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return eList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = getLayoutInflater().inflate(R.layout.custom_layout, null);
            ImageView img = (ImageView) convertView.findViewById(R.id.imageView);
            TextView name = (TextView) convertView.findViewById(R.id.txtCname);
            TextView description = (TextView) convertView.findViewById(R.id.txtCDescription);

            for(int i =0 ;i<eList.size();i++)
            {
                String cat = eList.get(i).get("category");
                int category = Integer.parseInt(cat);
               // Toast.makeText(getApplicationContext(),cat.toString(),Toast.LENGTH_LONG).show();



                if(cat.toString().equals("2")==true) {
                    Toast.makeText(getApplicationContext(),  String.valueOf(position),Toast.LENGTH_SHORT).show();
                    name.setText(eList.get(i).get("eventname"));
                    description.setText(eList.get(i).get("eventdescription"));
                    img.setImageResource(Images[0]);
                    return convertView;
                }
                else
                {
                    name.setText("");
                    description.setText("");
                    img.setImageResource(0);
                    return convertView;
                }


            }
            //String cat = eList.get(position).get("category");
            //int category = Integer.parseInt(cat);
            //Toast.makeText(getApplicationContext(),cat.toString(),Toast.LENGTH_LONG).show();

            //img.setImageResource(Images[0]);

           // if(cat.equals("1")) {
             //   name.setText(eList.get(position).get("eventname"));
               // description.setText(eList.get(position).get("eventdescription"));
            //}



            return convertView;
        }
    }

    public class JSONTask extends AsyncTask<String, String, List<EventModel>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<EventModel> doInBackground(String... params) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
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
                        String eventtype = c.getString("type");
                        String eventrsvp = c.getString("rsvp");
                        String eventCategory = c.getString("category");

                        // tmp hash map for single contact
                        HashMap<String, String> eve = new HashMap<>();

                        // adding each child node to HashMap key => value
                        eve.put("id", id);
                        eve.put("userID", userID);
                        eve.put("eventID", eventID);
                        eve.put("Ticketname", Ticketname);
                        eve.put("Ticketdescription", Ticketdescription);
                        eve.put("Ticketprice", Ticketprice);
                        eve.put("numTickets", numTickets);
                        eve.put("numSold", numSold);
                        eve.put("expiryDate", expiryDate);
                        eve.put("eventid", eventid);
                        eve.put("eventclub", eventclub);
                        eve.put("eventname", eventname);
                        eve.put("eventdescription", eventdescription);
                        eve.put("eventdate", eventdate);
                        eve.put("eventtime", eventtime);
                        eve.put("type", eventtype);
                        eve.put("rsvp", eventrsvp);
                        eve.put("category", eventCategory);

                        // adding contact to contact list

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
    }
}