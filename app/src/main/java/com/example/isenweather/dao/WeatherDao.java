package com.example.isenweather.dao;


import com.example.isenweather.model.City;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface WeatherDao {
    @Query("SELECT * FROM City")
    List<City> getAll();

    @Query("SELECT name FROM City")
    List<String> getNames();

    @Query("SELECT * FROM City WHERE name = :city_name")
    City getCity(String city_name);

    @Query("DELETE FROM City")
    void deleteAll();

    @Query("DELETE FROM City WHERE name = :city_name")
    void deleteCity(String city_name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<City> cities);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(City city);

    @Delete
    void delete(City city);
}
