package com.yaroslavgorbachh.counter.counter;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counter.CounterComp;
import com.yaroslavgorbachh.counter.component.counter.CounterCompImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class CounterViewModel extends ViewModel {
    private CounterComp counterComp;
    public CounterComp getCounterComponent(Repo repo, long id) {
        if (counterComp == null) {
            counterComp = new CounterCompImp(repo, id);
        }
        return counterComp;
    }
}
