package com.yaroslavgorbachh.counter.component.counters;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class CountersImp implements Counters {
    private final Repo mRepo;
    private String mGroup;

    public CountersImp(Repo repo) {
        mRepo = repo;
    }

    @Override
    public List<Counter> sortCounters(List<Counter> counters) {
        return Observable.fromIterable(counters)
                .filter(counter -> (counter.grope != null && counter.grope.equals(mGroup)))
                .toList()
                .blockingGet();
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

        if (from.createDateSort != null && to.createDateSort != null) {
            dataFrom = from.createDateSort;
            dataTo = to.createDateSort;
        } else {
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
        return mRepo.getCounters();
    }

    @Override
    public void createCounter(String title, String group) {
        Counter counter = new Counter(title, 0, Counter.MAX_VALUE,
                Counter.MIN_VALUE, 1, group, new Date(),
                new Date(), null, 0, 0, 0, null);
        mRepo.insertCounter(counter);
    }

    @Override
    public void remove(List<Counter> counters) {
        for (Counter counter : counters) {
            mRepo.deleteCounter(counter.id);
        }
    }

    @Override
    public void reset(List<Counter> counters, ResetCallback resetCallback) {
        for (Counter counter : counters) {
            mRepo.resetCounter(counter.id);
        }
        resetCallback.onReset(counters);
    }

    @Override
    public void update(List<Counter> copy) {
        for (Counter counter : copy) {
            mRepo.updateCounter(counter);
        }
    }

    @Override
    public void decSelected(List<Counter> selected) {
        for (Counter counter : selected) {
            mRepo.decCounter(counter.id);
        }
    }

    @Override
    public void incSelected(List<Counter> selected) {
        for (Counter counter : selected) {
            mRepo.incCounter(counter.id);
        }
    }

    @Override
    public void setGroup(String group) {
        mGroup = group;
        mRepo.triggerCountersLiveData();
    }

    @Override
    public String getCurrentGroup() {
        return mGroup;
    }

}
