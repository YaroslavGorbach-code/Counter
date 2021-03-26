package com.yaroslavgorbachh.counter.fullscreenCounter;

import android.content.res.Resources;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

public class FullscreenCounterViewModel extends ViewModel {
    private final Repo mRepo;
    private final Accessibility mAccessibility;
    private final Resources mRes;
    public LiveData<Counter> counter;

    @Inject
    public FullscreenCounterViewModel(Repo repo, Resources resources, Accessibility accessibility) {
        mRepo = repo;
        mAccessibility = accessibility;
        mRes = resources;
    }

    public void incCounter(View view) {
        counter.getValue().inc(view.getContext(), mRes, mRepo, mAccessibility, view);
    }

    public void decCounter(View view){
        counter.getValue().dec(view.getContext(), mRes, mRepo, mAccessibility, view);
    }

    public void setCounterId(Long id){
        counter = mRepo.getCounter(id);
    }

}
