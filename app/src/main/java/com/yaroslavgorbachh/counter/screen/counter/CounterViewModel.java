package com.yaroslavgorbachh.counter.screen.counter;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.counter.CounterCom;
import com.yaroslavgorbachh.counter.component.counter.CounterComImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.CounterComponent;
import com.yaroslavgorbachh.counter.di.DaggerCounterComponent;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class CounterViewModel extends AndroidViewModel {
    private CounterComponent counter = null;

    public CounterViewModel(@NonNull Application application) {
        super(application);
    }

    public CounterComponent getCounterComponent(long id) {
        if (counter == null) {
            counter = DaggerCounterComponent.factory().create(id, getApplication(), ((App)getApplication()).appComponent);
        }
        return counter;
    }
}
