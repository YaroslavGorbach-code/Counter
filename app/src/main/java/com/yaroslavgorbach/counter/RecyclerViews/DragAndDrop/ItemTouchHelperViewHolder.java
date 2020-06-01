package com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperViewHolder {
     void onSelectedChanged();
     void clearView();
}
