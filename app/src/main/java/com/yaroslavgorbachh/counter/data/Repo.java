package com.yaroslavgorbachh.counter.data;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;

import java.util.List;
import io.reactivex.rxjava3.core.Single;

public interface Repo { void incCounter(long mId);
    void decCounter(long mId);
    void resetCounter(long mId);
    void deleteCounter(long mId);
    void deleteCounterHistory(long mId);
    LiveData<Counter> getCounter(long counterId);
    LiveData<List<CounterHistory>> getCounterHistoryList(long counterId);
    void deleteHistoryItem(CounterHistory counterHistory);
    void insertCounterHistory(CounterHistory copy);
    void deleteCounters();
    LiveData<List<Counter>> getAllCounters();
    Single<List<Counter>> getAllCountersNoLiveData();
    void updateCounter(Counter counter);
    void changeTheme(AppStyle appStyle);
    LiveData<List<String>> getGroups();
    Single<Counter> getCounterNoLiveData(long id);
    void insertCounter(Counter counter);
}
