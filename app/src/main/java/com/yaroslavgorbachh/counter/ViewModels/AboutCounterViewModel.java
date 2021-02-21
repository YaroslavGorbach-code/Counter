package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

public class AboutCounterViewModel extends AndroidViewModel {
    public LiveData<Counter> counter;
    public AboutCounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        Repo mRepo = new Repo(application);
        counter = mRepo.getCounter(counterId);
    }

}
