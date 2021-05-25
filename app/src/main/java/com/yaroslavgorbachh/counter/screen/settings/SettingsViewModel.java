package com.yaroslavgorbachh.counter.screen.settings;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.settings.Settings;
import com.yaroslavgorbachh.counter.component.settings.SettingsImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.DaggerSettingsComponent;
import com.yaroslavgorbachh.counter.di.SettingsComponent;

public class SettingsViewModel extends AndroidViewModel {
    public SettingsComponent settingsComponent;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        settingsComponent = DaggerSettingsComponent.factory().create(((App)application).appComponent);
    }
}
