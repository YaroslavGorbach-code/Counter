package com.yaroslavgorbachh.counter.di;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.screen.about.AboutCounterFragment;
import com.yaroslavgorbachh.counter.screen.counter.CounterFragment;
import com.yaroslavgorbachh.counter.screen.counters.CounterCreateDialog;
import com.yaroslavgorbachh.counter.screen.counters.CountersFragment;
import com.yaroslavgorbachh.counter.screen.edit.EditCounterFragment;
import com.yaroslavgorbachh.counter.screen.fullscreen.FullscreenFragment;
import com.yaroslavgorbachh.counter.screen.history.HistoryFragment;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;
import com.yaroslavgorbachh.counter.screen.settings.SettingsFragment;
import com.yaroslavgorbachh.counter.screen.widget.WidgetConfigActivity;
import com.yaroslavgorbachh.counter.screen.widget.WidgetProvider;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(dependencies = RepoProvider.class)
public interface AppComponent extends RepoProvider {
    void inject(MainActivity activity);
    void inject(HistoryFragment fragment);
    void inject(SettingsFragment fragment);
    void inject(WidgetConfigActivity a);
    void inject(WidgetProvider widgetProvider);
    void inject(FullscreenFragment fragment);
    void inject(CounterCreateDialog dialog);
    void inject(EditCounterFragment fragment);
    void inject(SettingsActivity a);

    @Component.Factory
    interface Factory {
        AppComponent create(RepoProvider repoProvider);
    }
}
