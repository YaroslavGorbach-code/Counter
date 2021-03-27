package com.yaroslavgorbachh.counter.countersList;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CountersViewModel extends ViewModel {
    private final Repo mRepo;
    private final Resources mRes;
    private final LiveData<List<Counter>> mCounters;
    private final LiveData<List<String>> mGroups;

    @Inject
    public CountersViewModel(Repo repo, Resources resources) {
        mRepo = repo;
        mRes = resources;
        mCounters = mRepo.getAllCounters();
        mGroups = mRepo.getGroups();
    }

    public void incCounter(Counter counter, Accessibility accessibility, Context context) {
        counter.inc(context, mRes, mRepo, accessibility);
    }

    public void decCounter(Counter counter, Accessibility accessibility, Context context) {
        counter.dec(context, mRes, mRepo, accessibility);
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
