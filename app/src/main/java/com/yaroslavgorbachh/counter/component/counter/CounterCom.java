package com.yaroslavgorbachh.counter.component.counter;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import io.reactivex.rxjava3.core.Observable;

public interface CounterCom {
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
