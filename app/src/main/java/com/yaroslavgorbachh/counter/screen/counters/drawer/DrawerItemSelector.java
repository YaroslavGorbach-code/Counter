package com.yaroslavgorbachh.counter.screen.counters.drawer;

import androidx.recyclerview.widget.RecyclerView;

public interface DrawerItemSelector {
    void bindBackground(String title, RecyclerView.ViewHolder viewHolder);
    void clearSelected();
    void selectItem(String currentGroup);
}
