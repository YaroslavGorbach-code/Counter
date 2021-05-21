package com.yaroslavgorbachh.counter.component.aboutcounter;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import io.reactivex.rxjava3.core.Observable;

public interface AboutComponent {
    Observable<Counter> getCounter();
}
