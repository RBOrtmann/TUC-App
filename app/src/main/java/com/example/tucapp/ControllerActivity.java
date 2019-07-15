package com.example.tucapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControllerActivity extends AppCompatActivity {

    private int ptoCount = 0; // 0 - 5
    private boolean frontBack = false; // False = front, true = back
    private int lightMode = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        onListeners();

        JoystickView joystick = findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // Do stuff here
            }
        });
    }

    public void onListeners(){
        View.OnTouchListener otl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(view.getId()) {
                    case R.id.fabFloatDown:
                        // make boolean true
                        return true;
                    case R.id.fabPowerDown:
                        // make boolean true
                        return true;
                    case R.id.fabPowerUp:
                        // make boolean true
                        return true;
                    case R.id.fabTiltUp:
                        // make bool true
                        return true;
                    case R.id.fabTiltDown:
                        // make bool true
                        return true;
                }
                return false;
            }
        };
        findViewById(R.id.fabFloatDown).setOnTouchListener(otl);
        findViewById(R.id.fabPowerDown).setOnTouchListener(otl);
        findViewById(R.id.fabPowerUp).setOnTouchListener(otl);
        findViewById(R.id.fabTiltDown).setOnTouchListener(otl);
        findViewById(R.id.fabTiltUp).setOnTouchListener(otl);

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.fabPTO:
                        ptoCounter();
                        break;
                    case R.id.fabFrontBack:
                        frontBack();
                        break;
                    case R.id.fabLights:
                        toggleLights();
                        break;
                }
            }
        };

        findViewById(R.id.fabPTO).setOnClickListener(ocl);
        findViewById(R.id.fabFrontBack).setOnClickListener(ocl);
        findViewById(R.id.fabLights).setOnClickListener(ocl);
    }

    private void ptoCounter(){
        TextView tv = findViewById(R.id.txtPTO);
        ptoCount++;
        if(ptoCount > 5)
            ptoCount = 0;

        tv.setText(getString(R.string.pto_formatted, Integer.toString(ptoCount)));
    }

    private void frontBack(){
        FloatingActionButton fab = findViewById(R.id.fabFrontBack);
        frontBack = !frontBack;
        if(frontBack)
            fab.setImageDrawable(getDrawable(R.drawable.ic_swap_arrows_back));
        else
            fab.setImageDrawable(getDrawable(R.drawable.ic_swap_arrows_front));
    }

    private void toggleLights(){
        FloatingActionButton fab = findViewById(R.id.fabLights);
        lightMode++;
        if(lightMode > 3)
            lightMode = 0;

        if(lightMode == 0)
            fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_off));
        else if(lightMode == 1)
            fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_dimmed));
        else if(lightMode == 2)
            fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_high));
        else if(lightMode == 3)
            fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_both));

    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE // This prevents the action bar from being shown
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_settings:
                //Toast.makeText(this, "Settings selected...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_logout:
                //Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                break;
        }
        return false;
    }
}
