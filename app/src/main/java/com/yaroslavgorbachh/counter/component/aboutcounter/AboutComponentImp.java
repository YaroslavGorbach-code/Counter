package com.yaroslavgorbachh.counter.component.aboutcounter;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import io.reactivex.rxjava3.core.Observable;

public class AboutComponentImp implements AboutComponent {
    private final long mId;
    private final Repo repo;

    public AboutComponentImp(Repo repo, long id) {
        mId = id;
        this.repo = repo;
    }

    @Override
    public Observable<Counter> getCounter() {
        return repo.getCounter(mId);
    }
}
