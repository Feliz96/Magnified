package com.example.feliz.checked_in;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by Feliz on 2017/10/18.
 */

public class Buy extends AppCompatActivity
{
    private TextView txtPrice, txtexpire, txtClub,txtTime, txtTitle;
    private Button btnTicket;
    public ArrayList<HashMap<String, String>> eList;
    private String TAG = Buy.class.getSimpleName();
    JSONArray arr;
    ImageView image;
    String text2Qr;
    String tile;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ticket_layout);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtexpire = (TextView) findViewById(R.id.txtexpire);
        txtClub = (TextView) findViewById(R.id.txtClub);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtTitle = (TextView)findViewById(R.id.txtTitle);
        image = (ImageView) findViewById(R.id.image);
        btnTicket =(Button)findViewById(R.id.btnTicket);
        eList = new ArrayList<>();
        tile = getIntent().getExtras().getString("title");

        final ArrayList<Object> object = (ArrayList<Object>) getIntent().getSerializableExtra("arraylist");
        arr = new JSONArray(object);

        for (int i = 0; i < arr.length(); i++) {
            tile = getIntent().getExtras().getString("title");

            try {

                if (arr.getJSONObject(i).getString("eventname").equals(tile)) {
                    txtPrice.setText("Price : "+ "R" + arr.getJSONObject(i).getString("Ticketprice"));
                    txtexpire.setText("Time: "+ arr.getJSONObject(i).getString("expiryDate"));
                    txtClub.setText("@ " +arr.getJSONObject(i).getString("eventname"));
                    txtTime.setText("Date: " +arr.getJSONObject(i).getString("eventdate"));
                    txtTitle.setText(arr.getJSONObject(i).getString("Ticketname"));

                    text2Qr =arr.getJSONObject(i).getString("eventname").toString();
                    MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                    try

                    {
                        BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,100,100);
                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                        Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                        image.setImageBitmap(bitmap);
                    }
                    catch (WriterException e){
                        e.printStackTrace();
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Notification.class);
                intent.putExtra("title",tile);
                intent.putExtra("arraylist",object);

                startActivity(intent);

            }
        });
    }
    public void TicketInfo()
    {
        for(int i =0; i< eList.size(); i++)
        {

            if(eList.get(i).get("eventname").equals(tile))
            {
                txtPrice.setText(eList.get(i).get("Ticketprice"));
                txtexpire.setText(eList.get(i).get("expiryDate"));
                txtClub.setText(eList.get(i).get("eventname"));
                txtTime.setText(eList.get(i).get("eventtime"));

                text2Qr =eList.get(i).get("eventname").toString();
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try

                {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text2Qr, BarcodeFormat.QR_CODE,100,100);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    image.setImageBitmap(bitmap);
                }
                catch (WriterException e){
                    e.printStackTrace();
                }
            }


        }
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




                        // tmp hash map for single event
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




