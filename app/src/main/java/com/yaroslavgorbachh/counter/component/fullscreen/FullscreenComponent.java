package com.yaroslavgorbachh.counter.component.fullscreen;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface FullscreenComponent {
    void inc();
    void dec();
    LiveData<Counter> getCounter();
}
