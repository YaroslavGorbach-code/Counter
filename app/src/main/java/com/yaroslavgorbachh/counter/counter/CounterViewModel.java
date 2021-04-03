package com.yaroslavgorbachh.counter.counter;

import android.content.Context;
import android.os.Handler;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Objects;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.CopyCounterBeforeReset;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

public class CounterViewModel extends ViewModel {
    private final Repo mRepo;
    private CopyCounterBeforeReset mCopyCounterBeforeReset;
    private final Accessibility mAccessibility;
    public LiveData<Counter> counter;

    @Inject
    public CounterViewModel(Repo repo, Accessibility accessibility) {
        mRepo = repo;
        mAccessibility = accessibility;
    }

    public void incCounter(Context context) {
        counter.getValue().inc(context, mRepo, mAccessibility);
    }

    public void decCounter(Context context){
        counter.getValue().dec(context, mRepo, mAccessibility);
    }

    public void resetCounter(){
        mCopyCounterBeforeReset = new CopyCounterBeforeReset();
        mCopyCounterBeforeReset.addCounter(counter.getValue());
        mAccessibility.speechOutput(String.valueOf(counter.getValue().value));
        counter.getValue().reset(mRepo);
    }

    public void restoreValue(){
        mAccessibility.speechOutput(String.valueOf(counter.getValue().value));
        mRepo.updateCounter(mCopyCounterBeforeReset.getCounters().get(0));
        mCopyCounterBeforeReset = null;
    }

    public void deleteCounter(){
        mRepo.deleteCounterHistory(Objects.requireNonNull(counter.getValue()).id);
        new Handler().postDelayed(() -> mRepo.deleteCounter(counter.getValue()),500);
    }

    public void setCounterId(long mCounterId) {
        counter = mRepo.getCounter(mCounterId);
    }
}
