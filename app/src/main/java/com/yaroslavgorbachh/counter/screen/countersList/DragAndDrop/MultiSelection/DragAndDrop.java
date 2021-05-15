package com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.MultiSelection;

import androidx.recyclerview.widget.RecyclerView;

public interface DragAndDrop {
    void startDragging(RecyclerView.ViewHolder viewHolder);
    void stopDragging();
}
