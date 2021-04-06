package com.yaroslavgorbachh.counter.countersList.DragAndDrop.MultiSelection;

import androidx.recyclerview.widget.RecyclerView;

public interface DragAndDrop {
    void startDragging(RecyclerView.ViewHolder viewHolder);
    void stopDragging();
}
