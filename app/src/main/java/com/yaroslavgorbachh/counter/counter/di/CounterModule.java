package com.yaroslavgorbachh.counter.counter.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.aboutCounter.AboutCounterViewModel;
import com.yaroslavgorbachh.counter.counter.CounterViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CounterModule {
    @Binds
    @IntoMap
    @ViewModelKey(CounterViewModel.class)
    abstract ViewModel bindViewModel(CounterViewModel viewModel);
}
