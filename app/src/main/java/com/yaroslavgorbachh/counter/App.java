package com.yaroslavgorbachh.counter;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.AppComponent;
import com.yaroslavgorbachh.counter.di.DaggerAppComponent;
import com.yaroslavgorbachh.counter.di.DaggerRepoComponent;
import com.yaroslavgorbachh.counter.di.RepoProvider;

public class App extends Application implements RepoProvider {
    public AppComponent appComponent;

    @Override
    public void onCreate() {
        appComponent = DaggerAppComponent.factory().create(DaggerRepoComponent.factory().create(this));
        super.onCreate();
        if (provideRepo().getIsNightMod()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    public Repo provideRepo() {
        return appComponent.provideRepo();
    }
}
