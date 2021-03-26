package com.yaroslavgorbachh.counter.aboutCounter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

public class AboutCounterViewModel extends ViewModel {
    public LiveData<Counter> counter;
    Repo mRepo;

    @Inject
    public AboutCounterViewModel(@NonNull Repo repo) {
        mRepo = repo;
    }

    public void setCounterId(long counterId) {
        counter = mRepo.getCounter(counterId);
    }
}
