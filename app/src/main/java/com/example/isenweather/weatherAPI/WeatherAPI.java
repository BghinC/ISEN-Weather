package com.example.isenweather.weatherAPI;

import android.util.Log;

import com.example.isenweather.utils.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherAPI {


    public static String getWeather(String city_name, String weatherType) throws IOException {
        Log.i("Debug","start SEND_GET");
        String url = Constants.Weather.BASE_URL + weatherType + Constants.Weather.API_KEY + Constants.Weather.URL_PARAMETER_CITY + city_name;
        URL obj = new URL(url);
        HttpURLConnection con = getHttpConnection(obj);
        if (con == null) return "";
        try {
            int responseCode = con.getResponseCode();
            Log.i("Debug","Response Code: " + Integer.toString(responseCode));
            if (responseCode == HttpURLConnection.HTTP_OK) { // success
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                //Log.i("Debug",response.toString());
                return response.toString();
            } else if( responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return "notFound";
            }else{
                Log.i("Debug", "GET request not worked");
            }
        } catch (Exception e){
            Log.i("Debug",e.toString());
            e.printStackTrace();
        }
        return "";
    }

    private static HttpURLConnection getHttpConnection (URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            Log.i("Debug","OPEN CONNEXION");
            connection.setRequestMethod("GET");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            connection.setRequestProperty("Content-Type", "application/json");
        } catch (IOException e) {
            Log.i("Debug", "OPENNING CONNEXION failed !");
            e.printStackTrace();
        }
        return connection;
    }
}