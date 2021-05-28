package com.yaroslavgorbachh.counter.component.counters;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public interface Counters {

    interface ResetCallback{
        void onReset(List<Counter> copy);
    }

    LiveData<List<String>> getGroups();
    void inc(Counter counter, Repo.ValueCallback callback);
    void dec(Counter counter, Repo.ValueCallback callback);
    void onMove(Counter from, Counter to);
    Observable<List<Counter>> getCounters();
    void createCounter(String title, String group);
    void remove(List<Counter> counters);
    void reset(List<Counter> counters, ResetCallback callback);
    void update(List<Counter> copy);
    void decSelected(List<Counter> selected);
    void incSelected(List<Counter> selected);
    void setGroup(String group);
    void onLoverVolume();
    void onRaiseVolume();
    int getFastCountInterval();
    String getCurrentGroup();
    List<Counter> sortCounters(List<Counter> counters);
    boolean getAdIsAllow();
    void showPurchasesDialog(FragmentActivity activity);
    void queryPurchases(FragmentActivity activity);

}
