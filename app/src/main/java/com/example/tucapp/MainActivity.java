/**
 * File: MainActivity.java
 * Author: Brendan Ortmann
 * Owner: Ring-Co LLC
 * For: TUC Companion App
 * Date: July 2019
 *
 * Desc: "Launcher" activity.
 */

package com.example.tucapp;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Checks for LOCATION permission, which is required to get the SSID of the WiFi network for some reason
        if((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        && Build.VERSION.SDK_INT >= 26){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        // Use dark theme if enabled
        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_mode", false))
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        onListeners();
    }

    // Sets onClick listeners for on-screen Views
    private void onListeners(){
        findViewById(R.id.btnPassword).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(getPasswordText().equals(PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString("password", SettingsActivity.getDefaultPassword()))
                || getPasswordText().equals(SettingsActivity.getOverridePassword())){
                    Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                    toSelection();
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        EditText etPassword = findViewById(R.id.etPassword);
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    enableButton(false);
                } else {
                    enableButton(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    // Sends intent to SelectionActivity
    private void toSelection(){
        startActivity(new Intent(this, SelectionActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    // Enables the login button only when there is text to check
    private void enableButton(boolean b){
        findViewById(R.id.btnPassword).setEnabled(b);
    }

    // Gets text from password EditText
    private String getPasswordText(){
        EditText et = findViewById(R.id.etPassword);
        return et.getText().toString();
    }
}
