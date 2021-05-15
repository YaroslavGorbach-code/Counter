package com.yaroslavgorbachh.counter.counterSettings;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.counterSettings.themes.ThemeUtility;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity {
   @Inject SharedPreferences sharedPreferences;
   @Inject Repo repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication application = (MyApplication) getApplication();
        application.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        ThemeUtility.setTheme(sharedPreferences, this, repo);
        setContentView( R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(v -> finish());

        if (sharedPreferences.getBoolean("lockOrientation", true)) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
