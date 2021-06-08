package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.di.CountersComponent;
import com.yaroslavgorbachh.counter.di.DaggerCountersComponent;

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
