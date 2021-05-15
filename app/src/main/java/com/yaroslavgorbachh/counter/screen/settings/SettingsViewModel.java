package com.yaroslavgorbachh.counter.screen.settings;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.settings.Settings;
import com.yaroslavgorbachh.counter.component.settings.SettingsImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class SettingsViewModel extends ViewModel {
    private Settings settings;

    public Settings getSettings(Repo repo) {
        if (settings == null) {
            settings = new SettingsImp(repo);
        }
        return settings;
    }
}
