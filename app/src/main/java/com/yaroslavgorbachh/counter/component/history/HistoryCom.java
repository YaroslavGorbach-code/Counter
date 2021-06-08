package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import java.util.List;

public interface HistoryCom {
    LiveData<List<com.yaroslavgorbachh.counter.data.domain.History>> getHistory();
    void clean();
    void remove(com.yaroslavgorbachh.counter.data.domain.History history);
    void addItem(com.yaroslavgorbachh.counter.data.domain.History item);
}
