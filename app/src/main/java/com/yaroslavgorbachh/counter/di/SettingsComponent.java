package com.yaroslavgorbachh.counter.di;

import android.content.Context;

import com.yaroslavgorbachh.counter.component.fullscreen.Fullscreen;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenImp;
import com.yaroslavgorbachh.counter.component.settings.Settings;
import com.yaroslavgorbachh.counter.component.settings.SettingsImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.settings.SettingsFragment;

import dagger.BindsInstance;
import dagger.Component;
import dagger.Module;
import dagger.Provides;

@ViewModelScope
@Component(dependencies = {AppComponent.class},
        modules = {SettingsComponent.SettingsModule.class})
public interface SettingsComponent {
    void inject(SettingsFragment fragment);

    @Component.Factory
    interface Factory {
        SettingsComponent create(AppComponent appComponent);
    }

    @Module
    class SettingsModule {
        @ViewModelScope
        @Provides
        Settings provideSettings(Repo repo){
            return new SettingsImp(repo);
        }
    }
}
