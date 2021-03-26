package com.yaroslavgorbachh.counter.di;

import android.app.Application;
import android.content.Context;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.aboutCounter.di.AboutCounterComponent;
import com.yaroslavgorbachh.counter.counter.CounterComponent;
import com.yaroslavgorbachh.counter.counterSettings.SettingsActivity;
import com.yaroslavgorbachh.counter.counter.CounterFragment;
import com.yaroslavgorbachh.counter.countersList.CountersFragment;
import com.yaroslavgorbachh.counter.countersList.di.CountersComponent;
import com.yaroslavgorbachh.counter.fullscreenCounter.di.FullscreenCounterComponent;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        SharedPreferencesModule.class,
        AudioModule.class,
        ViewModelBuilderModule.class,
        RoomModule.class,
        SubcomponentsModule.class})

public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application, @BindsInstance Context context);
    }

    void inject(SettingsActivity activity);
    void inject(MainActivity activity);

    AboutCounterComponent.Builder aboutCounterComponentFactory();
    CounterComponent.Builder counterComponentFactory();
    CountersComponent.Builder countersComponentFactory();
    FullscreenCounterComponent.Builder fullscreenCounterComponent();

}
