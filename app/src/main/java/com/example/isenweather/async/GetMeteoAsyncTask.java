package com.example.isenweather.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.isenweather.weatherAPI.WeatherAPI;

public class GetMeteoAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        if((null != params) && (params.length == 2)){
            final String city = params[0];
            final String weatherType = params[1];
            try {
                return WeatherAPI.getWeather(city, weatherType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    @Override
    protected void onPostExecute(String str) {
        Log.i("Debug", "OnPostExcecute: " + str);
    }
}