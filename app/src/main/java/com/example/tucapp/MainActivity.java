package com.example.tucapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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

        onListeners();
    }

    public void onListeners(){

        final Button btnPassword = findViewById(R.id.btnPassword);
        btnPassword.setEnabled(false);
        final EditText etPassword = findViewById(R.id.etPassword);

        btnPassword.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(etPassword.getText().toString().equals(PreferenceManager.getDefaultSharedPreferences(
                        getApplicationContext()).getString("password", "admin"))
                        || etPassword.getText().toString().equals("overridepassword")){ // Override password should be changed
                    Toast.makeText(getApplicationContext(), "Login successful.", Toast.LENGTH_SHORT).show();
                    login();
                } else {
                    Toast.makeText(getApplicationContext(), "Login failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length() == 0){
                    btnPassword.setEnabled(false);
                } else {
                    btnPassword.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    private void login(){
        startActivity(new Intent(this, SelectionActivity.class)); //ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
    }
}
