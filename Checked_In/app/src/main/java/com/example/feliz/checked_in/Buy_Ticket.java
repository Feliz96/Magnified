package com.example.feliz.checked_in;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Feliz on 2017/10/18.
 */

public class Buy_Ticket extends AppCompatActivity
{
    private String TAG = Buy_Ticket.class.getSimpleName();
    String tile;
    JSONArray arr;
    private Button buy,rsvp;
    private TextView txtDate,txtTime,txtLocation, txtDescription,txtDuration, txtTitle,txtNumber;
    public ArrayList<HashMap<String, String>> eList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rsvp_buy);

        buy = (Button) findViewById(R.id.btnBuy);
        rsvp = (Button) findViewById(R.id.btnRSVP);

        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDuration = (TextView) findViewById(R.id.txtDuration);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtNumber = (TextView)findViewById(R.id.txtRSVP);


      final  ArrayList<Object> object = (ArrayList<Object>) getIntent().getSerializableExtra("arraylist");
        arr = new JSONArray(object);

        for(int i =0;i<arr.length();i++){
            tile = getIntent().getExtras().getString("title");

            try {

                if(arr.getJSONObject(i).getString("eventname").equals(tile))
                {

                    // txtevent.setText(arr.getJSONObject(i).get("Name").toString());
                    txtTitle.setText(arr.getJSONObject(i).getString("eventname"));
                    txtDescription.setText(arr.getJSONObject(i).getString("eventdescription"));
                    txtDate.setText(arr.getJSONObject(i).getString("eventdate"));
                    txtTime.setText("16:00");
                    txtDuration.setText("2 Hours");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //eList = new ArrayList<>(); //inst


        }
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Buy.class);
                intent.putExtra("title",tile);
                intent.putExtra("arraylist",object);
                startActivity(intent);
            }
        });

        rsvp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String num = txtNumber.getText().toString();
                int number = Integer.parseInt(num);
                int result = number +1;
                String res = String.valueOf(result);
                txtNumber.setText(res);
                rsvp.setVisibility(View.GONE);
            }
        });

    }



}



