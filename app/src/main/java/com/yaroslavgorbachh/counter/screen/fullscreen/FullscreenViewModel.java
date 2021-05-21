package com.yaroslavgorbachh.counter.screen.fullscreen;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.fullscreen.Fullscreen;
import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class FullscreenViewModel extends ViewModel {
    private Fullscreen fullscreen;

    public Fullscreen getFullscreenCounter(Repo repo, long id) {
        if (fullscreen == null) {
            fullscreen = new FullscreenImp(repo, id);
        }
        return fullscreen;
    }
}
