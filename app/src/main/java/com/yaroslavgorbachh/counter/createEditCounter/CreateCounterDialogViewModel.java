package com.yaroslavgorbachh.counter.createEditCounter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class CreateCounterDialogViewModel extends ViewModel {

    private final Repo mRepo;
    private final LiveData<List<String>> mGroups;
    @Inject
    public CreateCounterDialogViewModel(Repo repo) {
        mRepo = repo;
        mGroups = mRepo.getGroups();
    }

    public void createCounter(String title, String group) {
            Counter counter = new Counter(title, 0, Counter.MAX_VALUE,
                    Counter.MIN_VALUE, 1, group, new Date(),
                    new Date(), null, 0, 0, 0);
            mRepo.insertCounter(counter);
    }

    public LiveData<List<String>> getGroups() {
        return mGroups;
    }
}
