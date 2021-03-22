package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Date;
import java.util.Objects;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.CopyBeforeReset;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

public class CounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private  CopyBeforeReset mCopyBeforeReset;
    private final Accessibility mAccessibility;
    private final Resources mRes;
    public LiveData<Counter> counter;

    public CounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        counter = mRepo.getCounter(counterId);
        mAccessibility = new Accessibility(application);
        mRes = application.getResources();
    }

    public void incCounter(View view) {
        counter.getValue().inc(getApplication(), mRes, mRepo);
        mAccessibility.playIncFeedback(view, String.valueOf(counter.getValue().value));
    }

    public void decCounter(View view){
        counter.getValue().dec(getApplication(), mRes, mRepo);
        mAccessibility.playDecFeedback(view, String.valueOf(counter.getValue().value));
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
        Toast.makeText(getApplication(), getApplication().getString(R.string.createEditCounterCounterValueHint) + " " +
                counter.getValue().value + " " + getApplication().getString(R.string.saveToHistoryToast), Toast.LENGTH_SHORT).show();
    }
    public void deleteCounter(){
        mRepo.deleteCounterHistory(Objects.requireNonNull(counter.getValue()).id);
        new Handler().postDelayed(() -> mRepo.deleteCounter(counter.getValue()),500);
    }



}
