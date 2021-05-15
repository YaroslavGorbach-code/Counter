package com.yaroslavgorbachh.counter.component;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.database.Models.Counter;

public interface CounterComp {
    void incCounter();
    void decCounter();
    void resetCounter();
    void undoReset();
    void delete();
    LiveData<Counter> getCounter();
}
