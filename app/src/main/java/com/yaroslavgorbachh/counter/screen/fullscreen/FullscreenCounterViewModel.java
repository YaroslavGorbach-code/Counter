package com.yaroslavgorbachh.counter.screen.fullscreen;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenCounter;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenCounterImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class FullscreenCounterViewModel extends ViewModel {
    private FullscreenCounter fullscreenCounter;

    public FullscreenCounter getFullscreenCounter(Repo repo, long id) {
        if (fullscreenCounter == null) {
            fullscreenCounter = new FullscreenCounterImp(repo, id);
        }
        return fullscreenCounter;
    }
}
