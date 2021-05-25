package com.yaroslavgorbachh.counter.component.settings;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.screen.settings.SettingsFragmentView;

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
    public void resetAll(ResetCallback callback) {
        List<Counter> copy = mRepo.getCounters().blockingFirst();
        for (Counter counter : copy) {
            mRepo.resetCounter(counter.id);
        }
        callback.onReset(copy);
    }

    @Override
    public List<Counter> getAll() {
        return mRepo.getCounters().blockingFirst();
    }

    @Override
    public void changeNightMod(boolean b) {
        mRepo.setIsNightMod(b);
    }

    @Override
    public void undoReset(List<Counter> copy) {
        for (Counter counter: copy) {
            mRepo.updateCounter(counter);
        }
    }
}
