package com.yaroslavgorbachh.counter.screen.counters.DragAndDrop.MultiSelection;

import androidx.recyclerview.widget.RecyclerView;

public interface DragAndDrop {
    void startDragging(RecyclerView.ViewHolder viewHolder);
    void stopDragging();
}
