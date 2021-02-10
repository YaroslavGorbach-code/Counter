package com.yaroslavgorbach.counter.Activityes;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import com.yaroslavgorbach.counter.R;

public class MainActivity extends AppCompatActivity {
    public static final String ON_KEY_DOWN_BROADCAST = "ON_KEY_DOWN_BROADCAST";
    public static final String KEYCODE_EXTRA = "KEYCODE_EXTRA";
    public static final int KEYCODE_VOLUME_UP = 24;
    public static final int KEYCODE_VOLUME_DOWN = 25;
    private boolean mAllowedVolumeButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (mSharedPreferences.getBoolean("nightMod", false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mAllowedVolumeButtons = mSharedPreferences.getBoolean("useVolumeButtons", false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO: 2/7/2021 сделать так чтобы не реагировала когда в врагменте со счетчиками
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && mAllowedVolumeButtons){
            Intent intent = new Intent(ON_KEY_DOWN_BROADCAST).putExtra(KEYCODE_EXTRA, KEYCODE_VOLUME_UP);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;
        }

        // TODO: 2/7/2021 сделать так чтобы не реагировала когда в врагменте со счетчиками
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && mAllowedVolumeButtons){
            Intent intent = new Intent(ON_KEY_DOWN_BROADCAST).putExtra(KEYCODE_EXTRA, KEYCODE_VOLUME_DOWN);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
