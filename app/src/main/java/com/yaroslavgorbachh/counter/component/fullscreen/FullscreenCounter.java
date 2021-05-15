package com.yaroslavgorbachh.counter.component.fullscreen;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface FullscreenCounter {
    void inc();
    void dec();
    LiveData<Counter> getCounter();
}
