package com.yaroslavgorbachh.counter.counterHistory.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.counter.CounterViewModel;
import com.yaroslavgorbachh.counter.counterHistory.CounterHistoryViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CounterHistoryModule {
    @Binds
    @IntoMap
    @ViewModelKey(CounterHistoryViewModel.class)
    abstract ViewModel bindViewModel(CounterHistoryViewModel viewModel);
}
