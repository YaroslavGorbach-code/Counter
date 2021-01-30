package com.yaroslavgorbach.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Repo;

import java.util.Date;
import java.util.List;

public class CreateCounterDialogViewModel extends AndroidViewModel {

    private Repo mRepo;
    public CreateCounterDialogViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
    }

    public void createCounter(String title, String group) {
            Date currentDate = new Date();
            currentDate.getTime();
            Counter counter = new Counter(title, 0, Long.parseLong("9999999999999999"),
                    Long.parseLong("-9999999999999999"), 1, group, currentDate);
            mRepo.insertCounter(counter);
    }

    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }
}
