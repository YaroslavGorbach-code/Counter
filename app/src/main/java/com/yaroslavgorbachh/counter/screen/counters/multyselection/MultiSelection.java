package com.yaroslavgorbachh.counter.screen.counters.multyselection;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import java.util.List;

public interface MultiSelection{
    void select(Counter counter, RecyclerView.ViewHolder viewHolder);
    void unselect(Counter counter, RecyclerView.ViewHolder viewHolder);
    void selectAll(List<Counter> counters);
    void bindBackground(Counter counter, RecyclerView.ViewHolder viewHolder);
    LiveData<Integer> getSelectedCount();
    List<Counter> getSelected();
    Counter getFirstSelected();
    LiveData<Boolean> getIsSelectionActive();
    void unselectAll();
}
