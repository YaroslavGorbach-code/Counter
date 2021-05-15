package com.yaroslavgorbachh.counter.screen.counter;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counter.CounterComp;
import com.yaroslavgorbachh.counter.component.counter.CounterCompImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class CounterViewModel extends ViewModel {
    private CounterComp counter;
    public CounterComp getCounterComponent(Repo repo, long id) {
        if (counter == null) {
            counter = new CounterCompImp(repo, id);
        }
        return counter;
    }
}
