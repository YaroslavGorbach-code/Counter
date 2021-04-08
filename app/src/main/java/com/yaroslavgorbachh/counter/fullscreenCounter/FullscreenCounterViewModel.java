package com.yaroslavgorbachh.counter.fullscreenCounter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.counterHistory.HistoryManager;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

public class FullscreenCounterViewModel extends ViewModel {
    public LiveData<Counter> counter;

    private final Repo mRepo;
    private final Accessibility mAccessibility;

    @Inject
    public FullscreenCounterViewModel(Repo repo, Accessibility accessibility) {
        mRepo = repo;
        mAccessibility = accessibility;
    }

    public void incCounter(Context context) {
        counter.getValue().inc(context, mRepo, mAccessibility);
    }

    public void decCounter(Context context){
        counter.getValue().dec(context, mRepo, mAccessibility);
    }

    public void setCounterId(Long id){
        counter = mRepo.getCounter(id);
    }

}
