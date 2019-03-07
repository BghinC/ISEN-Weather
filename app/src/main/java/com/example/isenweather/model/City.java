package com.example.isenweather.model;

import com.example.isenweather.utils.ForecastInformation;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class City {

    @ColumnInfo
    @PrimaryKey
    @NonNull
    private String name;

    @ColumnInfo
    @NonNull
    private String dateTime;
    @ColumnInfo
    @NonNull
    private String temperature;
    @ColumnInfo
    @NonNull
    private String humidity;
    @ColumnInfo
    @NonNull
    private String pressure;
    @ColumnInfo
    @NonNull
    private String description;
    @ColumnInfo
    @NonNull
    private String image_name;

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    @NonNull
    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(@NonNull String dateTime) {
        this.dateTime = dateTime;
    }

    @NonNull
    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(@NonNull String temperature) {
        this.temperature = temperature;
    }

    @NonNull
    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(@NonNull String humidity) {
        this.humidity = humidity;
    }

    @NonNull
    public String getPressure() {
        return pressure;
    }

    public void setPressure(@NonNull String pressure) {
        this.pressure = pressure;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(@NonNull String image_name) {
        this.image_name = image_name;
    }
}
