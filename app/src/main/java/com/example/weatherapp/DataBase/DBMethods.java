package com.example.weatherapp.DataBase;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.weatherapp.Model.City;
import com.example.weatherapp.Model.Weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBMethods {
    public static void saveCityToDB(JSONObject city, SQLiteDatabase db) {
        try {
            ContentValues cv = new ContentValues();
            cv.put(DBContract.Cities.ID_COLUMN_NAME, city.getString("Key"));
            cv.put(DBContract.Cities.NAME_COLUMN_NAME, city.getString("LocalizedName"));
            cv.put(DBContract.Cities.COUNTRY_COLUMN_NAME, city.getJSONObject("Country").getString("LocalizedName"));
            cv.put(DBContract.Cities.REGION_COLUMN_NAME, city.getJSONObject("AdministrativeArea").getString("LocalizedName"));

            db.insertWithOnConflict(DBContract.Cities.TABLE_NAME, "", cv, SQLiteDatabase.CONFLICT_REPLACE);
        } catch (JSONException e) {

        }
    }

    public static void saveCityToDB(City city, SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(DBContract.Cities.ID_COLUMN_NAME, city.getId());
        cv.put(DBContract.Cities.NAME_COLUMN_NAME, city.getName());
        cv.put(DBContract.Cities.COUNTRY_COLUMN_NAME, city.getCountry());
        cv.put(DBContract.Cities.REGION_COLUMN_NAME, city.getRegion());

        db.insertWithOnConflict(DBContract.Cities.TABLE_NAME, "", cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static List<City> searchCityInDB(String s, SQLiteDatabase db) {
        List<City> result = new ArrayList<City>();
        String selection = DBContract.Cities.NAME_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {s+"%"};

        Cursor cursor = db.query(DBContract.Cities.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            City city;
            do {
                city = new City();
                city.setId(cursor.getString(cursor.getColumnIndex(DBContract.Cities.ID_COLUMN_NAME)));
                city.setCountry(cursor.getString(cursor.getColumnIndex(DBContract.Cities.COUNTRY_COLUMN_NAME)));
                city.setName(cursor.getString(cursor.getColumnIndex(DBContract.Cities.NAME_COLUMN_NAME)));
                city.setRegion(cursor.getString(cursor.getColumnIndex(DBContract.Cities.REGION_COLUMN_NAME)));
                result.add(city);
            } while (cursor.moveToNext());
        }
        return result;
    }

    public static void saveWeatherToDB(String id, Weather data, SQLiteDatabase db) {
            ContentValues cv = new ContentValues();
            cv.put(DBContract.Weather.ID_COLUMN_NAME, id);
            cv.put(DBContract.Weather.WEATHER_COLUMN_NAME, data.getWeather());
            cv.put(DBContract.Weather.OBSERVATION_DATETIME_COLUMN_NAME, data.getObservationDateTime());
            cv.put(DBContract.Weather.IMPERIAL_TEMP_COLUMN_NAME, data.getImperialTemp());
            cv.put(DBContract.Weather.METRIC_TEMP_COLUMN_NAME, data.getMetricTemp());

            db.insertWithOnConflict(DBContract.Weather.TABLE_NAME, "", cv, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public static Weather getWeatherFromDB(String id, SQLiteDatabase db) {

        String selection = DBContract.Weather.ID_COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = {id};

        Cursor cursor = db.query(DBContract.Weather.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            Weather result = new Weather();
            result.setWeather(cursor.getString(cursor.getColumnIndex(DBContract.Weather.WEATHER_COLUMN_NAME)));
            result.setMetricTemp(cursor.getDouble(cursor.getColumnIndex(DBContract.Weather.METRIC_TEMP_COLUMN_NAME)));
            result.setImperialTemp(cursor.getDouble(cursor.getColumnIndex(DBContract.Weather.IMPERIAL_TEMP_COLUMN_NAME)));
            result.setObservationDateTime(cursor.getString(cursor.getColumnIndex(DBContract.Weather.OBSERVATION_DATETIME_COLUMN_NAME)));
            return result;
        } else {
            return null;
        }
    }
}
