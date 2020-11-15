package com.example.myweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * MainActivity,display the city list and welcome page
 */
public class MainActivity extends AppCompatActivity {


    //cityList page an
    LinearLayout noDataLinear, dataLinear;
    ArrayList<String> cityNameList = new ArrayList<>();
    ListView cityNameListView;
    ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fabButton for link to add Activity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCityActivity.class);
                startActivity(intent);
            }
        });

        noDataLinear = findViewById(R.id.no_data_linear);
        dataLinear = findViewById(R.id.data_linear);
        cityNameListView = findViewById(R.id.city_name_list_view);
        arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, cityNameList);
        cityNameListView.setAdapter(arrayAdapter);
        cityNameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("cityName", cityNameList.get(i));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //you should check the local whether has city data
        final SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final Set<String> stringSet = sharedPreferences.getStringSet("city_set", new HashSet<String>());
        if (stringSet.size() > 0) {
            noDataLinear.setVisibility(View.GONE);
            dataLinear.setVisibility(View.VISIBLE);
            //load the data
            if (stringSet.size() != arrayAdapter.getCount()) {
                //update data
                cityNameList.clear();
                cityNameList.addAll(stringSet);
                arrayAdapter.notifyDataSetChanged();
            }
        } else {
            //show the welcome page
            noDataLinear.setVisibility(View.VISIBLE);
            dataLinear.setVisibility(View.GONE);
        }
    }
}