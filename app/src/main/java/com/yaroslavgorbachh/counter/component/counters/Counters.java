package com.yaroslavgorbachh.counter.component.counters;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.List;

public interface Counters {
    LiveData<List<String>> getGroups();
    void inc(long id);
    void dec(long id);
    void onMove(Counter from, Counter to);
    LiveData<List<Counter>> getCounters();
}
