package com.yaroslavgorbachh.counter.screen.history;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeDeleteDecor extends ItemTouchHelper {
    public interface ItemSwipeCallback {
        void onSwipe(RecyclerView.ViewHolder viewHolder);
    }

    public SwipeDeleteDecor(ItemSwipeCallback callback, Drawable itemBg) {
        super(new SimpleCallback(0, START | END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) { return false; }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                callback.onSwipe(viewHolder);
            }

            @Override
            public void onChildDraw(@NonNull Canvas canvas, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View item = viewHolder.itemView;
                int clipLeft;
                int clipRight;
                if (dX >= 0){ clipLeft = 0; }else{ clipLeft = item.getWidth() + (int)dX; }
                if (dX >= 0){ clipRight = (int)dX; }else { clipRight = item.getWidth(); }

                canvas.clipRect(clipLeft, item.getTop(), clipRight, item.getBottom());
                itemBg.setBounds(0, item.getTop(), item.getWidth(), item.getBottom());
                int alpha = (int)((1 - Math.abs(dX / item.getWidth())) * 255);
                if (alpha>=1)
                itemBg.setAlpha(alpha);
                itemBg.draw(canvas);

                super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            @Override
            public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                viewHolder.itemView.setActivated(false);
                super.clearView(recyclerView, viewHolder);
            }

            @Override
            public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
                if (actionState == ACTION_STATE_SWIPE) {
                    if (viewHolder != null) {
                        viewHolder.itemView.setActivated(true);
                    }
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        });

    }
}
