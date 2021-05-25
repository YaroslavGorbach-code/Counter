package com.yaroslavgorbachh.counter.screen.settings;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Repo;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity {
    public static final int RECREATE_RESULT_CODE = 1;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Repo repo = ((App)getApplication()).provideRepo();
        if (repo.getIsOrientationLock()) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
        if (repo.getKeepScreenOnIsAllow()){
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new SettingsFragment())
                .commit();

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

}
