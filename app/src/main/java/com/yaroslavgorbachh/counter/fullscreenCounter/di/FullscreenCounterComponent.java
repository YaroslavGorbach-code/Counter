package com.yaroslavgorbachh.counter.fullscreenCounter.di;

import com.yaroslavgorbachh.counter.fullscreenCounter.FullscreenCounterFragment;

import dagger.Subcomponent;

@Subcomponent(modules = FullscreenCounterModule.class)
public interface FullscreenCounterComponent {

    @Subcomponent.Factory
    interface Builder {
        FullscreenCounterComponent create();
    }

    void inject(FullscreenCounterFragment fragment);
}
