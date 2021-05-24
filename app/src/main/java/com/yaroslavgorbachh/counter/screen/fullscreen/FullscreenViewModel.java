package com.yaroslavgorbachh.counter.screen.fullscreen;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenComponent;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.feature.Accessibility;

public class FullscreenViewModel extends ViewModel {
    private FullscreenComponent fullscreenComponent;

    public FullscreenComponent getFullscreenCounter(Repo repo, long id, Accessibility accessibility) {
        if (fullscreenComponent == null) {
            fullscreenComponent = new FullscreenComponentImp(repo, id, accessibility);
        }
        return fullscreenComponent;
    }
}
