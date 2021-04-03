package com.yaroslavgorbachh.counter;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.RemoteViews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.yaroslavgorbachh.counter.counterSettings.ColorPickerDialog;
import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.countersList.CountersFragmentDirections;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.INTENT_VOLUME_DOWN;
import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.INTENT_VOLUME_UP;
import static com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider.START_MAIN_ACTIVITY_EXTRA;


public class MainActivity extends AppCompatActivity {
    private boolean mAllowedVolumeButtons;
    private BroadcastReceiver mMessageReceiver;

    @Inject SharedPreferences sharedPreferences;
    @Inject Repo repo;
    private final CompositeDisposable mDisposables = new CompositeDisposable();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        MyApplication application = (MyApplication) getApplication();
        application.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        new Utility().setTheme(sharedPreferences, this, repo);
        setContentView(R.layout.activity_main);

        // recreating activity when theme is changed
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
               recreate();
            }
        };

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(ColorPickerDialog.THEME_CHANGED_BROADCAST));
    }


    @Override
    protected void onStart() {
        super.onStart();
        /* depending on pref keeps screen on or let it off*/
        mAllowedVolumeButtons = sharedPreferences.getBoolean("useVolumeButtons", false);
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
        mDisposables.add(CounterWidgetProvider.updateWidgets(this, repo));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        mDisposables.dispose();
    }

}
