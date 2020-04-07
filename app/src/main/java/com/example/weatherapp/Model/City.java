package com.example.weatherapp.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class City {
    private String id = "";
    private String name = "";
    private String country = "";
    private String region = "";

    public City() { }
    public City(JSONObject city) {
        try {
            this.id = city.getString("Key");
            this.name = city.getString("LocalizedName");
            this.country = city.getJSONObject("Country").getString("LocalizedName");
            this.region = city.getJSONObject("AdministrativeArea").getString("LocalizedName");
        } catch (JSONException ex) {

        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
