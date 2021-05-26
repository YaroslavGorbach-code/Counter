package com.yaroslavgorbachh.counter.component.counter;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;

import io.reactivex.rxjava3.core.Observable;

public class CounterComImp implements CounterCom {
    private final Repo mRepo;
    private final long mId;
    private final Accessibility mAccessibility;

    public CounterComImp(Repo repo, long id, Accessibility accessibility) {
        mRepo = repo;
        mId = id;
        mAccessibility = accessibility;
    }

    @Override
    public void incCounter(Repo.ValueCallback callback) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playIncSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playIncVibrationEffect();
        if (mRepo.getClickSpeakIsAllow()) mAccessibility.speechOutput(String.valueOf(getCounter().blockingFirst().value + 1));
        mRepo.incCounter(mId, callback);
    }

    @Override
    public void decCounter(Repo.ValueCallback callback) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playDecSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playDecVibrationEffect();
        if (mRepo.getClickSpeakIsAllow()) mAccessibility.speechOutput(String.valueOf(getCounter().blockingFirst().value - 1));
        mRepo.decCounter(mId, callback);
    }

    @Override
    public void resetCounter(ResetCallback callback) {
        callback.onReset(getCounter().blockingFirst());
        mRepo.resetCounter(mId);
    }


    public void delete() {
        mRepo.deleteCounter(mId);
        mRepo.removeCounterHistory(mId);
    }

    @Override
    public void insert(Counter copy) {
        mRepo.createCounter(copy);
    }

    @Override
    public int getFastCountInterval() {
        return mRepo.getFastCountInterval();
    }

    @Override
    public Observable<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }

}
