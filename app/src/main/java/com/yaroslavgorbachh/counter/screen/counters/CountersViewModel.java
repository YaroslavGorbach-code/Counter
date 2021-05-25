package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Application;
import android.media.AudioManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.component.counters.CountersImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.CountersComponent;
import com.yaroslavgorbachh.counter.di.DaggerCountersComponent;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class CountersViewModel extends AndroidViewModel {
    public CountersComponent countersComponent ;

    public CountersViewModel(@NonNull Application application) {
        super(application);
        countersComponent = DaggerCountersComponent.factory().create(application, ((App)application).appComponent);
    }


}
