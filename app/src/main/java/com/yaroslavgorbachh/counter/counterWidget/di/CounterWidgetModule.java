package com.yaroslavgorbachh.counter.counterWidget.di;

import android.appwidget.AppWidgetProvider;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialogViewModel;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterViewModel;
import com.yaroslavgorbachh.counter.database.Repo;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class CounterWidgetModule {

    @ContributesAndroidInjector
    abstract CounterWidgetProvider contributesMyTestReceiver();

}
