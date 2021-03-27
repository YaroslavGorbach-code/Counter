package com.yaroslavgorbachh.counter.createEditCounter.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.counter.CounterViewModel;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialogViewModel;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterFragment;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class CreateEditCounterModule {
    @Binds
    @IntoMap
    @ViewModelKey(CreateCounterDialogViewModel.class)
    abstract ViewModel bindDialogViewModel(CreateCounterDialogViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(CreateEditCounterViewModel.class)
    abstract ViewModel bindViewModel(CreateEditCounterViewModel viewModel);
}
