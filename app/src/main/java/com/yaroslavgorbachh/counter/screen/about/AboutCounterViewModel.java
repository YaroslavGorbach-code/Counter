package com.yaroslavgorbachh.counter.screen.about;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounter;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounterImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.di.AboutCounterComponent;
import com.yaroslavgorbachh.counter.di.DaggerAboutCounterComponent;

public class AboutCounterViewModel extends AndroidViewModel {
    private AboutCounterComponent aboutCounterComponent = null;

    public AboutCounterViewModel(@NonNull Application application) {
        super(application);
    }

    public AboutCounterComponent getAboutCounterComponent(long id) {
        if (aboutCounterComponent == null) {
            aboutCounterComponent = DaggerAboutCounterComponent.factory().create(id, ((App)getApplication()).appComponent);
        }
        return aboutCounterComponent;
    }

}
