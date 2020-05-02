package com.yaroslavgorbach.counter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CounterViewModel extends AndroidViewModel {

    private Repo mRepo;
    private LiveData<List<Counter>> mAllCounters;

    public CounterViewModel(@NonNull Application application) {
        super(application);

        mRepo = new Repo(application);
        mAllCounters = mRepo.getAllCounters();


    }

    public void insert(Counter counter){

        mRepo.insert(counter);

    }

    public void delete(Counter counter){

        mRepo.delete(counter);

    }

    public void update(Counter counter){

        mRepo.update(counter);

    }

    public void setValue(Counter counter, int value){

        counter.value = value;
        mRepo.update(counter);

    }

    public LiveData<Counter> getCounter(long id){

      return mRepo.getCounter(id);
    }

    public LiveData<List<Counter>> getAllCounters(){

        return mAllCounters;

    }
}
