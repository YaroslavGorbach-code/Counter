package com.yaroslavgorbachh.counter.screen.counterHistory;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.history.History;
import com.yaroslavgorbachh.counter.component.history.HistoryImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class HistoryViewModel extends ViewModel {
    private History history;

    public History getHistoryComponent(Repo repo, long id) {
        if (history == null) {
            history = new HistoryImp(repo, id);
        }
        return history;
    }
}
