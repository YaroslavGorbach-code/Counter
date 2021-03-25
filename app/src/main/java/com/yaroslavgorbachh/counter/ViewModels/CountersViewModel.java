package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Date;
import java.util.List;

public class CountersViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private final Resources mRes;
    private final LiveData<List<Counter>> mCounters;
    private final LiveData<List<String>> mGroups;

    public CountersViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
        mRes = application.getResources();
        mCounters = mRepo.getAllCounters();
        mGroups = mRepo.getGroups();
    }

    public void incCounter(Counter counter, Accessibility accessibility, View view) {
        counter.inc(view.getContext(), mRes, mRepo, accessibility, view);
    }

    public void decCounter(Counter counter, Accessibility accessibility, View view) {
        counter.dec(view.getContext(), mRes, mRepo, accessibility, view);
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
        return mGroups;
    }

    public LiveData<List<Counter>> getCounters(){
         return mCounters;
    }
}
