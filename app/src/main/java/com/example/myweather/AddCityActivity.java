package com.example.myweather;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

/**
 *  AddCityActivity about add the city form user input
 */
public class AddCityActivity extends AppCompatActivity {

    EditText cityNameText;
    Button saveBtn;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        cityNameText = findViewById(R.id.city_name_edit);
        saveBtn = findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // whether input is empty
                if (cityNameText.getText().toString().isEmpty()) {
                    new AlertDialog.Builder(AddCityActivity.this).setTitle("City Name is Empty!").create().show();
                } else {
                    //save the city name in local sharedPreferences
                    final SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    Set<String> stringSet = sharedPreferences.getStringSet("city_set", new HashSet<String>());
                    final SharedPreferences.Editor editor = sharedPreferences.edit();
                    stringSet.add(cityNameText.getText().toString());
                    editor.putStringSet("city_set", stringSet).commit();
                    editor.apply();
                    //return the mainActivity
                    finish();
                }
            }
        });
    }
}