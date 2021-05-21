package com.yaroslavgorbachh.counter.component.aboutcounter;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

public interface AboutComponent {
    LiveData<Counter> getCounter();
}
