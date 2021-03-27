package com.yaroslavgorbachh.counter.createEditCounter.di;

import com.yaroslavgorbachh.counter.counter.CounterFragment;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialog;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterFragment;

import dagger.Subcomponent;

@Subcomponent(modules = CreateEditCounterModule.class)
public interface CreateEditCounterComponent {

    @Subcomponent.Factory
    interface Builder {
        CreateEditCounterComponent create();
    }

    void inject(CreateCounterDialog dialog);
    void inject(CreateEditCounterFragment fragment);
}
