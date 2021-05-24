package com.yaroslavgorbachh.counter.screen.counter;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counter.CounterComponent;
import com.yaroslavgorbachh.counter.component.counter.CounterComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class CounterViewModel extends ViewModel {
    private CounterComponent counter;
    public CounterComponent getCounterComponent(Repo repo, long id, Accessibility accessibility) {
        if (counter == null) {
            counter = new CounterComponentImp(repo, id, accessibility);
        }
        return counter;
    }
}
