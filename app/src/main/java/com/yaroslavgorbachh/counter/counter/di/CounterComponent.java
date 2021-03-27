package com.yaroslavgorbachh.counter.counter.di;

import com.yaroslavgorbachh.counter.counter.CounterFragment;

import dagger.Subcomponent;

@Subcomponent(modules = CounterModule.class)
public interface CounterComponent {

    @Subcomponent.Factory
    interface Builder {
        CounterComponent create();
    }

    void inject(CounterFragment fragment);
}
