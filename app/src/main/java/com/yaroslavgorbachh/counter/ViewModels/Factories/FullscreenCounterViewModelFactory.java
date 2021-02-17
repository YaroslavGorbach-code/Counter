package com.yaroslavgorbachh.counter.ViewModels.Factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yaroslavgorbachh.counter.ViewModels.AboutCounterViewModel;
import com.yaroslavgorbachh.counter.ViewModels.FullscreenCounterViewModel;

public class FullscreenCounterViewModelFactory extends ViewModelProvider.NewInstanceFactory{

    private final long id;
    private final Application application;

    public FullscreenCounterViewModelFactory(Application application, long id){
        super();
        this.id = id;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(FullscreenCounterViewModel.class)){
            return (T) new FullscreenCounterViewModel(application, id);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
