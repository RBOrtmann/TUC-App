package com.example.tucapp;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
    }

    // Checks WiFi connection for standard TUC naming convention, then sends intent to ControllerActivity
    public void toController(View v){
        WifiManager wfMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(Objects.requireNonNull(wfMan).getConnectionInfo().getSSID().contains("Ring-Co")){ // THIS SHOULD BE CHANGED TO STANDARD TUC NETWORK CONVENTION
            startActivity(new Intent(this, ControllerActivity.class));
        } else {
            Toast.makeText(this, "Please connect to a TUC network.", Toast.LENGTH_SHORT).show();
        }
    }

    // Sends intent to SettingsActivity
    public void toSettings(View v){
        startActivity(new Intent(this, SettingsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }
}
