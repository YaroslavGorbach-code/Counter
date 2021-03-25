package com.yaroslavgorbachh.counter.di;

import android.content.Context;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.counterSettings.SettingsActivity;
import com.yaroslavgorbachh.counter.counter.CounterFragment;
import com.yaroslavgorbachh.counter.countersList.CountersFragment;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {SharedPreferencesModule.class, AudioModule.class})
public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Context context);
    }

    void inject(SettingsActivity activity);
    void inject(MainActivity activity);
    void inject(CounterFragment fragment);
    void inject(CountersFragment fragment);


}
