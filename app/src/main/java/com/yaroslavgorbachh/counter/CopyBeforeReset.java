package com.yaroslavgorbachh.counter;

import com.yaroslavgorbachh.counter.Database.Models.Counter;

import java.util.ArrayList;
import java.util.List;

public class CopyBeforeReset {
    private final List<Counter> mCopyList;

    public CopyBeforeReset(){
        mCopyList = new ArrayList<>();
    }

    public void addCounter(Counter counter){
        Counter copy = new Counter(counter.title, counter.value,
                counter.maxValue, counter.minValue, counter.step,
                counter.grope, counter.createDate, counter.createDateSort,
                counter.lastResetDate, counter.lastResetValue,
                counter.counterMaxValue, counter.counterMinValue);
        copy.setId(counter.id);
        mCopyList.add(copy);
    }


    public List<Counter> getCounters(){
        return mCopyList;
    }
}
