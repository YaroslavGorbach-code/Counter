package com.yaroslavgorbachh.counter.Activityes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import com.yaroslavgorbachh.counter.Fragments.SettingsFragment;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

import static com.yaroslavgorbachh.counter.ScrollColorPicker.THEME_VALUE_KEY;

public class SettingsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme();
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
        Toolbar mToolbar = findViewById(R.id.toolbar_settings);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean("lockOrientation", true ) ){
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

    }

    public void setTheme(){
       SharedPreferences mPref = getSharedPreferences(THEME_VALUE_KEY, Context.MODE_PRIVATE);
        int color = mPref.getInt(THEME_VALUE_KEY, getResources().getColor(R.color.colorAccent));
        if (color == getResources().getColor(R.color.colorAccent_orange)){
            setTheme(R.style.AppTheme_orange);
        }

        if (color == getResources().getColor(R.color.colorAccent)){
            setTheme(R.style.AppTheme);
        }
    }


}
