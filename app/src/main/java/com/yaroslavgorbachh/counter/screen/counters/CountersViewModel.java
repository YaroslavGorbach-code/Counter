package com.yaroslavgorbachh.counter.screen.counters;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.component.counters.CountersImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class CountersViewModel extends ViewModel {
    private Counters mCounters;

    public Counters getCountersComponent(Repo repo) {
        if (mCounters == null){
            mCounters = new CountersImp(repo);
        }
        return mCounters;
    }
}
