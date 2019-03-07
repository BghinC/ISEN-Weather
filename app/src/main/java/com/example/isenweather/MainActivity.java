package com.example.isenweather;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.isenweather.database.DatabaseHelper;
import com.example.isenweather.ui.fragments.FavouriteFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends Activity {

    final Executor executor = Executors.newSingleThreadExecutor();
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    LocationManager mLocationManager;
    Location myLocation;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ImageButton buttonGeoloc;
    private ProgressBar progressBarGeoloc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton changeActivity = findViewById(R.id.goToCityActivity);
        changeActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText city_name = findViewById(R.id.plainTextCityName);
                if (!TextUtils.isEmpty(city_name.getText())) {
                    goToCityWeatherActivity(city_name.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this, "Enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });
        buttonGeoloc = findViewById(R.id.imageButtonGeolocalisation);
        progressBarGeoloc = findViewById(R.id.progressBarGeoloc);
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        buttonGeoloc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonLookingForLocation();
                myLocation = getLastKnownLocation();

                if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                        && !mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                        && myLocation == null ) {
                    Toast.makeText(MainActivity.this, "GPS not enabled", Toast.LENGTH_SHORT).show();
                    changeButtonNotLookingForLocation();
                    startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);;
                }else if(myLocation == null){
                    Toast.makeText(MainActivity.this, "Position unknown", Toast.LENGTH_SHORT).show();
                    changeButtonNotLookingForLocation();
                }else{

                    Log.i("GPS", Double.toString(myLocation.getLongitude()) + " // " + myLocation.getLatitude());

                    Geocoder geoCoder = new Geocoder(MainActivity.this, Locale.getDefault());

                    try {
                        List<Address> address = geoCoder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 1);
                        if (address.size() > 0) {
                            String city_name = address.get(0).getLocality();
                            Log.i("GPS", city_name);
                            goToCityWeatherActivity(city_name);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        changeButtonNotLookingForLocation();

        //this.overridePendingTransition(android.R.anim.slide_out_right, android.R.anim.slide_in_left);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.containerFragmentFavourite, new FavouriteFragment())
                .commit();
    }

    public void goToCityWeatherActivity(String city_name) {
        Intent goToCityWeather = new Intent(MainActivity.this, CityWeather.class);
        goToCityWeather.putExtra("city_name", city_name);
        //Start new activity
        startActivity(goToCityWeather);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    private Location getLastKnownLocation() {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("GPS not enabled")
                        .setMessage("Autorize ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }

        }

        for (String provider : providers) {

            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;

    }

    private void changeButtonLookingForLocation(){
        buttonGeoloc.setVisibility(View.INVISIBLE);
        progressBarGeoloc.setVisibility(View.VISIBLE);
    }

    private void changeButtonNotLookingForLocation(){
        buttonGeoloc.setVisibility(View.VISIBLE);
        progressBarGeoloc.setVisibility(View.INVISIBLE);
    }

}