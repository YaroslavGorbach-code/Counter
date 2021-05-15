package com.yaroslavgorbachh.counter.component.edit;

import android.os.Handler;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class EditCounterImp implements EditCounter {

    private final Repo mRepo;
    private final long mId;

    public EditCounterImp(Repo repo, long id) {
        mRepo = repo;
        mId = id;
    }

    @Override
    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }

    @Override
    public LiveData<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }

    @Override
    public void editCounter(String title, long value, long maxValue, long minValue, long step, String grope) {
        if (value > maxValue) {
            value = maxValue;
        }
        if (value < minValue) {
            value = minValue;
        }
        /*if mCounter == null insert counter*/
        if (getCounter().getValue() == null) {
            Counter newCounter = new Counter(title, value, maxValue, minValue, step, grope,
                    new Date(), new Date(), null, 0, 0, 0, null);
            new Handler().postDelayed(() -> mRepo.insertCounter(newCounter), 500);
        } else {
            /*if mCounter != null update counter*/
            Objects.requireNonNull(getCounter().getValue()).value = value;
            Objects.requireNonNull(getCounter().getValue()).grope = grope;
            Objects.requireNonNull(getCounter().getValue()).maxValue = maxValue;
            Objects.requireNonNull(getCounter().getValue()).minValue = minValue;
            Objects.requireNonNull(getCounter().getValue()).step = step;
            Objects.requireNonNull(getCounter().getValue()).title = title;
            mRepo.updateCounter(getCounter().getValue());
        }
    }
}
