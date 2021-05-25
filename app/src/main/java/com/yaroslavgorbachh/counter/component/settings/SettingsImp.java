package com.yaroslavgorbachh.counter.component.settings;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.List;

public class SettingsImp implements Settings {
    private final Repo mRepo;

    public SettingsImp(Repo repo) {
        mRepo = repo;
    }

    @Override
    public void backup(Intent data, Context context) {
        mRepo.backup(data, context);
    }

    @Override
    public void restore(Intent data, Context context) {
        mRepo.restore(data, context);
    }

    @Override
    public void deleteAll() {
        mRepo.deleteCounters();
    }

    @Override
    public void resetAll() {
        // TODO: 5/15/2021 reset all
//        Disposable disposable = repo.getAllCountersNoLiveData()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(counters -> {
//                    CopyCounterBeforeReset copyCounterBeforeReset = new CopyCounterBeforeReset();
//                    for (Counter counter : counters) {
//                        copyCounterBeforeReset.addCounter(counter);
//                        repo.resetCounter(counter.id);
//                    }
//                    Snackbar.make(view, view.getContext().getResources().getString(R.string
//                            .countersReset), BaseTransientBottomBar.LENGTH_LONG)
//                            .setAction( view.getContext().getString(R.string.counterResetUndo), v1 -> {
//                                for (Counter counter : copyCounterBeforeReset.getCounters()) {
//                                    repo.updateCounter(counter);
//                                }
//                            }).show();
//                });
//        mDisposables.add(disposable);
    }

    @Override
    public LiveData<List<Counter>> getAll() {
        return mRepo.getCounters();
    }
}
