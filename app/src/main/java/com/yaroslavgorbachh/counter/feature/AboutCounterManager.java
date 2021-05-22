package com.yaroslavgorbachh.counter.feature;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.Date;

public class AboutCounterManager {

    public static Counter updateMaxCounterValue(Counter counter){
        if (counter.value > counter.counterMaxValue){
            counter.counterMaxValue = counter.value;
        }
        return counter;
    }

    public static Counter updateMinCounterValue(Counter counter) {
        if (counter.value < counter.counterMinValue){
            counter.counterMinValue = counter.value;
        }
        return counter;
    }

    public static Counter updateValueBeforeReset(Counter counter) {
        counter.lastResetValue = counter.value;
        return counter;
    }

    public static Counter updateLastResetDate(Counter counter) {
        counter.lastResetDate = new Date();
        return counter;
    }
}
