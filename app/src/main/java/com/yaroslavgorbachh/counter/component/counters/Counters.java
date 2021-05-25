package com.yaroslavgorbachh.counter.component.counters;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface Counters {
    interface ResetCallback{
        void onReset(List<Counter> copy);
    }

    LiveData<List<String>> getGroups();
    void inc(Counter counter);
    void dec(Counter counter);
    void onMove(Counter from, Counter to);
    Observable<List<Counter>> getCounters();
    void createCounter(String title, String group);
    void remove(List<Counter> counters);
    void reset(List<Counter> counters, ResetCallback callback);
    void update(List<Counter> copy);
    void decSelected(List<Counter> selected);
    void incSelected(List<Counter> selected);
    void setGroup(String group);
    void onLoverVolume();
    void onRaiseVolume();
    int getFastCountInterval();
    String getCurrentGroup();
    List<Counter> sortCounters(List<Counter> counters);

}
