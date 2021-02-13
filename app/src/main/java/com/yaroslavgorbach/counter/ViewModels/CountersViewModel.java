package com.yaroslavgorbach.counter.ViewModels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Repo;

import java.util.Date;
import java.util.List;

public class CountersViewModel extends AndroidViewModel {

    private final Repo mRepo;
    public LiveData<List<Counter>> mCounters;
    public CountersViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
        mCounters = mRepo.getAllCounters();
    }

    public void incCounter(Counter counter) {
        long maxValue;
        long incOn;
        long value = counter.value;
        maxValue = counter.maxValue;
        incOn = counter.step;
        value += incOn;

        if (value > maxValue) {
            Toast.makeText(getApplication(), "This is maximum", Toast.LENGTH_SHORT).show();
            counter.value = maxValue;
        } else {
            counter.value = Math.max(counter.minValue, value);
        }
        if (counter.value == counter.minValue){
            Toast.makeText(getApplication(), "This is minimum", Toast.LENGTH_SHORT).show();
        }
        mRepo.updateCounter(counter);
    }

    public void decCounter(Counter counter) {
        long minValue;
        long decOn;
        minValue = counter.minValue;
        long value = counter.value;
        decOn = counter.step;
        value -= decOn;

        if (value < minValue){
            Toast.makeText(getApplication(), "This is minimum", Toast.LENGTH_SHORT).show();
            counter.value = minValue;
        }else {
            counter.value = Math.min(counter.maxValue, value);
        }
        if (counter.value == counter.maxValue){
            Toast.makeText(getApplication(), "This is maximum", Toast.LENGTH_SHORT).show();
        }
        mRepo.updateCounter(counter);
    }

    public void countersMoved(Counter counterFrom, Counter counterTo) {
        Date dataFrom = counterFrom.createData;
        Date dataTo = counterTo.createData;
        if (!dataFrom.equals(dataTo)) {
            counterTo.createData = dataFrom;
            mRepo.updateCounter(counterTo);

            counterFrom.createData = dataTo;
            mRepo.updateCounter(counterFrom);
        }
    }

    public LiveData<List<Counter>> getCountersByGroup(String group_title) {
        return mRepo.getCountersByGroup(group_title);
    }

    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }
}
