package com.yaroslavgorbach.counter.ViewModels;

import android.app.Application;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Models.CounterHistory;
import com.yaroslavgorbach.counter.Database.Repo;
import com.yaroslavgorbach.counter.R;

public class CounterViewModel extends AndroidViewModel {
    private final Repo mRepo;
    private long mOldValue;
    public LiveData<Counter> mCounter;

    public CounterViewModel(@NonNull Application application, long counterId) {
        super(application);
        mRepo = new Repo(application);
        mCounter = mRepo.getCounter(counterId);
    }

    public void incCounter() {
        long maxValue;
        long incOn;
        long minValue;
        long value = Objects.requireNonNull(mCounter.getValue()).value;
        incOn = mCounter.getValue().step;
        maxValue = mCounter.getValue().maxValue;
        minValue = mCounter.getValue().minValue;
        value += incOn;

        if (value > maxValue) {
            Toast.makeText(getApplication(), "This is maximum", Toast.LENGTH_SHORT).show();
            mCounter.getValue().value = maxValue;
        } else {
            mCounter.getValue().value = Math.max(minValue, value);
        }
        if (mCounter.getValue().value == minValue){
            Toast.makeText(getApplication(), "This is minimum", Toast.LENGTH_SHORT).show();
        }
        mRepo.updateCounter(mCounter.getValue());
    }

    public void decCounter(){
        long minValue;
        long decOn;
        long maxValue;
        long value = Objects.requireNonNull(mCounter.getValue()).value;
        decOn = mCounter.getValue().step;
        minValue = mCounter.getValue().minValue;
        maxValue = mCounter.getValue().maxValue;
        value -= decOn;

        if (value < minValue){
            Toast.makeText(getApplication(), "This is minimum", Toast.LENGTH_SHORT).show();
            mCounter.getValue().value = minValue;
        }else {
            mCounter.getValue().value = Math.min(maxValue, value);
        }
        if (mCounter.getValue().value == maxValue){
            Toast.makeText(getApplication(), "This is maximum", Toast.LENGTH_SHORT).show();
        }
        mRepo.updateCounter(mCounter.getValue());
    }

    public void resetCounter(){
        mOldValue = Objects.requireNonNull(mCounter.getValue()).value;
        if (mCounter.getValue().minValue > 0){
            mCounter.getValue().value = mCounter.getValue().minValue;
        }else {
            mCounter.getValue().value = 0;
        }
        mRepo.updateCounter(mCounter.getValue());
    }

    public void restoreValue(){
        Objects.requireNonNull(mCounter.getValue()).value = mOldValue;
        mRepo.updateCounter(mCounter.getValue());
    }

    public void saveValueToHistory(){
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss", Locale.getDefault());
        String date = dateFormat.format(currentDate);
        mRepo.insertCounterHistory(new CounterHistory(Objects.requireNonNull(
                mCounter.getValue()).value, date, mCounter.getValue().id));
        Toast.makeText(getApplication(), getApplication().getString(R.string.CreateEditCounterCounterValueHint) + " " +
                mCounter.getValue().value + " " + getApplication().getString(R.string.SaveToHistoryToast), Toast.LENGTH_SHORT).show();
    }
    public void deleteCounter(){
        mRepo.deleteCounterHistory(Objects.requireNonNull(mCounter.getValue()).id);
        new Handler().postDelayed(() -> mRepo.deleteCounter(mCounter.getValue()),500);
    }

}
