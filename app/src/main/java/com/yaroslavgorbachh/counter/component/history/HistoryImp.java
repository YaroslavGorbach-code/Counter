package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

public class HistoryImp implements History{
    private final Repo mRepo;
    private final long mId;
    public HistoryImp(Repo repo, long id){
        mRepo = repo;
        mId = id;
    }
    @Override
    public LiveData<List<CounterHistory>> getHistory() {
        return mRepo.getCounterHistoryList(mId);
    }

    @Override
    public void clean() {
        mRepo.deleteCounterHistory(mId);
    }
}
