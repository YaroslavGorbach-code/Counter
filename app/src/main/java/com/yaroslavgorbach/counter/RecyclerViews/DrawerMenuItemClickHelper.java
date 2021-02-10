package com.yaroslavgorbach.counter.RecyclerViews;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.R;

public class DrawerMenuItemClickHelper {

    private final MutableLiveData<String> selectedItem = new MutableLiveData<>();
    private RecyclerView.ViewHolder mSelected_vh;
    private View mAllCountersItem_v;

    public void selectRvItem(String string, RecyclerView.ViewHolder vh){

        if (mSelected_vh != null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh = null;
        }

        if (mAllCountersItem_v != null){
            setDefaultBackground(mAllCountersItem_v);
            mAllCountersItem_v = null;
        }

        mSelected_vh = vh;
        selectedItem.setValue(string);
        setSelectedBackground(mSelected_vh.itemView);

    }

    public LiveData<String> getSelectedItem(){
        return selectedItem;
    }

    public void bindBackground(String s, RecyclerView.ViewHolder viewHolder){
        if (mSelected_vh!=null){
            setDefaultBackground(mSelected_vh.itemView);
            mSelected_vh=null;
        }
        if (s.equals(selectedItem.getValue())) {
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

        selectedItem.setValue(view.getResources().getString(R.string.AllCountersItem));
        mAllCountersItem_v = view;
        setSelectedBackground(mAllCountersItem_v);
    }

    private void setDefaultBackground(View view){
        view.setBackgroundResource(0);
    }

    private void setSelectedBackground(View view){
        view.setBackgroundResource(R.drawable.group_item_selected);
    }

    public void restoreSelectedItem(String string) {
        selectedItem.setValue(string);
    }
}
