package com.yaroslavgorbachh.counter.feature;

import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class HistoryManager {
    public interface Callback{
        void onSave();
    }
    private static HistoryManager sInstance = null;
    private final Timer timer = new Timer();
    private final Map<Long, Long> previousValues = new HashMap<>();

    private HistoryManager(){

    }

    public static HistoryManager getInstance(){
        if (sInstance==null){
            sInstance = new HistoryManager();
        }
        return sInstance;
    }

    public void saveValueWitDelay(long id, long value, Callback callback){
        previousValues.put(id, value);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (previousValues.get(id)==value) callback.onSave();
            }
        }, 2000);

    }

}