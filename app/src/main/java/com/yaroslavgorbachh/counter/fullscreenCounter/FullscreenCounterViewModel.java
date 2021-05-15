package com.yaroslavgorbachh.counter.fullscreenCounter;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;

import javax.inject.Inject;

public class FullscreenCounterViewModel extends ViewModel {
    public LiveData<Counter> counter;

    private final Repo repo;
    private final Accessibility mAccessibility;

    @Inject
    public FullscreenCounterViewModel(Repo repo, Accessibility accessibility) {
        this.repo = repo;
        mAccessibility = accessibility;
    }

    public void incCounter(Context context) {
        repo.incCounter(counter.getValue().id);
    }

    public void decCounter(Context context){
        repo.decCounter(counter.getValue().id);
    }

    public void setCounterId(Long id){
        counter = repo.getCounter(id);
    }

}
