package com.yaroslavgorbachh.counter.counter;

import com.yaroslavgorbachh.counter.aboutCounter.AboutCounterFragment;

import dagger.Subcomponent;

@Subcomponent(modules = CounterModule.class)
public interface CounterComponent {

    @Subcomponent.Factory
    interface Builder {
        CounterComponent create();
    }

    void inject(CounterFragment fragment);
}
