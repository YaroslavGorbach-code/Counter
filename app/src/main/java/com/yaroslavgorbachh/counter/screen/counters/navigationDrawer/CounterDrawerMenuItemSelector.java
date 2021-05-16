package com.yaroslavgorbachh.counter.screen.counters.navigationDrawer;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.R;

import javax.inject.Inject;

public class CounterDrawerMenuItemSelector implements DrawerItemSelector {
    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();
    private RecyclerView.ViewHolder mSelected_vh;
    private View mAllCountersItem_v;

    @Inject
    public CounterDrawerMenuItemSelector(){}

    @Override
    public void selectItem(String string, RecyclerView.ViewHolder vh) {
        if (mSelected_vh != null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh = null;
        }

        if (mAllCountersItem_v != null){
            setDefaultBackground(mAllCountersItem_v);
            mAllCountersItem_v = null;
        }

        if (vh!=null){
            mSelected_vh = vh;
            setSelectedBackground(mSelected_vh.itemView);
        }
        selectedItem.setValue(string);
    }

    @Override
    public LiveData<String> getSelectedItemTitle() {
        return selectedItem;
    }

    @Override
    public void bindBackground(String title, RecyclerView.ViewHolder viewHolder) {
        if (mSelected_vh!=null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh=null;
        }
        if (title.equals(selectedItem.getValue())) {
            setSelectedBackground(viewHolder.itemView);
        }else {
            setDefaultBackground(viewHolder.itemView);
        }
    }

    @Override
    public void allCountersItemSelected(View view) {
        if (mAllCountersItem_v != null){
            setDefaultBackground(mAllCountersItem_v);
            mAllCountersItem_v = null;
        }

        if (mSelected_vh!=null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh = null;
        }

        selectedItem.setValue(view.getResources().getString(R.string.allCountersItem));
        mAllCountersItem_v = view;
        setSelectedBackground(mAllCountersItem_v);
    }

    private void setDefaultBackground(View view){
        view.setBackgroundResource(R.drawable.group_item);
    }

    private void setSelectedBackground(View view){
        view.setBackgroundResource(R.drawable.i_group_selected_bg);
    }

}
