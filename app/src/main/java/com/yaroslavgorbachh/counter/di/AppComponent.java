package com.yaroslavgorbachh.counter.di;

import android.app.Application;
import android.content.Context;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.aboutCounter.AboutCounterFragment;
import com.yaroslavgorbachh.counter.counter.CounterFragment;
import com.yaroslavgorbachh.counter.counterHistory.CounterHistoryFragment;
import com.yaroslavgorbachh.counter.counterSettings.SettingsActivity;
import com.yaroslavgorbachh.counter.counterSettings.SettingsFragment;
import com.yaroslavgorbachh.counter.counterSettings.themes.ColorPickerDialog;
import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetConfigActivity;
import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.countersList.CountersFragment;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialog;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterFragment;
import com.yaroslavgorbachh.counter.fullscreenCounter.FullscreenCounterFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        RoomModule.class})

public interface AppComponent {


    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application, @BindsInstance Context context);
    }

    void inject(MainActivity activity);
    void inject(CounterFragment  fragment);
    void inject(AboutCounterFragment fragment);
    void inject(CounterHistoryFragment fragment);
    void inject(SettingsFragment fragment);
    void inject(CountersFragment fragment);
    void inject(CounterWidgetConfigActivity a);
    void inject(CounterWidgetProvider widgetProvider);
    void inject(FullscreenCounterFragment fragment);
    void inject(ColorPickerDialog dialog);
    void inject(CreateCounterDialog dialog);
    void inject(CreateEditCounterFragment fragment);
    void inject(SettingsActivity a);



}
