package com.yaroslavgorbachh.counter.screen.counters.drawer;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.R;

public class CounterDrawerItemSelector implements DrawerItemSelector {
    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();
    private RecyclerView.ViewHolder mSelectedItem;

    @Override
    public void selectItem(String string, RecyclerView.ViewHolder vh) {
        if (mSelectedItem != null){
            setDefaultBackground(mSelectedItem.itemView);
            mSelectedItem = null;
        }

        if (vh!=null){
            mSelectedItem = vh;
            setSelectedBackground(mSelectedItem.itemView);
        }
        selectedItem.setValue(string);
    }

    @Override
    public LiveData<String> getSelectedItemTitle() {
        return selectedItem;
    }

    @Override
    public void clearSelected() {
        if (mSelectedItem !=null){
            setDefaultBackground(mSelectedItem.itemView);
            mSelectedItem = null;
        }
    }

    @Override
    public void bindBackground(String title, RecyclerView.ViewHolder viewHolder) {
        if (mSelectedItem !=null){
            setDefaultBackground(mSelectedItem.itemView);
            mSelectedItem =null;
        }
        if (title.equals(selectedItem.getValue())) {
            setSelectedBackground(viewHolder.itemView);
        }else {
            setDefaultBackground(viewHolder.itemView);
        }
    }

    private void setDefaultBackground(View view){
        view.setBackgroundResource(R.drawable.group_item);
    }

    private void setSelectedBackground(View view){
        view.setBackgroundResource(R.drawable.i_group_selected_bg);
    }

}
