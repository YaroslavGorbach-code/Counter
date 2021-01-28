package com.yaroslavgorbachh.counter.ViewModels.Factories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import com.yaroslavgorbachh.counter.ViewModels.CounterViewModel;
import com.yaroslavgorbachh.counter.ViewModels.CounterViewModel_new;

public class CounterViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private long id;
    private Application application;
   public CounterViewModelFactory(Application application, long id){
        super();
        this.id = id;
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(CounterViewModel_new.class)){
            return (T) new CounterViewModel_new(application, id);
        }else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
