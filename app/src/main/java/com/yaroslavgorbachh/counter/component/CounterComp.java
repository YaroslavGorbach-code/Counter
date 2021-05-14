package com.yaroslavgorbachh.counter.component;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.database.Models.Counter;

import java.util.List;

public interface CounterComp {
    void inc();
    void dec();
    LiveData<List<Counter>> getCounters();
}
