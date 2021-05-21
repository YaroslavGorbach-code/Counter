package com.yaroslavgorbachh.counter.component.aboutcounter;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

public class AboutComponentImp implements AboutComponent {
    private long mId;
    private Repo repo;
    public AboutComponentImp(Repo repo, long id) {
        mId = id;
        this.repo = repo;
    }

    @Override
    public LiveData<Counter> getCounter() {
        return repo.getCounter(mId);
    }
}
