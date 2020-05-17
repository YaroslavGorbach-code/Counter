package com.yaroslavgorbach.counter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class CounterViewModel extends AndroidViewModel {

    private Repo mRepo;
    private LiveData<List<Counter>> mAllCounters;
    private LiveData<List<String>> mGroups;

    public CounterViewModel(@NonNull Application application) {
        super(application);

        mRepo = new Repo(application);
        mAllCounters = mRepo.getAllCounters();
        mGroups = mRepo.getGroups();


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

    public void insert(CounterHistory counterHistory){

        mRepo.insert(counterHistory);

    }

    public void delete(CounterHistory counterHistory){

        mRepo.delete(counterHistory);

    }

    public void update(CounterHistory counterHistory){

        mRepo.update(counterHistory);

    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId){

        return mRepo.getCounterHistoryList(counterId);

    }

    public LiveData<CounterHistory> getCounterHistory(long counterId, String data){

        return mRepo.getCounterHistory(counterId, data);

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

    public LiveData<List<Counter>> getCountersByGroup(String group){

        return mRepo.getCountersByGroup(group);

    }

    public LiveData<List<String>> getGroups(){

        return mGroups;

    }

}
