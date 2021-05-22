package com.yaroslavgorbachh.counter.component.aboutcounter;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import io.reactivex.rxjava3.core.Observable;

public interface AboutComponent {
    Observable<Counter> getCounter();
}
