package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

public class FullscreenCounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    public LiveData<Counter> mCounter;
    private final Accessibility mAccessibility;
    private final Resources mRes;

    public FullscreenCounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        mCounter = mRepo.getCounter(counterId);
        // TODO: 3/24/2021 inject
        mAccessibility = new Accessibility(application, PreferenceManager.getDefaultSharedPreferences(application));
        mRes = application.getResources();
    }

    public void incCounter(View view) {
        mCounter.getValue().inc(view.getContext(), mRes, mRepo, mAccessibility, view);
    }

    public void decCounter(View view){
        mCounter.getValue().dec(view.getContext(), mRes, mRepo, mAccessibility, view);
    }

}
