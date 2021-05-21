package com.yaroslavgorbachh.counter.component.fullscreen;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import io.reactivex.rxjava3.core.Observable;

public interface FullscreenComponent {
    void inc();

    void dec();

    Observable<Counter> getCounter();
}
