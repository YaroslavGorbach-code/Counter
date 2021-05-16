package com.yaroslavgorbachh.counter.component.counters;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;

public class CountersImp implements Counters {
    private final Repo mRepo;
    public CountersImp(Repo repo) {
        mRepo = repo;
    }

    @Override
    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }

    @Override
    public void inc(long id) {
        mRepo.incCounter(id);
    }

    @Override
    public void dec(long id) {
        mRepo.decCounter(id);
    }

    @Override
    public void onMove(Counter from, Counter to) {
        Date dataFrom;
        Date dataTo;

        if (from.createDateSort!=null && to.createDateSort!=null){
            dataFrom = from.createDateSort;
            dataTo = to.createDateSort;
        }else {
            dataFrom = from.createDate;
            dataTo = to.createDate;
        }

        if (!dataFrom.equals(dataTo)) {
            to.createDateSort = dataFrom;
            mRepo.updateCounter(to);
            from.createDateSort = dataTo;
            mRepo.updateCounter(from);
        }
    }

    @Override
    public LiveData<List<Counter>> getCounters() {
        return mRepo.getAllCounters();
    }
}
