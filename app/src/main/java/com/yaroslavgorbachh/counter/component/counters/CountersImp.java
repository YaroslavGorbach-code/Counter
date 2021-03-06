package com.yaroslavgorbachh.counter.component.counters;

import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.feature.billing.BillingManager;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class CountersImp implements Counters {
    private final Repo mRepo;
    private String mGroup;
    private final Accessibility mAccessibility;
    private final AudioManager mAudioManager;
    private final BillingManager mBillingManager;
    public CountersImp(Repo repo, Accessibility accessibility, AudioManager audioManager, BillingManager billingManager) {
        mRepo = repo;
        mAccessibility = accessibility;
        mAudioManager = audioManager;
        mBillingManager = billingManager;
    }

    @Override
    public List<Counter> sortCounters(List<Counter> counters) {
        return Observable.fromIterable(counters)
                .filter(counter -> (counter.grope != null && counter.grope.equals(mGroup)))
                .toList()
                .blockingGet();
    }

    @Override
    public boolean getAdIsAllow() {
        return mRepo.getAdIsAllow();
    }

    @Override
    public void showPurchasesDialog(FragmentActivity activity) {
        mBillingManager.showPurchasesDialog(activity);
    }

    @Override
    public void queryPurchases(FragmentActivity activity) {
        mBillingManager.queryPurchases(isAdRemoved -> {
            if (isAdRemoved && mRepo.getAdIsAllow()) {
                mRepo.setAdIsAllow(false);
                activity.finish();
                activity.startActivity(new Intent(activity, activity.getClass()));
            }
        });
    }

    @Override
    public void onLoverVolume() {
        mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public void onRaiseVolume() {
        mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
    }

    @Override
    public int getFastCountInterval() {
        return mRepo.getFastCountInterval();
    }

    @Override
    public LiveData<List<String>> getGroups() {
        return mRepo.getGroups();
    }

    @Override
    public void inc(Counter counter, Repo.ValueCallback callback) {
       if (mRepo.getClickSoundIsAllow()) mAccessibility.playIncSoundEffect();
       if (mRepo.getClickVibrationIsAllow()) mAccessibility.playIncVibrationEffect();
       if (mRepo.getClickSpeakIsAllow()) mAccessibility.speechOutput(String.valueOf(counter.value + 1));
        mRepo.incCounter(counter.id, callback);
    }

    @Override
    public void dec(Counter counter, Repo.ValueCallback callback) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playDecSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playDecVibrationEffect();
        if (mRepo.getClickSpeakIsAllow()) mAccessibility.speechOutput(String.valueOf(counter.value - 1));
        mRepo.decCounter(counter.id, callback);
    }

    @Override
    public void onMove(Counter from, Counter to) {
        Date dataFrom;
        Date dataTo;

        if (from.createDateSort != null && to.createDateSort != null) {
            dataFrom = from.createDateSort;
            dataTo = to.createDateSort;
        } else {
            dataFrom = from.createDate;
            dataTo = to.createDate;
        }

        if (!dataFrom.equals(dataTo)) {
            to.createDateSort = dataFrom;
            mRepo.updateCounter(to);
            from.createDateSort = dataTo;
            mRepo.updateCounter(from);
        }
    }

    @Override
    public Observable<List<Counter>> getCounters() {
        return mRepo.getCounters();
    }

    @Override
    public void createCounter(String title, String group) {
        Counter counter = new Counter(title, 0, Counter.MAX_VALUE,
                Counter.MIN_VALUE, 1, R.color.purple, group, new Date(),
                new Date(), null, 0, 0, 0, null);
        mRepo.createCounter(counter);
    }

    @Override
    public void remove(List<Counter> counters) {
        for (Counter counter : counters) {
            mRepo.deleteCounter(counter.id);
        }
    }

    @Override
    public void reset(List<Counter> counters, ResetCallback resetCallback) {
        for (Counter counter : counters) {
            mRepo.resetCounter(counter.id);
        }
        resetCallback.onReset(counters);
    }

    @Override
    public void update(List<Counter> copy) {
        for (Counter counter : copy) {
            mRepo.updateCounter(counter);
        }
    }

    @Override
    public void decSelected(List<Counter> selected) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playDecSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playDecVibrationEffect();
        for (Counter counter : selected) {
            mRepo.decCounter(counter.id, null);
        }
    }

    @Override
    public void incSelected(List<Counter> selected) {
        if (mRepo.getClickSoundIsAllow()) mAccessibility.playIncSoundEffect();
        if (mRepo.getClickVibrationIsAllow()) mAccessibility.playIncVibrationEffect();
        for (Counter counter : selected) {
            mRepo.incCounter(counter.id, null);
        }
    }

    @Override
    public void setGroup(String group) {
        mGroup = group;
        mRepo.triggerCountersLiveData();
    }

    @Override
    public String getCurrentGroup() {
        return mGroup;
    }

}
