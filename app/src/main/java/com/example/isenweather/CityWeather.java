package com.example.isenweather;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.isenweather.database.DatabaseHelper;
import com.example.isenweather.model.City;
import com.example.isenweather.ui.fragments.ForecastFragment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CityWeather extends Activity {

    final Executor executor = Executors.newSingleThreadExecutor();
    DatabaseHelper dbh = DatabaseHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_weather);

        final ImageButton imageButtonStar = findViewById(R.id.imageButtonStar);
        final City city = new City();
        city.setName(getIntent().getStringExtra("city_name").replaceFirst(".",(getIntent().getStringExtra("city_name").charAt(0)+"").toUpperCase()));

        //Change the star if the city is in the database (favourite)
        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(dbh.getWeatherDao().getCity(city.getName()) != null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageButtonStar.setImageResource(android.R.drawable.btn_star_big_on);
                        }
                    });
                }
                else{
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageButtonStar.setImageResource(android.R.drawable.btn_star_big_off);
                        }
                    });
                }
            }
        });

        AddRemoveFavourite(imageButtonStar, city);

        //Click on the back button finish the activity
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Bundle arguments = new Bundle();
        arguments.putString("city_name", city.getName());

        Fragment forecastFragment = new ForecastFragment();
        forecastFragment.setArguments(arguments);

        getFragmentManager()
                .beginTransaction()
                    .replace(R.id.containerFragment, forecastFragment)
                        .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    //Use to handle click on the favourite button (Add or Remove the city from favourite)
    private void AddRemoveFavourite(final ImageButton imageButtonStar, final City city){
        //When click on the favourite button
        imageButtonStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView text = (TextView) findViewById(R.id.textViewCityName);
                city.setName(text.getText().toString());

                text = (TextView) findViewById(R.id.textViewDateTime);
                city.setDateTime(text.getText().toString());

                text = (TextView) findViewById(R.id.textViewDescription);
                city.setDescription(text.getText().toString());

                text = (TextView) findViewById(R.id.textViewHumidity);
                city.setHumidity(text.getText().toString());

                ImageView im = (ImageView) findViewById(R.id.imageWeather);
                city.setImage_name(im.getTag().toString());

                text = (TextView) findViewById(R.id.textViewPressure);
                city.setPressure(text.getText().toString());

                text = (TextView) findViewById(R.id.textViewTemperature);
                city.setTemperature(text.getText().toString());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Insert or Delete the city from favourite
                        if(dbh.getWeatherDao().getCity(city.getName()) != null){
                            dbh.getWeatherDao().deleteCity(city.getName());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Change the favourite image
                                    imageButtonStar.setImageResource(android.R.drawable.btn_star_big_off);
                                    //Display a toast
                                    String toastMessage = city.getName() + " " + CityWeather.this.getResources().getString(R.string.remove_from_favourite);
                                    Toast.makeText(CityWeather.this, toastMessage, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else{
                            dbh.getWeatherDao().insert(city);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //Change the favourite image
                                    String toastMessage = city.getName() + " " + CityWeather.this.getResources().getString(R.string.add_to_favourite);
                                    //Display a toast
                                    Toast.makeText(CityWeather.this, toastMessage, Toast.LENGTH_SHORT).show();
                                    imageButtonStar.setImageResource(android.R.drawable.btn_star_big_on);
                                }
                            });
                        }
                    }
                });
                //imageButtonStar.setImageResource(android.R.drawable.btn_star_big_on);
            }
        });
    }


}
