package com.yaroslavgorbach.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbach.counter.Database.Repo;

import java.util.List;

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
