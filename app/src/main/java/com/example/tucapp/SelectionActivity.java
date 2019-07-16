package com.example.tucapp;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        getWindow().setEnterTransition(new Explode());
//        getWindow().setExitTransition(new Slide(Gravity.LEFT));
    }

    public void toController(View v){
        WifiManager wfMan = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if(wfMan.getConnectionInfo().getSSID().contains("Ring-Co")){ // THIS SHOULD BE CHANGED TO STANDARD TUC NETWORK CONVENTION
            startActivity(new Intent(this, ControllerActivity.class));
        } else {
            Toast.makeText(this, "Please connect to a TUC network.", Toast.LENGTH_SHORT).show();
        }
    }

    public void toSettings(View v){
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
