package com.yaroslavgorbachh.counter.counter;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.component.CounterComp;
import com.yaroslavgorbachh.counter.component.CounterCompImp;
import com.yaroslavgorbachh.counter.database.Repo;

public class CounterViewModel extends ViewModel {
    private CounterComp counterComp;
    public CounterComp getCounterComponent(Repo repo, long id) {
        if (counterComp == null) {
            counterComp = new CounterCompImp(repo, id);
        }
        return counterComp;
    }
}
