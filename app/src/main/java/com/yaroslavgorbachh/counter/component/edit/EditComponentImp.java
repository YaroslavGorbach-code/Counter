package com.yaroslavgorbachh.counter.component.edit;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class EditComponentImp implements EditComponent {
    private final Repo mRepo;
    private final long mId;

    public EditComponentImp(Repo repo, long id) {
        mRepo = repo;
        mId = id;
    }

    @Override
    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }

    @Override
    public Observable<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }

    @Override
    public void updateCounter(Counter counter) {
        mRepo.createCounter(counter);
    }
}
