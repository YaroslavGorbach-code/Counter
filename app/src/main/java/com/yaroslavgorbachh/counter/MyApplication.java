package com.yaroslavgorbachh.counter;

import android.app.Application;

import com.yaroslavgorbachh.counter.di.AppComponent;
import com.yaroslavgorbachh.counter.di.DaggerAppComponent;

public class MyApplication extends Application {

    public AppComponent appComponent;
    @Override
    public void onCreate() {
        appComponent = DaggerAppComponent.factory().create(this, getApplicationContext());
        super.onCreate();
    }
}
