package com.yaroslavgorbachh.counter.counterHistory;

import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class HistoryManager {

    private static HistoryManager sInstance = null;
    private final Timer timer = new Timer();
    private long pValue;

    private HistoryManager(){}

    public static HistoryManager getInstance(){
        if (sInstance==null){
            sInstance = new HistoryManager();
        }
        return sInstance;
    }

    public void saveValueWitDelay(long value, long id, Repo repo){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (value==pValue)
                repo.insertCounterHistory(new CounterHistory(value, Utility.convertDateToString(new Date()), id));
            }
        }, 2000);
        pValue = value;
    }

}
