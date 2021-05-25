package com.yaroslavgorbachh.counter;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.yaroslavgorbachh.counter.screen.widget.WidgetProvider;
import com.yaroslavgorbachh.counter.data.Repo;

import javax.inject.Inject;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.INTENT_VOLUME_DOWN;
import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.INTENT_VOLUME_UP;


public class MainActivity extends AppCompatActivity {
    private Repo mRepo;

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRepo = ((App)getApplication()).provideRepo();

        if (mRepo.getIsOrientationLock()) {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        if (mRepo.getKeepScreenOnIsAllow()){
            this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }else {
            this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP && mRepo.getUseVolumeButtonsIsAllow()) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(INTENT_VOLUME_UP);
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN && mRepo.getUseVolumeButtonsIsAllow()) {
            LocalBroadcastManager.getInstance(this).sendBroadcast(INTENT_VOLUME_DOWN);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onStop() {
        super.onStop();
        WidgetProvider.updateWidgets(this, mRepo);
    }

}
