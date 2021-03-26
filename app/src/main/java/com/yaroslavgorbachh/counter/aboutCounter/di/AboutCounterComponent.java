package com.yaroslavgorbachh.counter.aboutCounter.di;

import com.yaroslavgorbachh.counter.aboutCounter.AboutCounterFragment;

import dagger.Component;
import dagger.Subcomponent;

@Subcomponent(modules = AboutCounterModule.class)
public interface AboutCounterComponent {

    @Subcomponent.Factory
    interface Builder {
        AboutCounterComponent create();
    }

    void inject(AboutCounterFragment fragment);
}
