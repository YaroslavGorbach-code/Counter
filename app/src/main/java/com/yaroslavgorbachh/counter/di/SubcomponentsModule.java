package com.yaroslavgorbachh.counter.di;

import com.yaroslavgorbachh.counter.aboutCounter.di.AboutCounterComponent;
import com.yaroslavgorbachh.counter.counter.CounterComponent;
import com.yaroslavgorbachh.counter.countersList.di.CountersComponent;
import com.yaroslavgorbachh.counter.countersList.di.CountersModule;
import com.yaroslavgorbachh.counter.fullscreenCounter.di.FullscreenCounterComponent;

import dagger.Module;

@Module(subcomponents = {
        AboutCounterComponent.class,
        CounterComponent.class,
        CountersComponent.class,
        FullscreenCounterComponent.class})
public class SubcomponentsModule {}
