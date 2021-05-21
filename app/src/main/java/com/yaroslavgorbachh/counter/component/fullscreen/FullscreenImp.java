package com.yaroslavgorbachh.counter.component.fullscreen;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

public class FullscreenImp implements Fullscreen {
    private final Repo mRepo;
    private final long mId;
    public FullscreenImp(Repo repo, long id){
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
    public LiveData<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }
}