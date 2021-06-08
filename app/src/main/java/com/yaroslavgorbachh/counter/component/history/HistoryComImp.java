package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

public class HistoryComImp implements HistoryCom {
    private final Repo mRepo;
    private final long mId;
    public HistoryComImp(Repo repo, long id){
        mRepo = repo;
        mId = id;
    }
    @Override
    public LiveData<List<com.yaroslavgorbachh.counter.data.domain.History>> getHistory() {
        return mRepo.getHistoryList(mId);
    }

    @Override
    public void clean() {
        mRepo.removeCounterHistory(mId);
    }

    @Override
    public void remove(com.yaroslavgorbachh.counter.data.domain.History history) {
        mRepo.removeHistoryItem(history.id);
    }

    @Override
    public void addItem(com.yaroslavgorbachh.counter.data.domain.History item) {
        mRepo.addHistory(item);
    }
}
