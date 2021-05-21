package com.yaroslavgorbachh.counter.component.edit;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface EditComponent {
    LiveData<List<String>> getGroups();
    Observable<Counter> getCounter();
    void updateCounter(Counter counter);

}
