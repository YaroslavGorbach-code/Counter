package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CreateEditCounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    public final LiveData<Counter> mCounter;
    public CreateEditCounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        mCounter = mRepo.getCounter(counterId);
    }

    public LiveData<List<String>> getGroups() {
       return mRepo.getGroups();
    }


    public void updateCreateCounter(String title, long value, long maxValue, long minValue, long step, String grope) {
        /*if mCounter == null insert counter*/
        if (mCounter.getValue() == null) {
            Date currentDate = new Date();
            currentDate.getTime();
            Counter newCounter = new Counter(title, value, maxValue, minValue, step, grope, currentDate);
            new Handler().postDelayed(() ->  mRepo.insertCounter(newCounter),500);
        } else {
            /*if mCounter != null update counter*/
            Objects.requireNonNull(mCounter.getValue()).value = value;
            Objects.requireNonNull(mCounter.getValue()).grope = grope;
            Objects.requireNonNull(mCounter.getValue()).maxValue = maxValue;
            Objects.requireNonNull(mCounter.getValue()).minValue = minValue;
            Objects.requireNonNull(mCounter.getValue()).step = step;
            Objects.requireNonNull(mCounter.getValue()).title = title;
        }
    }
}
