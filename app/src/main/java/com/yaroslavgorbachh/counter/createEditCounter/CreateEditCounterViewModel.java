package com.yaroslavgorbachh.counter.createEditCounter;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class CreateEditCounterViewModel extends ViewModel {
    private final Repo mRepo;
    private final LiveData<List<String>> mGroups;
    private LiveData<Counter> mCounter;

    @Inject
    public CreateEditCounterViewModel(Repo repo) {
        mRepo = repo;
        mGroups = mRepo.getGroups();
    }

    public LiveData<List<String>> getGroups() {
       return mGroups;
    }

    public LiveData<Counter> getCounter(){
        return mCounter;
    }

    public void setCounterId(Long id){
        mCounter = mRepo.getCounter(id);
    }


    public void updateCreateCounter(String title, long value, long maxValue, long minValue, long step, String grope) {
        if (value > maxValue){
            value = maxValue;
        }
        if (value < minValue){
            value = minValue;
        }
        /*if mCounter == null insert counter*/
        if (mCounter.getValue() == null) {
            Counter newCounter = new Counter(title, value, maxValue, minValue, step, grope,
                    new Date(), new Date(), null, 0, 0, 0, null);
            new Handler().postDelayed(() ->  mRepo.insertCounter(newCounter),500);
        } else {
            /*if mCounter != null update counter*/
            Objects.requireNonNull(mCounter.getValue()).value = value;
            Objects.requireNonNull(mCounter.getValue()).grope = grope;
            Objects.requireNonNull(mCounter.getValue()).maxValue = maxValue;
            Objects.requireNonNull(mCounter.getValue()).minValue = minValue;
            Objects.requireNonNull(mCounter.getValue()).step = step;
            Objects.requireNonNull(mCounter.getValue()).title = title;
            mRepo.updateCounter(mCounter.getValue());
        }
    }
}
