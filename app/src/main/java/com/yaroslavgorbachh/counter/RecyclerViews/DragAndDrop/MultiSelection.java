package com.yaroslavgorbachh.counter.RecyclerViews.DragAndDrop;

import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.Database.Models.Counter;

import java.util.List;

public interface MultiSelection {
    void select(Counter counter, RecyclerView.ViewHolder viewHolder);
    void unSelect(Counter counter, RecyclerView.ViewHolder viewHolder);
    void selectAll(List<Counter> counters);
    void clearAllSelections();
}
