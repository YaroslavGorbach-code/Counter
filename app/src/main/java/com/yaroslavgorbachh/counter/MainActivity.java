package com.yaroslavgorbachh.counter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.yaroslavgorbachh.counter.screen.widget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.data.Repo;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.INTENT_VOLUME_DOWN;
import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.INTENT_VOLUME_UP;
import static com.yaroslavgorbachh.counter.screen.settings.SettingsFragment.THEME_CHANGED_BROADCAST;


public class MainActivity extends AppCompatActivity {
    private boolean mAllowedVolumeButtons;
    private BroadcastReceiver mMessageReceiver;
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject SharedPreferences sharedPreferences;
    @Inject Repo repo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App application = (App) getApplication();
        application.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // recreating activity when theme is changed
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               recreate();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(THEME_CHANGED_BROADCAST));
    }


    @Override
    protected void onStart() {
        super.onStart();
        /* depending on pref keeps screen on or let it off*/
        mAllowedVolumeButtons = sharedPreferences.getBoolean("useVolumeButtons", true);
        if (sharedPreferences.getBoolean("keepScreenOn", true)) {
            MainActivity.this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        if (!sharedPreferences.getBoolean("keepScreenOn", true)) {
            MainActivity.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        /* sends local broadcast to catch the key volume up event*/
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && mAllowedVolumeButtons) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(INTENT_VOLUME_UP);
            return true;
        }

        /* sends local broadcast to catch the key volume down event*/
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && mAllowedVolumeButtons) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(INTENT_VOLUME_DOWN);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
       Disposable disposable = CounterWidgetProvider.updateWidgets(this, repo);
       if (disposable!=null)
       mDisposables.add(disposable);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        mDisposables.dispose();
    }

}
