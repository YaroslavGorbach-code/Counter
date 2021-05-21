package com.yaroslavgorbachh.counter.screen.fullscreen;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenComponent;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class FullscreenViewModel extends ViewModel {
    private FullscreenComponent fullscreenComponent;

    public FullscreenComponent getFullscreenCounter(Repo repo, long id) {
        if (fullscreenComponent == null) {
            fullscreenComponent = new FullscreenComponentImp(repo, id);
        }
        return fullscreenComponent;
    }
}
