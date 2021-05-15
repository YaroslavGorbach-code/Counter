package com.yaroslavgorbachh.counter.component.counter;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

public class CounterCompImp implements CounterComp {
    private Repo mRepo;
    private final long mId;


    public CounterCompImp(Repo repo, long id){
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

    public void resetCounter(){
        mRepo.resetCounter(mId);
    }

    public void undoReset(){

    }

    public void delete(){
        mRepo.deleteCounter(mId);
        mRepo.deleteCounterHistory(mId);
    }

    @Override
    public LiveData<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }

}
