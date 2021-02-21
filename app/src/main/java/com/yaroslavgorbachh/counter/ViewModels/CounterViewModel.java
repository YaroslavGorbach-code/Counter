package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Objects;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

public class CounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private long mOldValue;
    public LiveData<Counter> counter;
    private final Accessibility mAccessibility;
    private final Resources mRes;

    public CounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        counter = mRepo.getCounter(counterId);
        mAccessibility = new Accessibility(application);
        mRes = application.getResources();
    }

    public void incCounter(View view) {
        long maxValue;
        long incOn;
        long value = Objects.requireNonNull(counter.getValue()).value;
        incOn = counter.getValue().step;
        maxValue = counter.getValue().maxValue;
        value += incOn;

        if (value > maxValue) {
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
            counter.getValue().value = maxValue;
        } else {
            counter.getValue().value = Math.max(counter.getValue().minValue, value);
        }
        if (counter.getValue().value == counter.getValue().minValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
        }

        if (counter.getValue().value > counter.getValue().counterMaxValue)
            counter.getValue().counterMaxValue = counter.getValue().value;

        if (counter.getValue().value < counter.getValue().counterMinValue)
            counter.getValue().counterMaxValue = counter.getValue().value;

        mAccessibility.playIncFeedback(view, String.valueOf(counter.getValue().value));
        mRepo.updateCounter(counter.getValue());
    }

    public void decCounter(View view){
        long minValue;
        long decOn;
        long value = Objects.requireNonNull(counter.getValue()).value;
        decOn = counter.getValue().step;
        minValue = counter.getValue().minValue;
        value -= decOn;

        if (value < minValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMinimum), Toast.LENGTH_SHORT).show();
            counter.getValue().value = minValue;
        }else {
            counter.getValue().value = Math.min(counter.getValue().maxValue, value);
        }
        if (counter.getValue().value == counter.getValue().maxValue){
            Toast.makeText(getApplication(), mRes.getString(R.string.thisIsMaximum), Toast.LENGTH_SHORT).show();
        }

        if (counter.getValue().value > counter.getValue().counterMaxValue)
            counter.getValue().counterMaxValue = counter.getValue().value;

        if (counter.getValue().value < counter.getValue().counterMinValue)
            counter.getValue().counterMinValue = counter.getValue().value;

        mAccessibility.playDecFeedback(view, String.valueOf(counter.getValue().value));
        mRepo.updateCounter(counter.getValue());
    }

    public void resetCounter(){
        mOldValue = Objects.requireNonNull(counter.getValue()).value;
        counter.getValue().lastResetValue = mOldValue;
        counter.getValue().lastResetDate = new Date();
        if (counter.getValue().minValue > 0){
            counter.getValue().value = counter.getValue().minValue;
        }else {
            counter.getValue().value = 0;
        }
        mAccessibility.speechOutput(String.valueOf(counter.getValue().value));
        mRepo.updateCounter(counter.getValue());
    }

    public void restoreValue(){
        Objects.requireNonNull(counter.getValue()).value = mOldValue;
        mAccessibility.speechOutput(String.valueOf(counter.getValue().value));
        mRepo.updateCounter(counter.getValue());
    }

    public void saveValueToHistory(){
        mRepo.insertCounterHistory(new CounterHistory(Objects.requireNonNull(
                counter.getValue()).value, Utility.formatDateToString(new Date()), counter.getValue().id));
        Toast.makeText(getApplication(), getApplication().getString(R.string.createEditCounterCounterValueHint) + " " +
                counter.getValue().value + " " + getApplication().getString(R.string.saveToHistoryToast), Toast.LENGTH_SHORT).show();
    }
    public void deleteCounter(){
        mRepo.deleteCounterHistory(Objects.requireNonNull(counter.getValue()).id);
        new Handler().postDelayed(() -> mRepo.deleteCounter(counter.getValue()),500);
    }



}
