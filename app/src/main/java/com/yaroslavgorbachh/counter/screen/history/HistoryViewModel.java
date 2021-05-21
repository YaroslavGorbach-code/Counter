package com.yaroslavgorbachh.counter.screen.history;

import androidx.lifecycle.ViewModel;

import com.yaroslavgorbachh.counter.component.history.HistoryComponent;
import com.yaroslavgorbachh.counter.component.history.HistoryComponentImp;
import com.yaroslavgorbachh.counter.data.Repo;

public class HistoryViewModel extends ViewModel {
    private HistoryComponent historyComponent;

    public HistoryComponent getHistoryComponent(Repo repo, long id) {
        if (historyComponent == null) {
            historyComponent = new HistoryComponentImp(repo, id);
        }
        return historyComponent;
    }
}
