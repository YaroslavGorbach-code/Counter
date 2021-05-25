package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.fullscreen.Fullscreen;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.DaggerFullscreenComponent;
import com.yaroslavgorbachh.counter.di.FullscreenComponent;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class FullscreenViewModel extends AndroidViewModel {
    private FullscreenComponent fullscreenComponent;

    public FullscreenViewModel(@NonNull Application application) {
        super(application);
    }

    public FullscreenComponent getFullscreenCounter(long id) {
        if (fullscreenComponent == null) {
            fullscreenComponent = DaggerFullscreenComponent.factory().create(id, getApplication(), ((App)getApplication()).appComponent);
        }
        return fullscreenComponent;
    }
}
