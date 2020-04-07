package com.example.weatherapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.weatherapp.DataBase.DBHelper;
import com.example.weatherapp.DataBase.DBMethods;
import com.example.weatherapp.Interfaces.ResponseListener;
import com.example.weatherapp.Misc;
import com.example.weatherapp.Model.Weather;
import com.example.weatherapp.R;
import com.example.weatherapp.ServerCommunication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;

public class CityActivity extends AppCompatActivity {
    private Weather curWeather;

    private String cityId;
    private String cityName;

    private ProgressBar progress;
    private SwipeRefreshLayout container;
    private TextView cityTV;
    private TextView weatherTV;
    private TextView dateTV;

    private DBHelper mDbHelper;
    private SQLiteDatabase db;

    private int animationDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);

        mDbHelper = new DBHelper(getApplicationContext());
        db = mDbHelper.getReadableDatabase();

        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        Bundle b = getIntent().getExtras();
        if ( b != null ) {
            cityId = b.getString(Misc.CITY_ID);
            cityName = b.getString(Misc.CITY_NAME);
        }

        progress = findViewById(R.id.progress);
        cityTV = findViewById(R.id.city_name);
        cityTV.setText(cityName);
        weatherTV = findViewById(R.id.city_weather);
        dateTV = findViewById(R.id.date);
        container = findViewById(R.id.swipe_container);
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateWeather();
                container.setRefreshing(false);
            }
        });

       updateWeather();
    }

    private void updateWeather() {
        startUpdate();
        ServerCommunication.getCurCond(cityId, new ResponseListener() {
            @Override
            public void onSuccess(JSONObject data) {
                try {
                    curWeather = new Weather(data.getJSONArray("Response").getJSONObject(0));
                    DBMethods.saveWeatherToDB(cityId, curWeather, db);
                    updateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String reason) {
                curWeather = DBMethods.getWeatherFromDB(cityId, db);
                updateUI();
                Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI() {
        if (curWeather != null) {
            weatherTV.setText(curWeather.getMetricTemp() + ", " + curWeather.getWeather());
            try {
                dateTV.setText(Misc.dateToString(Misc.parseDate(curWeather.getObservationDateTime())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        stopUpdate();
    }

    private void startUpdate() {
        container.setAlpha(0.5f);
        progress.setVisibility(View.VISIBLE);
        progress.setAlpha(1f);
    }

    private void stopUpdate() {
        container.animate()
                .alpha(1f)
                .setDuration(animationDuration)
                .setListener(null);

        progress.animate()
                .alpha(0f)
                .setDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progress.setVisibility(View.GONE);
                    }
                });
    }
}
