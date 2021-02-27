package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

import java.util.Date;
import java.util.List;

public class CreateCounterDialogViewModel extends AndroidViewModel {

    private final Repo mRepo;
    private final LiveData<List<String>> mGroups;
    public CreateCounterDialogViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
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
