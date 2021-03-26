package com.yaroslavgorbachh.counter.aboutCounter.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.aboutCounter.AboutCounterViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class AboutCounterModule {
    @Binds
    @IntoMap
    @ViewModelKey(AboutCounterViewModel.class)
    abstract ViewModel bindViewModel(AboutCounterViewModel viewModel);
}
