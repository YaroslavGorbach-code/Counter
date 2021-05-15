package com.yaroslavgorbachh.counter.counterHistory;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;

import java.util.List;

import javax.inject.Inject;

public class CounterHistoryViewModel extends ViewModel {
    private final Repo repo;

    @Inject
    public CounterHistoryViewModel(Repo repo) {
        this.repo = repo;
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId) {
        return repo.getCounterHistoryList(counterId);
    }

    public void clean(long id) {
        repo.deleteCounterHistory(id);
    }

    public void deleteHistoryItem(CounterHistory counterHistory) {
        repo.deleteHistoryItem(counterHistory);
    }

    public void addHistoryItem(CounterHistory copy) {
        repo.insertCounterHistory(copy);
    }
}
