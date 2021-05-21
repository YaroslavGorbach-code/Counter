package com.yaroslavgorbachh.counter.screen.counters;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counters.CountersComponent;
import com.yaroslavgorbachh.counter.component.counters.CountersComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class CountersViewModel extends ViewModel {
    private CountersComponent mCountersComponent;

    public CountersComponent getCountersComponent(Repo repo) {
        if (mCountersComponent == null){
            mCountersComponent = new CountersComponentImp(repo);
        }
        return mCountersComponent;
    }
}
