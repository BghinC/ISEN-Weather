package com.example.isenweather;

import android.content.Context;
import android.app.Application;

public class WeatherApplication extends Application{
    public static Context getContext() {
        return context;
    }

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
