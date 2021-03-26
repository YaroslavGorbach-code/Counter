package com.yaroslavgorbachh.counter.countersList.di;

import com.yaroslavgorbachh.counter.counter.CounterFragment;
import com.yaroslavgorbachh.counter.countersList.CountersFragment;
import com.yaroslavgorbachh.counter.countersList.CountersViewModel;

import dagger.Subcomponent;

@Subcomponent(modules = CountersModule.class)
public interface CountersComponent {

    @Subcomponent.Factory
    interface Builder {
        CountersComponent create();
    }

    void inject(CountersFragment fragment);
}
