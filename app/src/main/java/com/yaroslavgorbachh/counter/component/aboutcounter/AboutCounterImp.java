package com.yaroslavgorbachh.counter.component.aboutcounter;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import io.reactivex.rxjava3.core.Observable;

public class AboutCounterImp implements AboutCounter {
    private final long mId;
    private final Repo repo;

    public AboutCounterImp(Repo repo, long id) {
        mId = id;
        this.repo = repo;
    }

    @Override
    public Observable<Counter> getCounter() {
        return repo.getCounter(mId);
    }
}
