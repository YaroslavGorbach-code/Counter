package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.media.AudioManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.component.counters.CountersImp;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.CountersComponent;
import com.yaroslavgorbachh.counter.di.DaggerCountersComponent;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class CountersViewModel extends AndroidViewModel {
    private CountersComponent countersComponent = null;

    public CountersViewModel(@NonNull Application application) {
        super(application);
    }

    public CountersComponent getCountersComponent(Activity activity){
        if (countersComponent == null){
            countersComponent = DaggerCountersComponent.factory().create(
                    getApplication(), activity, ((App)getApplication()).appComponent);
        }
        return countersComponent;
    }
}
