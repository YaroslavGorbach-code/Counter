package com.yaroslavgorbachh.counter.counterHistory;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.List;

public class CounterHistoryViewModel extends ViewModel {
   // private final Repo mRepo;

    public CounterHistoryViewModel() {
            //mRepo = new Repo();
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId) {
       // return mRepo.getCounterHistoryList(counterId);
        return null;
    }

    public void clean(long id) {
            //mRepo.deleteCounterHistory(id);
    }
}
