package com.yaroslavgorbachh.counter.screen.aboutCounter;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounter;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounterImp;
import com.yaroslavgorbachh.counter.data.Repo;

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
