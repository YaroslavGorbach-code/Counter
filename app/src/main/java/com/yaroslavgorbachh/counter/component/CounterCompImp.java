package com.yaroslavgorbachh.counter.component;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.CopyCounterBeforeReset;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;



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
        return null;
    }

}
