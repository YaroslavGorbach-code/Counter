package com.yaroslavgorbachh.counter.screen.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.di.DaggerHistoryComponent;
import com.yaroslavgorbachh.counter.di.HistoryComponent;

public class HistoryViewModel extends AndroidViewModel {
    private HistoryComponent historyComponent;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
    }

    public HistoryComponent getHistoryComponent(long id) {
        if (historyComponent == null) {
            historyComponent = DaggerHistoryComponent.factory().create(id, ((App)getApplication()).appComponent);
        }
        return historyComponent;
    }
}
