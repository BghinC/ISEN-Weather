package com.example.isenweather.utils;

public class ForecastInformation {

    private String city_name;
    private String dateTime;
    private String temperature;
    private String humidity;
    private String pressure;
    private String cloudiness;
    private String wind;
    private String description;
    private String image_name;

    public ForecastInformation(String city_name, String dateTime, String temperature, String humidity, String pressure, String cloudiness, String wind, String description, String image_name) {
        this.city_name = city_name;
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.cloudiness = cloudiness;
        this.wind = wind;
        this.description = description;
        this.image_name = image_name;
    }

    public ForecastInformation(String dateTime, String temperature, String humidity, String pressure, String cloudiness, String wind, String description, String image_name) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.cloudiness = cloudiness;
        this.wind = wind;
        this.description = description;
        this.image_name = image_name;
    }

    public ForecastInformation(String city_name,String dateTime, String temperature, String humidity, String pressure, String description, String image_name) {
        this.city_name = city_name;
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.description = description;
        this.image_name = image_name;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String weather) {
        this.image_name = weather;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCloudiness() {
        return cloudiness;
    }

    public void setCloudiness(String cloudiness) {
        this.cloudiness = cloudiness;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }
}
