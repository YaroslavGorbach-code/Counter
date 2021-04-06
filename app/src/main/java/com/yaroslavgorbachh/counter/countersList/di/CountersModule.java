package com.yaroslavgorbachh.counter.countersList.di;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.counter.CounterViewModel;
import com.yaroslavgorbachh.counter.countersList.CountersViewModel;
import com.yaroslavgorbachh.counter.countersList.DragAndDrop.MultiSelection.CounterMultiSelection;
import com.yaroslavgorbachh.counter.countersList.DragAndDrop.MultiSelection.MultiCount;
import com.yaroslavgorbachh.counter.countersList.navigationDrawer.CounterDrawerMenuItemSelector;
import com.yaroslavgorbachh.counter.countersList.navigationDrawer.DrawerItemSelector;
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

    @Binds
    abstract MultiCount bindMultiCount(CounterMultiSelection counterMultiSelection);

    @Binds
    abstract DrawerItemSelector bindDrawerItemSelector(CounterDrawerMenuItemSelector counterDrawerMenuItemSelector);
}
