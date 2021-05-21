package com.yaroslavgorbachh.counter.component.edit;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.List;

public interface EditComponent {
    LiveData<List<String>> getGroups();
    LiveData<Counter> getCounter();
    void updateCounter(Counter counter);

}
