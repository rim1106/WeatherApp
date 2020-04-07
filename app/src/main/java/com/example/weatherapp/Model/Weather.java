package com.example.weatherapp.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class Weather {
    private String weather = "";
    private String observationDateTime = "";
    private double metricTemp = 0;
    private double imperialTemp = 0;

    public Weather() { }
    public Weather(JSONObject data) {
        try {
            this.weather = data.getString("WeatherText");
            this.observationDateTime = data.getString("LocalObservationDateTime");
            this.metricTemp = data.getJSONObject("Temperature").getJSONObject("Metric").getDouble("Value");
            this.imperialTemp = data.getJSONObject("Temperature").getJSONObject("Imperial").getDouble("Value");
        } catch (JSONException ex) {

        }
    }

    public double getMetricTemp() {
        return metricTemp;
    }

    public void setMetricTemp(double metricTemp) {
        this.metricTemp = metricTemp;
    }

    public double getImperialTemp() {
        return imperialTemp;
    }

    public void setImperialTemp(double imperialTemp) {
        this.imperialTemp = imperialTemp;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getObservationDateTime() {
        return observationDateTime;
    }

    public void setObservationDateTime(String observationDateTime) {
        this.observationDateTime = observationDateTime;
    }
}
