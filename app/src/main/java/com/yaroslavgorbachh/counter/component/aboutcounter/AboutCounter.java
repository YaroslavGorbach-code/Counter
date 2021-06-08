package com.yaroslavgorbachh.counter.component.aboutcounter;

import com.yaroslavgorbachh.counter.data.domain.Counter;

import io.reactivex.rxjava3.core.Observable;

public interface AboutCounter {
    Observable<Counter> getCounter();
}
