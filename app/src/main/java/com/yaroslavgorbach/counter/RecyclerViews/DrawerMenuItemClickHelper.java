package com.yaroslavgorbach.counter.RecyclerViews;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.R;

public class DrawerMenuItemClickHelper {

    private MutableLiveData<String> selectedGroup = new MutableLiveData<>();
    private RecyclerView.ViewHolder mSelected_vh;
    private View mAllCountersItem_v;

    public void selectRvItem(String string, RecyclerView.ViewHolder vh){

        if (mSelected_vh != null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh = null;
            selectedGroup.setValue(null);
        }

        if (mAllCountersItem_v != null){
            setDefaultBackground(mAllCountersItem_v);
            mAllCountersItem_v = null;
        }

        mSelected_vh = vh;
        selectedGroup.setValue(string);
        setSelectedBackground(mSelected_vh.itemView);

    }

    public LiveData<String> getSelectedItem(){
        return selectedGroup;
    }


    public void bindBackground(RecyclerView.ViewHolder viewHolder){
        if (viewHolder == mSelected_vh){
            setSelectedBackground(viewHolder.itemView);
        }else {
            setDefaultBackground(viewHolder.itemView);
        }
    }

    public void allCountersItemSelected(View view) {
        if (mAllCountersItem_v != null){
            setDefaultBackground(mAllCountersItem_v);
            mAllCountersItem_v = null;
        }

        if (mSelected_vh!=null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh = null;
        }

        // TODO: 2/5/2021 возможно изменить на allCounters
        selectedGroup.setValue(null);
        mAllCountersItem_v = view;
        setSelectedBackground(mAllCountersItem_v);
    }

    private void setDefaultBackground(View view){
        view.setBackgroundResource(R.drawable.item_background);
    }

    private void setSelectedBackground(View view){
        view.setBackgroundResource(R.drawable.item_selected);
    }

}
