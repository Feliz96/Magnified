package com.example.feliz.checked_in;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener,NavigationView.OnNavigationItemSelectedListener
{
public static final int MY_PERMISSIONS_REQUEST_LOCATION = 1;
    private static final int REQUEST_LOCATION = 1;
    private String TAG = MapsActivity.class.getSimpleName();
        LocationRequest mLocationRequest;
        GoogleApiClient mGoogleApiClient;
        LatLng latLng;
        GoogleMap mGoogleMap;
        SupportMapFragment mFragment;
        Marker currLocationMarker;
        private DrawerLayout drawerLayout;
        private ActionBarDrawerToggle Toggle;
        LocationManager locationManager;
        String lattitude,longitude;
    public ArrayList<HashMap<String, String>> getList() {
        return eList;
    }

    public void setList(ArrayList<HashMap<String, String>> eList) {
        this.eList = eList;
    }

    public ArrayList<HashMap<String, String>> eList;
    public TextView notification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        eList = new ArrayList<>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        notification=(TextView)MenuItemCompat.getActionView(navigationView.getMenu().findItem(R.id.nav_tickets));


        mFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mFragment.getMapAsync(this);



        new JSONTask().execute();

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
                    for (int i = 0; i < events.length(); i++) {
                        JSONObject c = events.getJSONObject(i);
                        String longitude = c.getString("longitude");
                        String latitude = c.getString("latitude");
                        String Name = c.getString("Name");
                        String address = c.getString("address");
                        String description = c.getString("description");
                        String Time = c.getString("Time");
                        String date = c.getString("date");
                        String eventname = c.getString("name");
                        // tmp hash map for single contact
                        HashMap<String, String> eve = new HashMap<>();

                        // adding each child node to HashMap key => value
                        eve.put("longitude", longitude);
                        eve.put("latitude", latitude);
                        eve.put("Name",Name);
                        eve.put("address",address);
                        eve.put("description",description);
                        eve.put("Time",Time);
                        eve.put("date",date);
                        eve.put("name",eventname);
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


    public void initializeCountDrawer(){
        notification.setGravity(Gravity.CENTER_VERTICAL);
        notification.setTypeface(null, Typeface.BOLD);
        notification.setTextColor(getResources().getColor(R.color.colorAccent));
        notification.setText("99+");
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
        getMenuInflater().inflate(R.menu.maps, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(GoogleMap gMap) {

        mGoogleMap = gMap;

      //  Toast.makeText(this,eList.size(),Toast.LENGTH_LONG).show();
        gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        gMap.setOnInfoWindowClickListener(
                new GoogleMap.OnInfoWindowClickListener(){
                    public void onInfoWindowClick(Marker marker){

                        Intent intent = new Intent(getApplicationContext(), Event_Profile.class);
                        intent.putExtra("title",marker.getTitle().toString());
                        intent.putExtra("arraylist",eList);

                        startActivity(intent);

                    }
                }
        );

        Boolean check =checkLocationPermission();
        if(check==true)
        {
            mGoogleMap.setMyLocationEnabled(true);

            buildGoogleApiClient();

            mGoogleApiClient.connect();
        }
        else
        {
            Toast.makeText(this,"Please enable location",Toast.LENGTH_SHORT).show();
        }


        //displaying event location
        for (int i =0; i<eList.size();i++)
        {

            double lat = Double.parseDouble(eList.get(i).get("latitude"));
            double lng = Double.parseDouble(eList.get(i).get("longitude"));
            LatLng luy = new LatLng(lat,lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(luy);
            markerOptions.title(eList.get(i).get("Name")).snippet(eList.get(i).get("address"));
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.home));
            mGoogleMap.addMarker(markerOptions);
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(luy,15));
         }


    }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    public void onConnected(Bundle bundle)
    {
     //   Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
        //boolean check = checkLocationPermission();
        //if(check==true)
       // {
         //   Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
           //         mGoogleApiClient);
            //if (mLastLocation != null) {
                //place marker at current position
                //mGoogleMap.clear();
              //  latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
               // MarkerOptions markerOptions = new MarkerOptions();
                //markerOptions.position(latLng);
                //markerOptions.title("Current Position");
                //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                //currLocationMarker = mGoogleMap.addMarker(markerOptions);


            //}

            //mLocationRequest = new LocationRequest();
            //mLocationRequest.setInterval(5000); //5 seconds
            //mLocationRequest.setFastestInterval(3000); //3 seconds
            //mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

       // }

    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {
        Intent intent = new Intent(getApplicationContext(), CreateEvent.class);
            startActivity(intent);

        } else if (id == R.id.nav_tickets) {

            Intent intent = new Intent(getApplicationContext(), Notification.class);
            startActivity(intent);


        } else if (id == R.id.nav_slideshow) {

            Intent intent = new Intent(getApplicationContext(), FeedsActivity.class);
            startActivity(intent);

        }
        else if (id ==R.id.home)
        {
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
        }
        else if(id ==R.id.nav_send)
        {
           Intent intent = new Intent(getApplicationContext(), Category.class);
            startActivity(intent);
        }
        else if (id==R.id.nav_logout)
        {
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }
        else if(id==R.id.nav_transport)
        {
            Intent intent = new Intent(getApplicationContext(),Transport.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_scan)
        {
            Intent intent = new Intent(getApplicationContext(),ReaderActivity.class);
            startActivity(intent);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    public void onLocationChanged(Location location) {
        mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currLocationMarker = mGoogleMap.addMarker(markerOptions);

        Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //zoom to current position:
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));

        //If you only need one location, unregister the listener
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);



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
