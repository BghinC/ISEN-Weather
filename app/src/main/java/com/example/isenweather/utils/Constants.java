package com.example.isenweather.utils;

public class Constants {

    public class Weather {
        //http://api.openweathermap.org/data/2.5/forecast?&units=metric&q=Lille&APPID=YOUR_API_KEY
        public static final String API_KEY = "APPID=YOUR_API_KEY";
        public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";
        public static final String CURRENT_METEO = "weather?";
        public static final String FORECAST_METEO = "forecast?";
        public static final String URL_PARAMETER_CITY = "&units=metric&q=";
        public static final String URL_PARAMETER_LATITUTE = "&lat=";
        public static final String URL_PARAMETER_LONGITUDE = "&lon=";


        public static final String WEATHER_PERIOD_CURRENT = "Current";
        public static final String WEATHER_PERIOD_TOMORROW = "Tomorrow";
        public static final String WEATHER_PERIOD_AFTER_TOMORROW = "After Tomorrow";

        public static final String ERROR = "ERROR";

    }

    public class Information{
        public static final String IMAGENAME = "IMAGENAME";
        public static final String TEMPERATURE = "TEMPERATURE";
        public static final String HUMIDITY = "HUMIDITY";
        public static final String PRESSURE = "PRESSURE";
        public static final String CLOUDINESS = "CLOUDINESS";
        public static final String WIND = "WIND";
        public static final String DATETIME = "DATETIME";
        public static final String DESCRIPTION = "DESCRIPTION";
    }
}
