package com.yaroslavgorbachh.counter.screen.createEditCounter;

import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

public class CreateEditCounterViewModel extends ViewModel {
    private final Repo repo;
    private final LiveData<List<String>> mGroups;
    private LiveData<Counter> mCounter;

    @Inject
    public CreateEditCounterViewModel(Repo repo) {
        this.repo = repo;
        mGroups = repo.getGroups();
    }

    public LiveData<List<String>> getGroups() {
       return mGroups;
    }

    public LiveData<Counter> getCounter(){
        return mCounter;
    }

    public void setCounterId(Long id){
        mCounter = repo.getCounter(id);
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
            new Handler().postDelayed(() ->  repo.insertCounter(newCounter),500);
        } else {
            /*if mCounter != null update counter*/
            Objects.requireNonNull(mCounter.getValue()).value = value;
            Objects.requireNonNull(mCounter.getValue()).grope = grope;
            Objects.requireNonNull(mCounter.getValue()).maxValue = maxValue;
            Objects.requireNonNull(mCounter.getValue()).minValue = minValue;
            Objects.requireNonNull(mCounter.getValue()).step = step;
            Objects.requireNonNull(mCounter.getValue()).title = title;
            repo.updateCounter(mCounter.getValue());
        }
    }
}
