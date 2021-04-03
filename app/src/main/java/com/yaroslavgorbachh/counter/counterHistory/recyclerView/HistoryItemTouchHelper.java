package com.yaroslavgorbachh.counter.counterHistory.recyclerView;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.countersList.DragAndDrop.ItemTouchHelperViewHolder;

public class HistoryItemTouchHelper extends ItemTouchHelper.Callback{

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int swipeFlags = ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT;
        return makeMovementFlags(0, swipeFlags);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if (viewHolder instanceof ItemTouchHelperSwipeListener) {
            ItemTouchHelperSwipeListener itemTouchHelper = (ItemTouchHelperSwipeListener) viewHolder;
            itemTouchHelper.onSwipe();
        }
    }
}
