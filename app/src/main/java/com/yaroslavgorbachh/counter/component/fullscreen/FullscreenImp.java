package com.yaroslavgorbachh.counter.component.fullscreen;

import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;

import io.reactivex.rxjava3.core.Observable;

public class FullscreenImp implements Fullscreen {
    private final Repo mRepo;
    private final long mId;
    private final Accessibility mAccessibility;
    public FullscreenImp(Repo repo, long id, Accessibility accessibility){
        mRepo = repo;
        mId = id;
        mAccessibility = accessibility;
    }
    @Override
    public void inc(Repo.ValueCallback callback) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playIncSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playIncVibrationEffect();
        if (mRepo.getClickSpeakIsAllow()) mAccessibility.speechOutput(String.valueOf(getCounter().blockingFirst().value + 1));
        mRepo.incCounter(mId, callback);
    }

    @Override
    public void dec(Repo.ValueCallback callback) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playDecSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playDecVibrationEffect();
        if (mRepo.getClickSpeakIsAllow()) mAccessibility.speechOutput(String.valueOf(getCounter().blockingFirst().value - 1));
        mRepo.decCounter(mId, callback);
    }

    @Override
    public Observable<Counter> getCounter() {
        return mRepo.getCounter(mId);
    }
}
