package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;

import java.util.Objects;

public class FullscreenCounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    public LiveData<Counter> mCounter;
    private final Accessibility mAccessibility;
    private final Resources mRes;

    public FullscreenCounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        mCounter = mRepo.getCounter(counterId);
        mAccessibility = new Accessibility(application);
        mRes = application.getResources();
    }

    public void incCounter(View view) {
        mCounter.getValue().inc(getApplication(), mRes, mRepo);
        mAccessibility.playIncFeedback(view, String.valueOf(mCounter.getValue().value));
    }

    public void decCounter(View view){
        mCounter.getValue().dec(getApplication(), mRes, mRepo);
        mAccessibility.playDecFeedback(view, String.valueOf(mCounter.getValue().value));
    }

}
