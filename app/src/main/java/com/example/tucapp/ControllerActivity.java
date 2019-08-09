/*
 * File: ControllerActivity.java
 * Author: Brendan Ortmann
 * Owner: Ring-Co LLC
 * For: TUC Companion App
 * Date: July 2019
 *
 * Desc: "Controller" activity which provides an end-user interface for controlling the TUC.
 */

package com.example.tucapp;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class ControllerActivity extends AppCompatActivity {

    private int ptoCount = 0; // 0 - 5
    private boolean frontBack = false; // False = front, true = back
    private int lightMode = 0; // 0 - 3
    private ByteBuffer bb;
    private ByteBuffer bb2;
    private BlockingQueue<ByteBuffer> bq = new LinkedBlockingQueue<>(1);
    private BlockingQueue<ByteBuffer> bq2 = new LinkedBlockingQueue<>(1);

/*
PTO, Lights, and Front/back should be on a separate message that sends integers instead of just quick presses
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);

        onListeners();
        companionListener();

        bb = ByteBuffer.wrap(new byte[8]);
        bb2 = ByteBuffer.wrap(new byte[8]);

        // Start threads for sending info
        new Thread(new Sender()).start();
        new ControllerThread(bq, bq2).start();
    }

    // Sets the onTouch, onClick, and onMove listeners for the on-screen Views
    private void onListeners(){
        // PRESS & HOLDS
        View.OnTouchListener otl = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view instanceof FloatingActionButton){
                    if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
                        switch (view.getId()){
                            case R.id.fabFloatDown:
                                bb2.put(0, (byte)1); break;
                            case R.id.fabPowerDown:
                                bb2.put(1, (byte)1); break;
                            case R.id.fabPowerUp:
                                bb2.put(2, (byte)1); break;
                            case R.id.fabTiltDown:
                                bb2.put(3, (byte)1); break;
                            case R.id.fabTiltUp:
                                bb2.put(4, (byte)1); break;
                            case R.id.fabLights:
                                bb2.put(5, (byte)1); break;
                            case R.id.fabPTO:
                                bb2.put(6, (byte)1); break;
                        }
                    } else if(motionEvent.getActionMasked() == MotionEvent.ACTION_UP){
                        switch (view.getId()){
                            case R.id.fabFloatDown:
                                bb2.put(0, (byte)0); break;
                            case R.id.fabPowerDown:
                                bb2.put(1, (byte)0); break;
                            case R.id.fabPowerUp:
                                bb2.put(2, (byte)0); break;
                            case R.id.fabTiltDown:
                                bb2.put(3, (byte)0); break;
                            case R.id.fabTiltUp:
                                bb2.put(4, (byte)0); break;
                            case R.id.fabLights:
                                bb2.put(5, (byte)0);
                                toggleLights(); break;
                            case R.id.fabPTO:
                                bb2.put(6, (byte)0);
                                ptoCounter(); break;
                        }
                        view.performClick();
                    }
                }
                return false;
            }
        };

        findViewById(R.id.fabFloatDown).setOnTouchListener(otl);
        findViewById(R.id.fabPowerDown).setOnTouchListener(otl);
        findViewById(R.id.fabPowerUp).setOnTouchListener(otl);
        findViewById(R.id.fabTiltDown).setOnTouchListener(otl);
        findViewById(R.id.fabTiltUp).setOnTouchListener(otl);
        findViewById(R.id.fabLights).setOnTouchListener(otl);
        findViewById(R.id.fabPTO).setOnTouchListener(otl);

        // TOGGLES
        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.fabFrontBack) {
                    frontBack();
                }
            }
        };

        findViewById(R.id.fabFrontBack).setOnClickListener(ocl);

        // JOYSTICK
        JoystickView joystick = findViewById(R.id.joystickView);
        joystick.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                bb.putInt(0, angle);
                bb.putInt(4, strength);
            }
        });

    }

    // If Companion Mode is enabled, this sets the listener for that View
    @SuppressLint("ClickableViewAccessibility")
    private void companionListener(){
        if(PreferenceManager.getDefaultSharedPreferences(this).getString("user_mode", "1").equals("2")){
            disableJoystick();
            enableCompanion();
        } else {
            disableCompanion();
            enableJoystick();
        }
    }

    // This method enables the companion button
    @SuppressLint("ClickableViewAccessibility")
    private void enableCompanion(){
        Button btn = findViewById(R.id.btnCompanion);
        btn.setEnabled(true);
        btn.setVisibility(View.VISIBLE);

        btn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    findViewById(R.id.joystickView).requestFocus();
            }
        });

        btn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN){
                    enableJoystick();
                } else if(motionEvent.getActionMasked() == MotionEvent.ACTION_UP){
                    disableJoystick();
                    view.performClick();
                }
                return true;
            }
        });
    }

    // This method disables the companion button
    private void disableCompanion(){
        Button btn = findViewById(R.id.btnCompanion);
        btn.setEnabled(false);
        btn.setVisibility(View.INVISIBLE);
    }

    // A method for enabling the Joystick
    private void enableJoystick(){
        JoystickView js = findViewById(R.id.joystickView);
        js.setAlpha(1f);
        js.setEnabled(true);
    }

    // A method for disabling the Joystick
    private void disableJoystick(){
        JoystickView js = findViewById(R.id.joystickView);
        js.setAlpha(.35f);
        js.setEnabled(false);
    }

    // Increments the PTO counter and returns its value
    private void ptoCounter(){
        TextView tv = findViewById(R.id.txtPTO);
        ptoCount++;
        if(ptoCount > 5)
            ptoCount = 0;

        tv.setText(getString(R.string.pto_formatted, Integer.toString(ptoCount)));
    }

    // Switches the attachment mode to front if back, and to back if front, returning the value as an integer
    private void frontBack(){
        FloatingActionButton fab = findViewById(R.id.fabFrontBack);
        frontBack = !frontBack;

        fab.setImageDrawable(
                frontBack
                        ? getDrawable(R.drawable.ic_swap_arrows_back)
                        : getDrawable(R.drawable.ic_swap_arrows_front));

        bb2.put(7, (byte)(frontBack ? 1 : 0));
    }

    // Increments the counter that keeps track of the light mode and returns it
    private void toggleLights(){
        FloatingActionButton fab = findViewById(R.id.fabLights);

        lightMode++;
        if(lightMode > 3)
            lightMode = 0;

        switch(lightMode){
            case 0:
                fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_off)); break;
            case 1:
                fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_dimmed)); break;
            case 2:
                fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_high)); break;
            case 3:
                fab.setImageDrawable(getDrawable(R.drawable.ic_car_light_both)); break;
        }
    }

    // Subclass that continuously enqueues the ByteBuffer into the BlockingQueue
    private class Sender implements Runnable{
        @Override
        public void run() {
            ByteBuffer oldBB = ByteBuffer.wrap(new byte[8]);
            ByteBuffer oldBB2 = ByteBuffer.wrap(new byte[8]);
            try {
                while(getClass().getSimpleName().equals("Sender")) {

                    // THis is not working for some reason...
                    if (bb.compareTo(oldBB) != 0){
                        bq.put(bb);
                        Log.d("Sender", "put BB");
                    }

                    if(bb2.compareTo(oldBB2) != 0) {
                        bq2.put(bb2);
                        Log.d("Sender", "put BB2");
                    }

                    oldBB = bb;
                    oldBB2 = bb2;
                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    /*
    The next few methods deal almost entirely with how the Activity behaves, ensuring fullscreen and the like
     */
    @Override
    public void onResume() {
        super.onResume();
        hideSystemUI();
        companionListener();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    // Hides the system UI
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
