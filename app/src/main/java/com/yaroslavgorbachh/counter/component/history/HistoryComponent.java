package com.yaroslavgorbachh.counter.component.history;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Models.History;

import java.util.List;

public interface HistoryComponent {
    LiveData<List<History>> getHistory();
    void clean();
}
