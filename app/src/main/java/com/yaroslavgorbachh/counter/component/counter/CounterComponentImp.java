package com.yaroslavgorbachh.counter.component.counter;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import io.reactivex.rxjava3.core.Observable;

public class CounterComponentImp implements CounterComponent {
    private final Repo mRepo;
    private final long mId;

    public CounterComponentImp(Repo repo, long id) {
        mRepo = repo;
        mId = id;
    }

    @Override
    public void incCounter() {
        mRepo.incCounter(mId);
    }

    @Override
    public void decCounter() {
        mRepo.decCounter(mId);
    }

    @Override
    public void resetCounter(ResetCallback callback) {
        callback.onReset(mRepo.getCounter(mId).blockingFirst());
        mRepo.resetCounter(mId);
    }


    public void delete() {
        mRepo.deleteCounter(mId);
        mRepo.removeHistory(mId);
    }

    @Override
    public void insert(Counter copy) {
        mRepo.createCounter(copy);
    }

    @Override
    public Observable<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }

}
