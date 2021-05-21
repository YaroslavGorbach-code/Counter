package com.yaroslavgorbachh.counter.component.counter;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

public class CounterComponentImp implements CounterComponent {
    private final Repo mRepo;
    private final long mId;

    public CounterComponentImp(Repo repo, long id){
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
    public void resetCounter(ResetCallback callback){
        callback.onReset(mRepo.getCounterNoLiveData(mId).blockingGet());
        mRepo.resetCounter(mId);
    }


    public void delete(){
        mRepo.deleteCounter(mId);
        mRepo.deleteCounterHistory(mId);
    }

    @Override
    public void insert(Counter copy) {
        mRepo.insertCounter(copy);
    }

    @Override
    public LiveData<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }

}
