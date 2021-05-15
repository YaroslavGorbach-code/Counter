package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.CounterHistory;

import java.util.List;

public interface History {
    LiveData<List<CounterHistory>> getHistory();
    void clean();
}
