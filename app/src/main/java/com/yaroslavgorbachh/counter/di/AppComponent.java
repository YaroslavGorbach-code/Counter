package com.yaroslavgorbachh.counter.di;

import android.app.Application;
import android.content.Context;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.screen.about.AboutCounterFragment;
import com.yaroslavgorbachh.counter.screen.counter.CounterFragment;
import com.yaroslavgorbachh.counter.screen.edit.EditCounterFragment;
import com.yaroslavgorbachh.counter.screen.history.HistoryFragment;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;
import com.yaroslavgorbachh.counter.screen.settings.SettingsFragment;
import com.yaroslavgorbachh.counter.screen.settings.ColorPickerDialog;
import com.yaroslavgorbachh.counter.screen.counterWidget.CounterWidgetConfigActivity;
import com.yaroslavgorbachh.counter.screen.counterWidget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.screen.counters.CountersFragment;
import com.yaroslavgorbachh.counter.screen.counters.CreateCounterDialog;
import com.yaroslavgorbachh.counter.screen.fullscreen.FullscreenCounterFragment;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class})

public interface AppComponent {


    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application, @BindsInstance Context context);
    }

    void inject(MainActivity activity);
    void inject(CounterFragment  fragment);
    void inject(AboutCounterFragment fragment);
    void inject(HistoryFragment fragment);
    void inject(SettingsFragment fragment);
    void inject(CountersFragment fragment);
    void inject(CounterWidgetConfigActivity a);
    void inject(CounterWidgetProvider widgetProvider);
    void inject(FullscreenCounterFragment fragment);
    void inject(ColorPickerDialog dialog);
    void inject(CreateCounterDialog dialog);
    void inject(EditCounterFragment fragment);
    void inject(SettingsActivity a);



}
