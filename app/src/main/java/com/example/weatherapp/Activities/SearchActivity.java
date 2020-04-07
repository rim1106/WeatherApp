package com.example.weatherapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.Adapters.SearchResultsAdapter;
import com.example.weatherapp.DataBase.DBHelper;
import com.example.weatherapp.DataBase.DBMethods;
import com.example.weatherapp.Interfaces.ResponseListener;
import com.example.weatherapp.Misc;
import com.example.weatherapp.Model.City;
import com.example.weatherapp.R;
import com.example.weatherapp.ServerCommunication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ProgressBar progress;
    private ListView citiesLV;
    private EditText searchET;
    private TextView emptyTV;

    private SearchResultsAdapter adapter;
    private List<City> searchResults;

    private int animationDuration;

    private DBHelper mDbHelper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        animationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mDbHelper = new DBHelper(getApplicationContext());
        db = mDbHelper.getReadableDatabase();

        searchET = findViewById(R.id.search);
        progress = findViewById(R.id.progress);
        citiesLV = findViewById(R.id.search_results);
        emptyTV = findViewById(R.id.empty);

        searchResults = new ArrayList<City>();
        adapter = new SearchResultsAdapter(getApplicationContext(), searchResults);
        citiesLV.setAdapter(adapter);
        citiesLV.setEmptyView(emptyTV);

        citiesLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = searchResults.get(position);
                Intent intent = new Intent(SearchActivity.this, CityActivity.class);
                intent.putExtra(Misc.CITY_ID, city.getId());
                intent.putExtra(Misc.CITY_NAME, city.getName());
                startActivity(intent);
            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                final String s = cs.toString();
                searchResults.clear();
                if ("".equals(s.trim())) {
                    adapter.notifyDataSetChanged();
                    return;
                }
                startSearch();
                ServerCommunication.searchCities(s, new ResponseListener() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        try {
                            JSONArray result = data.getJSONArray("Response");
                            for(int i = 0; i < result.length(); i++) {
                                City city = new City(result.getJSONObject(i));
                                searchResults.add(city);
                                DBMethods.saveCityToDB(city,db);
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JSONException ex) {

                        }

                        stopSearch();
                    }

                    @Override
                    public void onError(String reason) {
                        searchResults.addAll(DBMethods.searchCityInDB(s,db));
                        adapter.notifyDataSetChanged();
                        stopSearch();
                        Toast.makeText(getApplicationContext(), reason, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) { }

            @Override
            public void afterTextChanged(Editable arg0) { }
        });
    }

    private void startSearch() {
        citiesLV.setAlpha(0.5f);
        progress.setVisibility(View.VISIBLE);
        progress.setAlpha(1f);
    }

    private void stopSearch() {
        citiesLV.animate()
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
