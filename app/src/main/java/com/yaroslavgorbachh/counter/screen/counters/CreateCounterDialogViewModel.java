package com.yaroslavgorbachh.counter.screen.counters;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CreateCounterDialogViewModel extends ViewModel {

    private final Repo repo;
    private final LiveData<List<String>> mGroups;
    @Inject
    public CreateCounterDialogViewModel(Repo repo) {
        this.repo = repo;
        mGroups = repo.getGroups();
    }

    public void createCounter(String title, String group) {
            Counter counter = new Counter(title, 0, Counter.MAX_VALUE,
                    Counter.MIN_VALUE, 1, group, new Date(),
                    new Date(), null, 0, 0, 0, null);
        repo.insertCounter(counter);
    }

    public LiveData<List<String>> getGroups() {
        return mGroups;
    }
}
