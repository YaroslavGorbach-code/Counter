package com.yaroslavgorbachh.counter.screen.settings;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.utill.ThemeUtility;
import com.yaroslavgorbachh.counter.data.Repo;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity {
   @Inject SharedPreferences sharedPreferences;
   @Inject Repo repo;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication application = (MyApplication) getApplication();
        application.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        ThemeUtility.setTheme(sharedPreferences, this, repo);
        setContentView( R.layout.activity_settings);
        if (sharedPreferences.getBoolean("lockOrientation", true)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());


    }

}