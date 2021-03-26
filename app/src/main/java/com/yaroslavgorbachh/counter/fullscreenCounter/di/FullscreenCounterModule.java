package com.yaroslavgorbachh.counter.fullscreenCounter.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.counter.CounterViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;
import com.yaroslavgorbachh.counter.fullscreenCounter.FullscreenCounterViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class FullscreenCounterModule {
    @Binds
    @IntoMap
    @ViewModelKey(FullscreenCounterViewModel.class)
    abstract ViewModel bindViewModel(FullscreenCounterViewModel viewModel);
}
