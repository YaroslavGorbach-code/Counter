package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.History;

import java.util.List;

public interface HistoryCom {
    LiveData<List<com.yaroslavgorbachh.counter.data.Domain.History>> getHistory();
    void clean();
    void remove(com.yaroslavgorbachh.counter.data.Domain.History history);
    void addItem(com.yaroslavgorbachh.counter.data.Domain.History item);
}
