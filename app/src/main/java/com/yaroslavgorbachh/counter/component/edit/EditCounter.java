package com.yaroslavgorbachh.counter.component.edit;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.List;

public interface EditCounter {
    LiveData<List<String>> getGroups();
    LiveData<Counter> getCounter();
    void editCounter(String title, long value, long maxValue, long minValue, long step, String grope);

}
