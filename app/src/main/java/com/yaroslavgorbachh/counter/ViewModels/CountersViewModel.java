package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

import java.util.Date;
import java.util.List;

public class CountersViewModel extends AndroidViewModel {

    private final Repo mRepo;
    public CountersViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
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
        } else {
            counter.value = value;
            mRepo.updateCounter(counter);
        }
    }

    public void decCounter(Counter counter) {
        long minValue;
        long decOn;
        minValue = counter.minValue;
        long value = counter.value;
        decOn = counter.step;
        value -= decOn;

        if (value < minValue) {
            Toast.makeText(getApplication(), "This is minimum", Toast.LENGTH_SHORT).show();
        } else {
            counter.value = value;
            mRepo.updateCounter(counter);
        }
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

    public LiveData<List<Counter>> getAllCounters() {
        return mRepo.getAllCounters();
    }
}
