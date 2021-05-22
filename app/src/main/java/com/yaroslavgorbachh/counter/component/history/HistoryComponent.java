package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.History;

import java.util.List;

public interface HistoryComponent {
    LiveData<List<History>> getHistory();
    void clean();
    void remove(History history);
    void addItem(History item);
}
