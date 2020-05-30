package com.yaroslavgorbach.counter.Database.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbach.counter.Database.Repo;
import com.yaroslavgorbach.counter.Models.Counter;

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

    public void setValue(Counter counter, long value){

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
