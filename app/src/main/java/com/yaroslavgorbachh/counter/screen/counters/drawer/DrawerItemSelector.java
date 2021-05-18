package com.yaroslavgorbachh.counter.screen.counters.drawer;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

public interface DrawerItemSelector {
    void selectItem(String string, RecyclerView.ViewHolder vh);
    void bindBackground(String title, RecyclerView.ViewHolder viewHolder);
    LiveData<String> getSelectedItemTitle();
    void clearSelected();

}
