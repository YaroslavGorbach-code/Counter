package com.yaroslavgorbachh.counter.Activityes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;

import com.yaroslavgorbachh.counter.Fragments.SettingsFragment;
import com.yaroslavgorbachh.counter.R;

public class SettingsActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();
        Toolbar mToolbar = findViewById(R.id.toolbar_settings);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

}
