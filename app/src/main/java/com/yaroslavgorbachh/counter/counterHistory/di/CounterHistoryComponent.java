package com.yaroslavgorbachh.counter.counterHistory.di;

import com.yaroslavgorbachh.counter.counter.CounterFragment;
import com.yaroslavgorbachh.counter.counterHistory.CounterHistoryFragment;

import dagger.Subcomponent;

@Subcomponent(modules = CounterHistoryModule.class)
public interface CounterHistoryComponent {

    @Subcomponent.Factory
    interface Builder {
        CounterHistoryComponent create();
    }

    void inject(CounterHistoryFragment fragment);
}
