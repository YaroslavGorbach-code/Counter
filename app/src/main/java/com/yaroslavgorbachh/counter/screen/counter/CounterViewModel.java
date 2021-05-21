package com.yaroslavgorbachh.counter.screen.counter;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counter.CounterComponent;
import com.yaroslavgorbachh.counter.component.counter.CounterComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class CounterViewModel extends ViewModel {
    private CounterComponent counter;
    public CounterComponent getCounterComponent(Repo repo, long id) {
        if (counter == null) {
            counter = new CounterComponentImp(repo, id);
        }
        return counter;
    }
}
