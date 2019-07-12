package com.example.tucapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControllerActivity extends AppCompatActivity {

    private int ptoCount = 0; // 0 - 5
    private boolean frontBack = false; // False = front, true = back

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
                    case R.id.fabHeadlights:
                        // maybe combine with Worklights?
                        toggleLights();
                        break;
                    case R.id.fabWorklights:
                        // maybe combine with Headlights?
                        break;
                }
            }
        };

        findViewById(R.id.fabPTO).setOnClickListener(ocl);
        findViewById(R.id.fabFrontBack).setOnClickListener(ocl);
        findViewById(R.id.fabHeadlights).setOnClickListener(ocl);
        findViewById(R.id.fabWorklights).setOnClickListener(ocl);
    }

    private void ptoCounter(){
        TextView tv = findViewById(R.id.txtPTO);
        if(ptoCount >= 5)
            ptoCount = 0;
        ptoCount++;

        tv.setText(getString(R.string.pto_formatted, Integer.toString(ptoCount)));
    }

    private void frontBack(){
        TextView tv = findViewById(R.id.txtFrontBack);
        frontBack = !frontBack;

        if(frontBack)
            tv.setText(getString(R.string.toggle_back));
        else
            tv.setText(getString(R.string.toggle_front));
    }

    private void toggleLights(){
        TextView tv = findViewById(R.id.txtLights);
        if(tv.getText().equals(getString(R.string.toggle_on)))
            tv.setText(getString(R.string.toggle_off));
        else if(tv.getText().equals(getString(R.string.toggle_off)))
            tv.setText(getString(R.string.toggle_on));
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
