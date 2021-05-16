package com.yaroslavgorbachh.counter.screen.counters.navigationDrawer;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

public interface DrawerItemSelector {
    void selectItem(String string, RecyclerView.ViewHolder vh);
    void bindBackground(String title, RecyclerView.ViewHolder viewHolder);
    void allCountersItemSelected(View view);
    LiveData<String> getSelectedItemTitle();

}
