package com.example.tucapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        startActivity(new Intent(this, ControllerActivity.class));
    }

    public void toSettings(View v){
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
