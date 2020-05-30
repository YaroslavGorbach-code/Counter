package com.yaroslavgorbach.counter.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbach.counter.Database.Repo;
import com.yaroslavgorbach.counter.Models.CounterHistory;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private Repo mRepo;

    public HistoryViewModel(@NonNull Application application) {
        super(application);

        mRepo = new Repo(application);


    }

    public void insert(CounterHistory counterHistory){

        mRepo.insert(counterHistory);

    }

    public void delete(long counterId){

        mRepo.delete(counterId);

    }

    public void update(CounterHistory counterHistory){

        mRepo.update(counterHistory);

    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId){

        return mRepo.getCounterHistoryList(counterId);

    }

    public LiveData<List<CounterHistory>> getCounterHistoryListSortByValue(long counterId){

        return mRepo.getCounterHistoryListSortByValue(counterId);

    }

}
