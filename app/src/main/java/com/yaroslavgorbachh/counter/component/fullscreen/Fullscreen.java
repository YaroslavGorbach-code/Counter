package com.yaroslavgorbachh.counter.component.fullscreen;

import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import io.reactivex.rxjava3.core.Observable;

public interface Fullscreen {
    void inc(Repo.ValueCallback callback);
    void dec(Repo.ValueCallback callback);
    Observable<Counter> getCounter();
}
