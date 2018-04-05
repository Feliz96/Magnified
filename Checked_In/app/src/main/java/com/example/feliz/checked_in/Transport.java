package com.example.feliz.checked_in;

/**
 * Created by Feliz on 2017/10/05.
 */

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Transport extends FragmentActivity implements LocationListener {

    private GoogleMap mMap;
    private SeekBar mSeekBar;
    private AutoCompleteTextView mPickUpAutoComplete, mDropAutoComplete;
    private View hatchBackView, suvView, sedanView;
    private Handler handler;
    private ArrayList<String> mAddresses = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    private RequestQueue queue;
    private Marker pickUpMarker, dropMarker;
    private NotificationHelper progressHelper;
    private Button nowButton, laterButton;
    private String currentCar = "Hatchback";
    private LatLng dropLatLng = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport);

        progressHelper = new NotificationHelper(this);

        hatchBackView = findViewById(R.id.hatchbackView);
        sedanView = findViewById(R.id.sedanView);
        suvView = findViewById(R.id.suvView);

        nowButton = (Button) findViewById(R.id.now);
        laterButton = (Button) findViewById(R.id.later);

        nowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transport.this, RideDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("pickUp", mPickUpAutoComplete.getText().toString());
                bundle.putString("car", currentCar);
                bundle.putString("drop", mDropAutoComplete.getText().toString());
                if (dropLatLng != null) {
                    bundle.putDouble("lat", dropLatLng.latitude);
                    bundle.putDouble("lon", dropLatLng.longitude);
                } else {
                    bundle.putDouble("lat", 0);
                    bundle.putDouble("lon", 0);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        laterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transport.this, RideDetails.class);
                Bundle bundle = new Bundle();
                bundle.putString("pickUp", mPickUpAutoComplete.getText().toString());
                bundle.putString("car", currentCar);
                bundle.putString("drop", mDropAutoComplete.getText().toString());
                if (dropLatLng != null) {
                    bundle.putDouble("lat", dropLatLng.latitude);
                    bundle.putDouble("lon", dropLatLng.longitude);
                } else {
                    bundle.putDouble("lat", 0);
                    bundle.putDouble("lon", 0);
                }
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        handler = new Handler();
        mSeekBar = (SeekBar) findViewById(R.id.car_type);
        mSeekBar.setMax(100);

        hatchBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(0);
                currentCar = "Hatchback";
            }
        });

        sedanView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(60);
                currentCar = "Sedan";
            }
        });

        suvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSeekBar.setProgress(100);
                currentCar = "SUV";
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress >= 0 && progress <= 30) {
                    currentCar = "Hatchback";
                    seekBar.setProgress(0);
                } else if (progress >= 30 && progress <= 70) {
                    currentCar = "Sedan";
                    seekBar.setProgress(60);
                } else {
                    currentCar = "SUV";
                    seekBar.setProgress(100);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mPickUpAutoComplete = (AutoCompleteTextView) findViewById(R.id.pickUpEdit);
        mDropAutoComplete = (AutoCompleteTextView) findViewById(R.id.dropEdit);


        mPickUpAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String place = ((TextView) view).getText().toString();
                progressHelper.showNotification("Adding " + place + " as a pick up point");
                onItemSelected(place, mPickUpAutoComplete);
            }
        });
        mDropAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String place = ((TextView) view).getText().toString();
                progressHelper.showNotification("Adding " + place + " as a drop point");
                onItemSelected(place, mDropAutoComplete);
            }
        });

        mPickUpAutoComplete.setThreshold(3);
        mDropAutoComplete.setThreshold(3);

        mPickUpAutoComplete.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                handler.removeCallbacks(null);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchForPlaces(s, mPickUpAutoComplete);
                    }
                }, 1000);

            }
        });

        mDropAutoComplete.addTextChangedListener(new AbstractTextWatcher() {
            @Override
            public void afterTextChanged(final Editable s) {
                handler.removeCallbacks(null);
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchForPlaces(s, mDropAutoComplete);
                    }
                }, 1000);
            }
        });

        setUpMapIfNeeded();

        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            boolean check = checkLocationPermission();
            //if(check==true)
            if (mMap != null && check==true) {
                mMap.setMyLocationEnabled(true);
                mMap.setTrafficEnabled(true);
                mMap.setBuildingsEnabled(true);
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                String provider = locationManager.getBestProvider(criteria, true);
               Location location = locationManager.getLastKnownLocation(provider);
                if (location != null) {
                    onLocationChanged(location);
                }
                locationManager.requestLocationUpdates(provider, 120000, 0, this);

            }
        }
    }

    public void closeSoftKeyBoardAlways() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow((null == getCurrentFocus()) ? null : getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void onItemSelected(String place, AutoCompleteTextView view) {
        closeSoftKeyBoardAlways();
        view.clearListSelection();
        view.dismissDropDown();

        Log.d("Place", place);
        view.clearFocus();
        Geocoder mGeoCoder = new Geocoder(this);
        List<Address> addresses = null;
        try {
            addresses = mGeoCoder.getFromLocationName(place, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (addresses != null) {
            Address address = addresses.get(0);
            LatLng latlng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

            if (view.getId() == R.id.pickUpEdit) {
                if (pickUpMarker != null)
                    pickUpMarker.remove();
                pickUpMarker = mMap.addMarker(new MarkerOptions().title("Pickup - " + place).position(latlng));
            } else {
                if (dropMarker != null)
                    dropMarker.remove();
                dropMarker = mMap.addMarker(new MarkerOptions().title("Drop - " + place).position(latlng));
                dropLatLng = latlng;
            }
        }
        progressHelper.cancelNotification();

    }

    /**
     * Search Google Places and set autocomplete in Pickup and Drop fields.
     *
     *
     */
    private void searchForPlaces(Editable s, final AutoCompleteTextView view) {
        progressHelper.showNotification("Searching for " + s.toString());
        String inputQuery = s.toString();
        if (inputQuery.isEmpty()) {
            return;
        }
        StringBuilder urlBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=")
                .append(Uri.encode(inputQuery))
                .append("&key=" + Secrets.PLACES_API_KEY)
                .append("&location=")
                .append(mMap.getMyLocation().getLatitude() + "," + mMap.getMyLocation().getLongitude());

        String url = new String(urlBuilder);
        Log.d(getClass().getSimpleName(), url);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener() {

                    @Override
                    public void onResponse(Object res) {
                        try {
                            JSONObject responsePlaces = new JSONObject((String) res);
                            JSONArray predictionsArray = responsePlaces.getJSONArray("predictions");
                            mAddresses.clear();
                            for (int i = 0; i < predictionsArray.length(); i++) {
                                JSONObject predictionObject = predictionsArray.getJSONObject(i);
                                String description = predictionObject.getString("description");
                                mAddresses.add(description);
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(Transport.this, android.R.layout.simple_list_item_1, mAddresses);
                            view.setAdapter(adapter);
                            view.showDropDown();
                            Log.d(getClass().getSimpleName(), mAddresses.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        progressHelper.cancelNotification();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null)
                    Log.d("Error Response", error.getMessage());
                progressHelper.cancelNotification();
            }
        }
        );
        queue.add(stringRequest);
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        LatLng latLng = new LatLng(latitude, longitude);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /**
     * Used for above TextWatcher implementations. Need not override each method always.
     */
    private class AbstractTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public boolean checkLocationPermission(){


//In Android 6.0 and higher you need to request permissions at runtime, and the user has the ability to grant or deny each permission. Users can also revoke a previously-granted permission at any time, so your app must always check that it has access to each permission, before trying to perform actions that require that permission. Here, we’re using ContextCompat.checkSelfPermission to check whether this app currently has the ACCESS_COARSE_LOCATION permission//


        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION)


//If your app does have access to COARSE_LOCATION, then this method will return PackageManager.PERMISSION_GRANTED//


                != PackageManager.PERMISSION_GRANTED) {




            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION)) {


                // If your app doesn’t have this permission, then you’ll need to request it by calling the ActivityCompat.requestPermissions method//


                // requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);


            } else {

//Request the permission by launching Android’s standard permissions dialog. If you want to provide any additional information, such as why your app requires this particular permission, then you’ll need to add this information before calling requestPermission//


                //requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
