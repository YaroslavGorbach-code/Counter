package com.yaroslavgorbachh.counter.aboutCounter;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounter;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounterImp;
import com.yaroslavgorbachh.counter.component.counter.CounterComp;
import com.yaroslavgorbachh.counter.component.counter.CounterCompImp;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;

import javax.inject.Inject;

public class AboutCounterViewModel extends ViewModel {
    private AboutCounter aboutCounter;

    public AboutCounter getAboutCounter(Repo repo, long id) {
        if (aboutCounter == null) {
            aboutCounter = new AboutCounterImp(repo, id) {
            };
        }
        return aboutCounter;
    }

}
