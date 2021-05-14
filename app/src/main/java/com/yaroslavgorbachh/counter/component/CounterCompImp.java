package com.yaroslavgorbachh.counter.component;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.database.Models.Counter;

import java.util.List;

public class CounterCompImp implements CounterComp {

    @Override
    public void inc() {

    }

    @Override
    public void dec() {

    }

    @Override
    public LiveData<List<Counter>> getCounters() {
        return null;
    }
}
