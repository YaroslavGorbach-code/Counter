package com.yaroslavgorbach.counter;

import androidx.recyclerview.widget.RecyclerView;

public interface ItemTouchHelperAdapter {

    boolean onItemMove(int fromPosition, int toPosition);

      void onItemMoved (RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, int fromPos,
                    RecyclerView.ViewHolder target, int toPos, int x, int y);
}
