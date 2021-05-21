package com.yaroslavgorbachh.counter.component.fullscreen;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface Fullscreen {
    void inc();
    void dec();
    LiveData<Counter> getCounter();
}
