package com.yaroslavgorbachh.counter.di;

import com.yaroslavgorbachh.counter.aboutCounter.di.AboutCounterComponent;
import com.yaroslavgorbachh.counter.counter.di.CounterComponent;
import com.yaroslavgorbachh.counter.countersList.di.CountersComponent;
import com.yaroslavgorbachh.counter.createEditCounter.di.CreateEditCounterComponent;
import com.yaroslavgorbachh.counter.createEditCounter.di.CreateEditCounterModule;
import com.yaroslavgorbachh.counter.fullscreenCounter.di.FullscreenCounterComponent;

import dagger.Module;

@Module(subcomponents = {
        AboutCounterComponent.class,
        CounterComponent.class,
        CountersComponent.class,
        FullscreenCounterComponent.class,
        CreateEditCounterComponent.class})
public class SubcomponentsModule {}
