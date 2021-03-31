package com.yaroslavgorbachh.counter.counter;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.preference.PreferenceManager;

import java.util.Date;
import java.util.Objects;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.CopyBeforeReset;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

import javax.inject.Inject;

public class CounterViewModel extends ViewModel {
    private final Repo mRepo;
    private  CopyBeforeReset mCopyBeforeReset;
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
        mCopyBeforeReset = new CopyBeforeReset();
        mCopyBeforeReset.addCounter(counter.getValue());
        mAccessibility.speechOutput(String.valueOf(counter.getValue().value));
        counter.getValue().reset(mRepo);
    }

    public void restoreValue(){
        mAccessibility.speechOutput(String.valueOf(counter.getValue().value));
        mRepo.updateCounter(mCopyBeforeReset.getCounters().get(0));
        mCopyBeforeReset = null;
    }

    public void saveValueToHistory(){
        mRepo.insertCounterHistory(new CounterHistory(Objects.requireNonNull(
                counter.getValue()).value, Utility.convertDateToString(new Date()), counter.getValue().id));
    }

    public void deleteCounter(){
        mRepo.deleteCounterHistory(Objects.requireNonNull(counter.getValue()).id);
        new Handler().postDelayed(() -> mRepo.deleteCounter(counter.getValue()),500);
    }

    public void setCounterId(long mCounterId) {
        counter = mRepo.getCounter(mCounterId);
    }
}
