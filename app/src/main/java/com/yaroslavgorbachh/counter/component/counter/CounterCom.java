package com.yaroslavgorbachh.counter.component.counter;

import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import io.reactivex.rxjava3.core.Observable;

public interface CounterCom {
    void incCounter(Repo.ValueCallback callback);
    void decCounter(Repo.ValueCallback callback);
    void resetCounter(ResetCallback resetCallback);
    void delete();
    void insert(Counter copy);
    int getFastCountInterval();
    Observable<Counter> getCounter();

    interface ResetCallback {
        void onReset(Counter copy);
    }
}
