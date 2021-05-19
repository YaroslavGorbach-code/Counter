package com.yaroslavgorbachh.counter.component.counter;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface CounterComp {

    interface ResetCallback{
        void onReset(Counter copy);
    }
    void incCounter();
    void decCounter();
    void resetCounter(ResetCallback resetCallback);
    void delete();
    void insert(Counter copy);
    LiveData<Counter> getCounter();
}
