package com.yaroslavgorbachh.counter.aboutCounter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;

import javax.inject.Inject;

public class AboutCounterViewModel extends ViewModel {
    public LiveData<Counter> counter;
    private final Repo repo;

    @Inject
    public AboutCounterViewModel(@NonNull Repo repo) {
        this.repo = repo;
    }

    public void setCounterId(long counterId) {
        counter = repo.getCounter(counterId);
    }
}
