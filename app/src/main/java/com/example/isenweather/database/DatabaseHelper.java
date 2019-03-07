package com.example.isenweather.database;


import com.example.isenweather.WeatherApplication;
import com.example.isenweather.dao.WeatherDao;

import androidx.room.Room;

public class DatabaseHelper {
    static DatabaseHelper instance = null;
    private final WeatherDatabase db;

    public static DatabaseHelper getInstance(){
        if(instance == null){
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public WeatherDao getWeatherDao(){
        return db.weatherDao();
    }

    public DatabaseHelper(){
        db = Room.databaseBuilder(
                WeatherApplication.getContext(),
                WeatherDatabase.class, "ma_bdd.db"
        ).build();
    }
}