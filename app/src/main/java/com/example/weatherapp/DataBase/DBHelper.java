package com.example.weatherapp.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "weather_app_db";
	 
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE_CITIES = "CREATE TABLE " +
            DBContract.Cities.TABLE_NAME + " (" + DBContract.Cities.ID_COLUMN_NAME +
            " ntext PRIMARY KEY, " + DBContract.Cities.NAME_COLUMN_NAME + " ntext, " +
            DBContract.Cities.REGION_COLUMN_NAME + " ntext, " +
            DBContract.Cities.COUNTRY_COLUMN_NAME + " ntext);";

    private static final String DATABASE_CREATE_WEATHER = "CREATE TABLE " +
            DBContract.Weather.TABLE_NAME + " (" + DBContract.Weather.ID_COLUMN_NAME +
            " ntext PRIMARY KEY, " + DBContract.Weather.WEATHER_COLUMN_NAME + " ntext, " +
            DBContract.Weather.OBSERVATION_DATETIME_COLUMN_NAME + " ntext, " +
            DBContract.Weather.METRIC_TEMP_COLUMN_NAME + " real, " +
            DBContract.Weather.IMPERIAL_TEMP_COLUMN_NAME + " real);";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE_CITIES);
        database.execSQL(DATABASE_CREATE_WEATHER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
    }
}
