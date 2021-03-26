package com.yaroslavgorbachh.counter.di;

import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelBuilderModule {

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelProviderFactory(
            ViewModelProviderFactory viewModelProviderFactory);

}
