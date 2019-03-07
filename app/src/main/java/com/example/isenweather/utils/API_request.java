package com.example.isenweather.utils;

import android.os.Debug;
import android.util.Log;

import com.example.isenweather.async.GetMeteoAsyncTask;
import com.example.isenweather.model.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class API_request {

    private List<ForecastInformation> forecastInformationList;
    private String cityName;

    public List<ForecastInformation> get(City city){
        forecastInformationList = new ArrayList<ForecastInformation>();
        try {
            cityName = city.getName();
            String apiResult = new GetMeteoAsyncTask().execute(city.getName(), Constants.Weather.FORECAST_METEO).get();
            Log.i("Debug","apiResult: " + apiResult);


            Log.i("Debug","CLEMENT");
            if(apiResult.equals("notFound")){
                ForecastInformation infoWeather = new ForecastInformation("","","","","","","404","");
                forecastInformationList.add(infoWeather);
                return forecastInformationList;
            }else if(apiResult.equals("")){
                return null;
            }else{
                JSONObject jsonObject = new JSONObject(apiResult);
                displayForecastWeather(jsonObject);
            }


        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return forecastInformationList;
    }

    public List<ForecastInformation> getFavourite(City city){
        forecastInformationList = new ArrayList<ForecastInformation>();
        try {
            String apiResult = new GetMeteoAsyncTask().execute(city.getName(), Constants.Weather.FORECAST_METEO).get();
            JSONObject jsonObject = new JSONObject(apiResult);
            if(jsonObject.getString("cod").equals("404")){
                forecastInformationList.add(new ForecastInformation("","","","","","404",""));
                return forecastInformationList;
            }else if(jsonObject.getString("cod").equals("200")){
                displayForecastWeatherFavourite(jsonObject);
            }else{
                return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return forecastInformationList;
    }

    private void displayForecastWeather(JSONObject jsonObject){
        try {
            JSONObject jsonObject1 = jsonObject.getJSONObject("city");

            JSONArray forecastList = jsonObject.getJSONArray("list");
            jsonObject1 = forecastList.getJSONObject(0);
            setForecastInformation(jsonObject1);

            /*
            To display only Today, tomorrow and after tomorrow

            int i = 5;
            boolean find = false;
            while(i<forecastList.length() && !find) {
                jsonObject1 = forecastList.getJSONObject(i);
                if(getHour(jsonObject1.getInt("dt")) == 12) {
                    setForecastInformation(jsonObject1, Constants.Weather.WEATHER_PERIOD_TOMORROW);
                    i+=8;
                    jsonObject1 = forecastList.getJSONObject(i);
                    setForecastInformation(jsonObject1, Constants.Weather.WEATHER_PERIOD_AFTER_TOMORROW);
                    find = true;
                }
                i++;
            }
            */
            int i = 0;
            while(i<forecastList.length()) {
                jsonObject1 = forecastList.getJSONObject(i);
                setForecastInformation(jsonObject1);
                i++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayForecastWeatherFavourite(JSONObject jsonObject){
        try {
            JSONObject jsonObject1 = jsonObject.getJSONObject("city");
            JSONArray forecastList = jsonObject.getJSONArray("list");
            jsonObject1 = forecastList.getJSONObject(0);
            setForecastInformation(jsonObject1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getHour(int time) {
        return (time/3600)%24;
    }

    private void setForecastInformation(JSONObject object) {
        try {
            ForecastInformation infoWeather;

            JSONObject jsonWeather = object.getJSONArray("weather").getJSONObject(0);
            JSONObject jsonObject = object.getJSONObject("main");
            infoWeather = new ForecastInformation(cityName, object.getString("dt_txt"),jsonObject.getString("temp")+"°C",jsonObject.getString("humidity")+"%",
                    jsonObject.getString("pressure")+" hPa",object.getJSONObject("clouds").getString("all")+"%",object.getJSONObject("wind").getString("speed")+" km/h",jsonWeather.getString("description").toUpperCase(),jsonWeather.getString("icon"));
            forecastInformationList.add(infoWeather);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
    }
}