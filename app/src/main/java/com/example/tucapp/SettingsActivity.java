package com.example.tucapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
//            Preference.OnPreferenceChangeListener opcl = new Preference.OnPreferenceChangeListener() {
//                @Override
//                public boolean onPreferenceChange(Preference preference, Object newValue) {
//                    if(PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext())).getBoolean("dark_mode", false)){
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//                        return true;
//                    } else {
//                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//                    }
//                    return false;
//                }
//            };
//            Objects.requireNonNull(findPreference("dark_mode")).setOnPreferenceChangeListener(opcl);
        }
    }

    @Override
    protected void onPause(){
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(sp.getString("password", "admin").length() == 0){
            sp.edit().putString("password", "admin").apply();
        }
        super.onPause();
    }
}