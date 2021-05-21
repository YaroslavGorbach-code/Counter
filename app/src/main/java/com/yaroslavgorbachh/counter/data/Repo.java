package com.yaroslavgorbachh.counter.data;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.History;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface Repo { void incCounter(long mId);
    void decCounter(long mId);
    void resetCounter(long mId);
    void deleteCounter(long mId);
    void removeHistory(long mId);
    Observable<Counter> getCounter(long counterId);
    LiveData<List<History>> getHistoryList(long counterId);
    void removeHistory(History history);
    void addHistory(History copy);
    void deleteCounters();
    LiveData<List<Counter>> getCounters();
    void editCounter(Counter counter);
    void changeTheme(AppStyle appStyle);
    LiveData<List<String>> getGroups();
    void createCounter(Counter counter);
    void backup(Intent data, Context context);
    void restore(Intent data, Context context);
    void triggerCountersLiveData();

}
