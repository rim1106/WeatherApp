package com.example.weatherapp.Interfaces;

import org.json.JSONObject;

public interface ResponseListener {
    public void onSuccess(JSONObject data);
    public void onError(String reason);
}
