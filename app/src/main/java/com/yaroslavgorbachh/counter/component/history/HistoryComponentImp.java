package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.History;
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
    public LiveData<List<History>> getHistory() {
        return mRepo.getHistoryList(mId);
    }

    @Override
    public void clean() {
        mRepo.removeCounterHistory(mId);
    }

    @Override
    public void remove(History history) {
        mRepo.removeHistoryItem(history.id);
    }

    @Override
    public void addItem(History item) {
        mRepo.addHistory(item);
    }
}
