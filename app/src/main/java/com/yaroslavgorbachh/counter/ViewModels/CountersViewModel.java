package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

import java.util.Date;
import java.util.List;

public class CountersViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private final Resources mRes;
    public LiveData<List<Counter>> mCounters;

    public CountersViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
        mCounters = mRepo.getAllCounters();
        mRes = application.getResources();
    }

    public void incCounter(Counter counter) {
        counter.inc(getApplication(), mRes, mRepo);
    }

    public void decCounter(Counter counter) {
        counter.dec(getApplication(), mRes, mRepo);
    }

    public void countersMoved(Counter counterFrom, Counter counterTo) {
        Date dataFrom;
        Date dataTo;

        if (counterFrom.createDateSort!=null && counterTo.createDateSort!=null){
            dataFrom = counterFrom.createDateSort;
            dataTo = counterTo.createDateSort;
        }else {
            dataFrom = counterFrom.createDate;
            dataTo = counterTo.createDate;
        }

        if (!dataFrom.equals(dataTo)) {
            counterTo.createDateSort = dataFrom;
            mRepo.updateCounter(counterTo);
            counterFrom.createDateSort = dataTo;
            mRepo.updateCounter(counterFrom);
        }

    }

    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }
}
