package com.yaroslavgorbachh.counter.component.counters;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.List;

public interface Counters {

    interface ResetCallback{
        void onReset(List<Counter> copy);
    }

    LiveData<List<String>> getGroups();
    void inc(long id);
    void dec(long id);
    void onMove(Counter from, Counter to);
    LiveData<List<Counter>> getCounters();
    void createCounter(String title, String group);
    void remove(List<Counter> counters);
    void reset(List<Counter> counters, ResetCallback callback);
    void update(List<Counter> copy);
    void decSelected(List<Counter> selected);
    void incSelected(List<Counter> selected);
    void setGroup(String group);
    String getCurrentGroup();
    List<Counter> getSortedCounters(List<Counter> counters);

}
