package com.example.weatherapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Misc {
    public static final String KEY = "V9kV03P2qunwxnMgvFP8tBOWhNWaavc2";

    public static final String CITY_ID = "id";
    public static final String CITY_NAME = "city";

    public static final String GET_CURRENT_CONDITIONS = "http://dataservice.accuweather.com/currentconditions/v1/";
    public static final String SEARCH_CITIES = "http://dataservice.accuweather.com/locations/v1/cities/autocomplete";

    final public static SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    final public static SimpleDateFormat outDateFormat = new SimpleDateFormat("HH:mm:ss dd.MM");

    public static String dateToString(Date dt) {
        return outDateFormat.format(dt);
    }

    public static Date parseDate(String dt) throws ParseException {
        return isoDateFormat.parse(dt);
    }
}
