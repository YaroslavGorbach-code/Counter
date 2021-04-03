package com.yaroslavgorbachh.counter.counterHistory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.List;

import javax.inject.Inject;

public class CounterHistoryViewModel extends ViewModel {
    private final Repo mRepo;

    @Inject
    public CounterHistoryViewModel(Repo repo) {
        mRepo = repo;
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId) {
        return mRepo.getCounterHistoryList(counterId);
    }

    public void clean(long id) {
        mRepo.deleteCounterHistory(id);
    }

    public void deleteHistoryItem(CounterHistory counterHistory) {
        mRepo.deleteHistoryItem(counterHistory);
    }

    public void addHistoryItem(CounterHistory copy) {
        mRepo.insertCounterHistory(copy);
    }
}
