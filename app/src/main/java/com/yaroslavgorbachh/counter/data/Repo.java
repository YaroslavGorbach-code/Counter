package com.yaroslavgorbachh.counter.data;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Domain.History;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface Repo {
    void incCounter(long id);
    void decCounter(long id);
    void resetCounter(long id);
    void deleteCounter(long mId);
    void removeCounterHistory(long id);
    void removeHistoryItem(long id);
    Observable<Counter> getCounter(long counterId);
    LiveData<List<History>> getHistoryList(long counterId);
    void addHistory(History copy);
    void deleteCounters();
    Counter getCounterWidget(long widgetId);
    Observable<List<Counter>> getCounters();
    void updateCounter(Counter counter);
    LiveData<List<String>> getGroups();
    void createCounter(Counter counter);
    void backup(Intent data, Context context);
    void restore(Intent data, Context context);
    void triggerCountersLiveData();
    boolean getIsOrientationLock();
    boolean getUseVolumeButtonsIsAllow();
    boolean getKeepScreenOnIsAllow();
    boolean getClickVibrationIsAllow();
    boolean getClickSoundIsAllow();
    boolean getClickSpeakIsAllow();
    boolean getIsNightMod();
    void setIsNightMod(boolean b);
    int getFastCountInterval();
}
