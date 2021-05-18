package com.yaroslavgorbachh.counter.screen.counters.drawer;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.R;

public class GroupsDrawerItemSelector implements DrawerItemSelector {
    private String mSelectedItem;

    @Override
    public void clearSelected() {
        mSelectedItem = null;
    }

    @Override
    public void selectItem(String group) {
        mSelectedItem = group;
    }

    @Override
    public void bindBackground(String title, RecyclerView.ViewHolder viewHolder) {
        if (mSelectedItem != null && mSelectedItem.equals(title)) {
            setSelectedBackground(viewHolder.itemView);
        } else {
            setDefaultBackground(viewHolder.itemView);
        }
    }

    private void setDefaultBackground(View view) {
        view.setBackgroundResource(R.drawable.i_group);
    }

    private void setSelectedBackground(View view) {
        view.setBackgroundResource(R.drawable.i_group_selected_bg);
    }

}
