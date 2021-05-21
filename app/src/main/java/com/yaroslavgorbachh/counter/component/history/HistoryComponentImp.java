package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

public class HistoryComponentImp implements HistoryComponent {
    private final Repo mRepo;
    private final long mId;
    public HistoryComponentImp(Repo repo, long id){
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
