package com.yaroslavgorbachh.counter.countersList.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.counter.CounterViewModel;
import com.yaroslavgorbachh.counter.countersList.CountersViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CountersModule {

    @Binds
    @IntoMap
    @ViewModelKey(CountersViewModel.class)
    abstract ViewModel bindViewModel(CountersViewModel viewModel);
}
