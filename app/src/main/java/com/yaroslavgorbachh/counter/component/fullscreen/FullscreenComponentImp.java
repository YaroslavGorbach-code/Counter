package com.yaroslavgorbachh.counter.component.fullscreen;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import io.reactivex.rxjava3.core.Observable;

public class FullscreenComponentImp implements FullscreenComponent {
    private final Repo mRepo;
    private final long mId;
    public FullscreenComponentImp(Repo repo, long id){
        mRepo = repo;
        mId = id;
    }
    @Override
    public void inc() {
        mRepo.incCounter(mId);
    }

    @Override
    public void dec() {
        mRepo.decCounter(mId);
    }

    @Override
    public Observable<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }
}
