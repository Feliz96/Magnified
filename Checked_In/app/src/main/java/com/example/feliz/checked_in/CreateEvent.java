package com.example.feliz.checked_in;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.io.IOException;
import java.sql.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.pm.PackageManager;
import android.icu.util.TimeZone;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.feliz.checked_in.R.mipmap.calendar;




public class CreateEvent  extends Activity implements View.OnClickListener,DatePickerDialog.OnDateSetListener,View.OnFocusChangeListener,TimePickerDialog.OnTimeSetListener {
    private EditText txtDate;
    private EditText txtTime,txtAddress,txtDescription,txtName;
    private int Eventday;
    private int Eventmonth;
    private int Eventyear;
    private Context context;
    private Calendar calendar;
    private String description;
    private int hour;
    private int min;
    private double lat,lng;
    private Button Create;
    String add;

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    double lattitude;
    String time;
    String date;
    String name;


    final String category ="1";
    final String cover ="1-000_111_2928.jpg";
    final String header ="Event created by user";
    final String rsvp ="0";
    final String admin ="0";
    final String type="public";
    final String user ="18";
    final String club ="9";

    private ProgressDialog progressDialog;

    public double getLatitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        calendar = Calendar.getInstance();
        txtDate = (EditText)findViewById(R.id.txtDate);
        txtTime = (EditText)findViewById(R.id.txtTime);
        txtAddress = (EditText)findViewById(R.id.txtLocation);
        txtDescription = (EditText)findViewById(R.id.txtDescription);
        txtName = (EditText)findViewById(R.id.txtName);

        Create = (Button)findViewById(R.id.btnCreate);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        add = getCompleteAddressString(lattitude,longitude);
        txtAddress.append(add);
        Spinner spinner =(Spinner)findViewById(R.id.txtDuration);
        spinner.setPrompt("Select Event Duration");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CreateEvent.this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.Duration));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setPrompt("Select Duration");
        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog pickerDialog = new DatePickerDialog(CreateEvent.this, new DatePickerDialog.OnDateSetListener() {
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
                mTimePicker = new TimePickerDialog(CreateEvent.this, new TimePickerDialog.OnTimeSetListener() {
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

  Create.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v)
      {
          description = txtDescription.getText().toString();
          name = txtName.getText().toString();

        Event();
          Club();
      }
  });


    }
public void Event()
{


    StringRequest stringRequest = new StringRequest(Request.Method.POST,
            Constants.URL_CreateEvent,
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
            Map<String, String> params = new HashMap<>();
            params.put("club", club);
            params.put("name", name);
            params.put("cover", cover);
            params.put("header", header);
            params.put("description", description);
            params.put("date", date);
            params.put("Time",time);
            params.put("type",type);
            params.put("RSVP", rsvp);
            params.put("admin",admin);
            params.put("user", user);
            params.put("category",category);
            params.get("club");
            return params;
        }
    };


    RequestHandler.getInstance(this).addToRequestQueue(stringRequest);

}

public void Club()
{
    StringRequest stringRequest = new StringRequest(Request.Method.POST,
            Constants.URL_CreateClub,
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
            Map<String, String> params = new HashMap<>();
            String lng = String.valueOf(longitude);
            String lat = String.valueOf(lattitude);
            params.put("user", user);
            params.put("Name", name);
            params.put("cover",cover);
            params.put("address", add);
            params.put("latitude", lat);
            params.put("longitude", lng);
            return params;
        }
    };


    RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
}

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Eventday = dayOfMonth;
        Eventyear = year;
        Eventmonth = month;
        updateDisplay();

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

    private void updateAddress()
    {
        txtAddress.setText(add);
    }

    @Override
    public void onClick(View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog pickerDialog = new DatePickerDialog(context, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus)
    {

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        hour = hourOfDay;
        min = minute;
        updateTimeDisplay();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {
             ;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return strAdd;
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
                lattitude = location.getLatitude();
                longitude = location.getLongitude();


            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }
}
