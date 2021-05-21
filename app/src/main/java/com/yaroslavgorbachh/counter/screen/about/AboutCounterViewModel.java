package com.yaroslavgorbachh.counter.screen.about;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.aboutcounter.AboutComponent;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class AboutCounterViewModel extends ViewModel {
    private AboutComponent aboutComponent;

    public AboutComponent getAboutCounter(Repo repo, long id) {
        if (aboutComponent == null) {
            aboutComponent = new AboutComponentImp(repo, id) {
            };
        }
        return aboutComponent;
    }

}
