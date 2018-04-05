package com.example.feliz.magnified_pictures;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by Feliz on 2018/01/06.
 */
public class Rate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{

    private Button okay,good,great;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        okay = (Button) findViewById(R.id.btnOkay);
        good = (Button) findViewById(R.id.btnGood);
        great = (Button)findViewById(R.id.btnGreat);



        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Photographer rating = OKAY",Toast.LENGTH_LONG).show();
                okay.setVisibility(View.GONE);
                good.setVisibility(View.GONE);
                great.setVisibility(View.GONE);
            }
        });
        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Photographer rating = GOOD",Toast.LENGTH_LONG).show();
                okay.setVisibility(View.GONE);
                good.setVisibility(View.GONE);
                great.setVisibility(View.GONE);
            }
        });

        great.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Photographer rating = GREAT",Toast.LENGTH_LONG).show();
                okay.setVisibility(View.GONE);
                good.setVisibility(View.GONE);
                great.setVisibility(View.GONE);
            }
        });
    }


    public void Thank_you()
    {

        Toast.makeText(getApplicationContext(),"Thank you for using our app",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(),Book.class);
        startActivity(intent);
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
}
