package com.yaroslavgorbachh.counter.counterSettings.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.counterSettings.ColorPickerDialogViewModel;
import com.yaroslavgorbachh.counter.counterSettings.SettingsViewModel;
import com.yaroslavgorbachh.counter.createEditCounter.CreateCounterDialogViewModel;
import com.yaroslavgorbachh.counter.createEditCounter.CreateEditCounterViewModel;
import com.yaroslavgorbachh.counter.di.ViewModelKey;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class SettingsModule {
    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel.class)
    abstract ViewModel bindDialogViewModel(SettingsViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(ColorPickerDialogViewModel.class)
    abstract ViewModel bindViewModel(ColorPickerDialogViewModel viewModel);
}
