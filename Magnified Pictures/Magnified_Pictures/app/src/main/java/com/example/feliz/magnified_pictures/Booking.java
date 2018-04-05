package com.example.feliz.magnified_pictures;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Feliz on 2018/01/31.
 */

public class Booking extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{


    private String date;
    private String time;
    private String place;
    private String amount;
    private String categories;
    private int start;
    private String name;
    private int id;
    private Calendar calendar;

    private int hour;
    private int min;

    private String duration,location,category;

    private EditText txtDate, txtTime;
    private Spinner txtLocation,txtCategory,txtDuration;
    private Button btnCreate;

    private int Eventday;
    private int Eventmonth;
    private int Eventyear;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_booking);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        calendar = Calendar.getInstance();
        txtDate = (EditText)findViewById(R.id.txtDate);
        txtTime = (EditText)findViewById(R.id.txtTime);
        txtDuration =(Spinner)findViewById(R.id.txtDuration);
        txtDuration.setPrompt("Select Event Duration");

        txtLocation= (Spinner)findViewById(R.id.txtLocation);
        txtLocation.setPrompt("Select City");

        txtCategory = (Spinner) findViewById(R.id.txtCategory);
        txtCategory.setPrompt("Select Category");

        btnCreate = (Button)findViewById(R.id.btnCreate);

        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Duration));
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtDuration.setAdapter(spinAdapter);

        ArrayAdapter<String> locationSpin = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.City));
        locationSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtLocation.setAdapter(locationSpin);

        ArrayAdapter<String> CategorySpin = new ArrayAdapter<String>(Booking.this,
                android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Category));
        CategorySpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        txtCategory.setAdapter(CategorySpin);


        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog pickerDialog = new DatePickerDialog(Booking.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int year, int month, int day)
                    {
                        Eventday = day;
                        Eventyear= year;
                        Eventmonth = month;
                        updateDisplay();

                    }
                },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                pickerDialog.setTitle("Select Event Date");
                pickerDialog.show();



            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Booking.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        min = selectedMinute;
                        updateTimeDisplay();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);//Yes 24 hour time
                mTimePicker.setTitle("Select Event Time");
                mTimePicker.show();

            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                Insert();
            }
        });

    }

public void Insert()

{


    StringRequest stringRequest = new StringRequest(Request.Method.POST,
            Constants.URL_BOOKINGS,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }) {
        @Override

        protected Map<String, String> getParams() throws AuthFailureError {

            String start;

            if(category.equals("Half Day"))
            {
                start ="0";
            }
            else
            {
                start ="1";
            }
            Map<String, String> params = new HashMap<>();
            params.put("bdate", date);
            params.put("btime", time);
            params.put("place",location);
            params.put("category",category);
            params.put("start", start);
           // params.put("admin",admin);
           // params.put("user", user);
           // params.put("category",category);
            return params;
        }
    };


    RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
}



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent intent = new Intent(getApplicationContext(),Browse.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(getApplicationContext(),Book.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {


            Intent intent = new Intent(getApplicationContext(),Rate.class);
            startActivity(intent);
        } else if(id==R.id.nav_home)
        {
            Intent intent = new Intent(getApplicationContext(),Home.class);
            startActivity(intent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateDisplay() {
        txtDate.setText(new StringBuilder().append(Eventyear).append("-").append(Eventmonth + 1).append("-").append(Eventday).append(""));
        date = txtDate.getText().toString();
    }
    private void updateTimeDisplay()
    {

        txtTime.setText(new StringBuilder().append(hour).append(":").append(min));
        time = txtTime.getText().toString();
    }
}
