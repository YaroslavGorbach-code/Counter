package com.yaroslavgorbachh.counter.screen.countersList;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.Date;
import java.util.List;

public class CountersViewModel extends ViewModel {
    private final Repo repo;
    private final LiveData<List<Counter>> mCounters;
    private final LiveData<List<String>> mGroups;

    public CountersViewModel(Repo repo) {
        this.repo = repo;
        mCounters = repo.getAllCounters();
        mGroups = repo.getGroups();
    }

    public void incCounter(Counter counter, Accessibility accessibility, Context context) {
       repo.incCounter(counter.id);
    }

    public void decCounter(Counter counter, Accessibility accessibility, Context context) {
     repo.decCounter(counter.id);
    }

    public void countersMoved(Counter counterFrom, Counter counterTo) {
        Date dataFrom;
        Date dataTo;

        if (counterFrom.createDateSort!=null && counterTo.createDateSort!=null){
            dataFrom = counterFrom.createDateSort;
            dataTo = counterTo.createDateSort;
        }else {
            dataFrom = counterFrom.createDate;
            dataTo = counterTo.createDate;
        }

        if (!dataFrom.equals(dataTo)) {
            counterTo.createDateSort = dataFrom;
            repo.updateCounter(counterTo);
            counterFrom.createDateSort = dataTo;
            repo.updateCounter(counterFrom);
        }

    }

    public LiveData<List<String>> getGroups() {
        return mGroups;
    }

    public LiveData<List<Counter>> getCounters(){
         return mCounters;
    }



    public static class CountersVmFactory extends ViewModelProvider.NewInstanceFactory {
        private final Repo repo;

        public CountersVmFactory(Repo repo) {
            super();
            this.repo = repo;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass.isAssignableFrom(CountersViewModel.class)) {
                return (T) new CountersViewModel(repo);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
