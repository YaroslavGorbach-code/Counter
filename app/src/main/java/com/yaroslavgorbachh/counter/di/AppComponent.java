package com.yaroslavgorbachh.counter.di;

import android.app.Application;
import android.content.Context;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.aboutCounter.di.AboutCounterComponent;
import com.yaroslavgorbachh.counter.counter.di.CounterComponent;
import com.yaroslavgorbachh.counter.counterHistory.di.CounterHistoryComponent;
import com.yaroslavgorbachh.counter.counterSettings.di.SettingsComponent;
import com.yaroslavgorbachh.counter.counterWidget.di.CounterWidgetComponent;
import com.yaroslavgorbachh.counter.countersList.di.CountersComponent;
import com.yaroslavgorbachh.counter.createEditCounter.di.CreateEditCounterComponent;
import com.yaroslavgorbachh.counter.fullscreenCounter.di.FullscreenCounterComponent;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {
        AppModule.class,
        ViewModelBuilderModule.class,
        RoomModule.class,
        SubcomponentsModule.class})

public interface AppComponent {

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application, @BindsInstance Context context);
    }

    void inject(MainActivity activity);

    AboutCounterComponent.Builder aboutCounterComponentFactory();
    CounterComponent.Builder counterComponentFactory();
    CountersComponent.Builder countersComponentFactory();
    FullscreenCounterComponent.Builder fullscreenCounterComponent();
    CreateEditCounterComponent.Builder createEditCounterComponent();
    SettingsComponent.Builder settingsComponentFactory();
    CounterHistoryComponent.Builder counterHistoryComponent();
    CounterWidgetComponent.Builder counterWidgetComponent();

}
