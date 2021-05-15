package com.yaroslavgorbachh.counter.component.counter;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface CounterComp {
    void incCounter();
    void decCounter();
    void resetCounter();
    void undoReset();
    void delete();
    LiveData<com.yaroslavgorbachh.counter.data.Models.Counter> getCounter();
}
