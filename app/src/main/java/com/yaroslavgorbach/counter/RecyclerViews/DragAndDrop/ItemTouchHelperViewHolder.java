package com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop;

import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;

public interface ItemTouchHelperViewHolder {
     void onSelectedChanged();
     void clearView();
     void onDragging(RecyclerView.ViewHolder viewHolder);
}
