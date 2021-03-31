package com.yaroslavgorbachh.counter.counterWidget.di;

import android.appwidget.AppWidgetProvider;

import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetConfigActivity;
import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialog;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterFragment;
import com.yaroslavgorbachh.counter.createEditCounter.di.CreateEditCounterModule;
import com.yaroslavgorbachh.counter.database.Models.Counter;

import dagger.Subcomponent;

@Subcomponent(modules = CounterWidgetModule.class)
public interface CounterWidgetComponent {

    @Subcomponent.Factory
    interface Builder {
        CounterWidgetComponent create();
    }

    void inject(CounterWidgetConfigActivity counterWidgetConfigActivity);
    void inject(CounterWidgetProvider counterWidgetProvider);
}
