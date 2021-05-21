package com.yaroslavgorbachh.counter.screen.settings;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.settings.SettingsComponent;
import com.yaroslavgorbachh.counter.component.settings.SettingsComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class SettingsViewModel extends ViewModel {
    private SettingsComponent settingsComponent;

    public SettingsComponent getSettings(Repo repo) {
        if (settingsComponent == null) {
            settingsComponent = new SettingsComponentImp(repo);
        }
        return settingsComponent;
    }
}
