package com.example.tucapp;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControllerActivity extends AppCompatActivity {

    private int ptoCount = 0; // 0 - 5
    private boolean frontBack = false; // False = front, true = back
    private int lightMode = 0; // 0 - 3
    private ByteBuffer bb = ByteBuffer.allocateDirect(10);
    private IntBuffer ib = bb.asIntBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        onListeners();
        companionListener();

        ib.put(10, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("tuc_mode", "1")));
    }

    public void onListeners(){
        // PRESS & HOLDS
        View.OnTouchListener otl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    switch (view.getId()) {
                        case R.id.fabFloatDown:
                            ib.put(2, 1);
                            break;
                        case R.id.fabPowerDown:
                            ib.put(3, 1);
                            break;
                        case R.id.fabPowerUp:
                            ib.put(4, 1);
                            break;
                        case R.id.fabTiltUp:
                            ib.put(5, 1);
                            break;
                        case R.id.fabTiltDown:
                            ib.put(6, 1);
                            break;
                    }

                    sendIntBuff();

                } else if(motionEvent.getActionMasked() == MotionEvent.ACTION_UP){
                    ib.put(2, 0);
                    ib.put(3, 0);
                    ib.put(4, 0);
                    ib.put(5, 0);
                    ib.put(6, 0);
                    sendIntBuff();
                    view.performClick();
                }

                return true;
            }
        };
        findViewById(R.id.fabFloatDown).setOnTouchListener(otl);
        findViewById(R.id.fabPowerDown).setOnTouchListener(otl);
        findViewById(R.id.fabPowerUp).setOnTouchListener(otl);
        findViewById(R.id.fabTiltDown).setOnTouchListener(otl);
        findViewById(R.id.fabTiltUp).setOnTouchListener(otl);

        // TOGGLES
        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.fabPTO:
                        ib.put(ptoCounter());
                        break;
                    case R.id.fabFrontBack:
                        ib.put(frontBack());
                        break;
                    case R.id.fabLights:
                        ib.put(toggleLights());
                        break;
                }
                sendIntBuff();
            }
        };

        findViewById(R.id.fabPTO).setOnClickListener(ocl);
        findViewById(R.id.fabFrontBack).setOnClickListener(ocl);
        findViewById(R.id.fabLights).setOnClickListener(ocl);

        // JOYSTICK
        final JoystickView joystick = findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                // Do stuff here
                ib.put(0, angle);
                ib.put(1, strength);
                sendIntBuff();
            }
        });

    }

    @SuppressLint("ClickableViewAccessibility")
    private void companionListener(){
        Button btn = findViewById(R.id.btnCompanion);

        if(PreferenceManager.getDefaultSharedPreferences(this).getString("user_mode", "1").equals("2")){
            disableJoystick();
            btn.setEnabled(true);
            btn.setVisibility(View.VISIBLE);

            btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b)
                        findViewById(R.id.joystickView).requestFocus();
                }
            });

            // COMPANION
            btn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
                        enableJoystick();
                    } else if(motionEvent.getActionMasked() == MotionEvent.ACTION_UP){
                        disableJoystick();
                    }
                    return true;
                }
            });
        } else {
            btn.setEnabled(false);
            btn.setVisibility(View.INVISIBLE);
            enableJoystick();
        }
    }

    private void sendIntBuff(){
        while(getApplicationContext() == this){
            // send ib to Thread here
        }
    }

    private void enableJoystick(){
        JoystickView js = findViewById(R.id.joystickView);
        js.setAlpha(1f);
        js.setEnabled(true);
    }

    private void disableJoystick(){
        JoystickView js = findViewById(R.id.joystickView);
        js.setAlpha(.35f);
        js.setEnabled(false);
    }

    private int ptoCounter(){
        TextView tv = findViewById(R.id.txtPTO);
        ptoCount++;
        if(ptoCount > 5)
            ptoCount = 0;

        tv.setText(getString(R.string.pto_formatted, Integer.toString(ptoCount)));

        return ptoCount;
    }

    private int frontBack(){
        FloatingActionButton fab = findViewById(R.id.fabFrontBack);
        frontBack = !frontBack;
        if(frontBack)
            fab.setImageDrawable(getDrawable(R.drawable.ic_swap_arrows_back));
        else
            fab.setImageDrawable(getDrawable(R.drawable.ic_swap_arrows_front));

        return frontBack ? 1 : 0; // Converts boolean value to integer
    }

    private int toggleLights(){
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

        return lightMode;
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        companionListener();
        ib.put(10, Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("tuc_mode", "1")));
        sendIntBuff();
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
                startActivity(new Intent(this, SettingsActivity.class), ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
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
