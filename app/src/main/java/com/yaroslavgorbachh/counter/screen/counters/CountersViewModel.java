package com.yaroslavgorbachh.counter.screen.counters;

import android.media.AudioManager;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.counters.CountersComponent;
import com.yaroslavgorbachh.counter.component.counters.CountersComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class CountersViewModel extends ViewModel {
    private CountersComponent mCountersComponent;

    public CountersComponent getCountersComponent(Repo repo, Accessibility accessibility, AudioManager audioManager) {
        if (mCountersComponent == null){
            mCountersComponent = new CountersComponentImp(repo, accessibility, audioManager);
        }
        return mCountersComponent;
    }
}
