package com.example.isenweather.database;

import com.example.isenweather.dao.WeatherDao;
import com.example.isenweather.model.City;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {City.class}, version = 1, exportSchema = false)
public abstract class WeatherDatabase extends RoomDatabase {
    public abstract WeatherDao weatherDao();
}
