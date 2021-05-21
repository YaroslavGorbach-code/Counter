package com.yaroslavgorbachh.counter.component.counter;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import io.reactivex.rxjava3.core.Observable;

public interface CounterComponent {
    void incCounter();
    void decCounter();
    void resetCounter(ResetCallback resetCallback);
    void delete();
    void insert(Counter copy);
    Observable<Counter> getCounter();

    interface ResetCallback {
        void onReset(Counter copy);
    }
}
