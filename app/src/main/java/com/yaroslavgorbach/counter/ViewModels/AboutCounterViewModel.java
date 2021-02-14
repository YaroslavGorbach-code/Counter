package com.yaroslavgorbach.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Repo;

public class AboutCounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    public LiveData<Counter> counter;
    public AboutCounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        counter = mRepo.getCounter(counterId);
    }

}
