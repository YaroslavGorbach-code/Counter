package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.Database.Models.CounterHistory;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private Repo mRepo;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
    }

    public void insert(CounterHistory counterHistory){
        mRepo.insertCounterHistory(counterHistory);
    }

    public void delete(long counterId){
        mRepo.deleteCounterHistory(counterId);
    }

    public void delete(CounterHistory counterHistory){
        mRepo.deleteCounterHistory(counterHistory);
    }

    public void update(CounterHistory counterHistory){
        mRepo.updateCounterHistory(counterHistory);
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId){
        return mRepo.getCounterHistoryList(counterId);
    }

    public LiveData<List<CounterHistory>> getCounterHistoryListSortByValue(long counterId){
        return mRepo.getCounterHistoryListSortByValue(counterId);
    }

}
