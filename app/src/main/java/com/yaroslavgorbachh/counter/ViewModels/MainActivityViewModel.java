package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivityViewModel extends AndroidViewModel {
    private final Repo mRepo;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        mRepo = new Repo(application);
    }

    public LiveData<List<String>> getGroups() {
       return mRepo.getGroups();
    }
}
