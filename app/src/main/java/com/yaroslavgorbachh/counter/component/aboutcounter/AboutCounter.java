package com.yaroslavgorbachh.counter.component.aboutcounter;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface AboutCounter {
    LiveData<Counter> getCounter();
}
