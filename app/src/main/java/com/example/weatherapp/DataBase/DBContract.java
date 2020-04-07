package com.example.weatherapp.DataBase;

import android.provider.BaseColumns;

public final class DBContract {

    public DBContract() {}

    public static abstract class Cities implements BaseColumns {
        public static final String TABLE_NAME = "Cities";
        public static final String ID_COLUMN_NAME = "id";
        public static final String NAME_COLUMN_NAME = "name";
        public static final String REGION_COLUMN_NAME = "region";
        public static final String COUNTRY_COLUMN_NAME = "country";
    }

    public static abstract class Weather implements BaseColumns {
        public static final String TABLE_NAME = "Weather";
        public static final String ID_COLUMN_NAME = "id";
        public static final String WEATHER_COLUMN_NAME = "weather";
        public static final String OBSERVATION_DATETIME_COLUMN_NAME = "observationdt";
        public static final String METRIC_TEMP_COLUMN_NAME = "metrictemprature";
        public static final String IMPERIAL_TEMP_COLUMN_NAME = "imperialtemprature";
    }
}