package com.yaroslavgorbachh.counter.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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

    /*delete the same groups*/
    public List<String> deleteTheSameGroups(List<String> strings){
        Set<String> set = new HashSet<>(strings);
        return Arrays.asList(set.toArray(new String[0]));
    }

    public void insertCounter(String title, String group){
        Date currentDate = new Date();
        currentDate.getTime();
        Counter counter = new Counter(title, 0, Long.parseLong("9999999999999999"),
                Long.parseLong("-9999999999999999"), 1, group, currentDate);
        mRepo.insert(counter);
    }
}
