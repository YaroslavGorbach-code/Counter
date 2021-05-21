package com.yaroslavgorbachh.counter.screen.counters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class DragAndDropItemTouchHelper extends ItemTouchHelper.Callback {

    public interface CallbackAdapter {
        void onMoved(int fromPos, int toPos);
    }

    public interface CallbackViewHolder {
        void clearView(RecyclerView.ViewHolder viewHolder);
        void onSelectedChange(RecyclerView.ViewHolder viewHolder);
    }

    private final CallbackAdapter mCallbackAdapter;

    public DragAndDropItemTouchHelper(CallbackAdapter callbackAdapter) {
        mCallbackAdapter = callbackAdapter;
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return false;
    }

    @Override
    public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof CallbackViewHolder) {
            CallbackViewHolder itemViewHolder =
                    (CallbackViewHolder) viewHolder;
            itemViewHolder.clearView(viewHolder);
        }
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        return makeMovementFlags(dragFlags, 0);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder instanceof CallbackViewHolder) {
            CallbackViewHolder itemViewHolder =
                    (CallbackViewHolder) viewHolder;
            itemViewHolder.onSelectedChange(viewHolder);
        }
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onMoved(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, int fromPos, @NonNull RecyclerView.ViewHolder target, int toPos, int x, int y) {
        super.onMoved(recyclerView, viewHolder, fromPos, target, toPos, x, y);
        mCallbackAdapter.onMoved(viewHolder.getBindingAdapterPosition(), target.getBindingAdapterPosition());
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) { }
}

