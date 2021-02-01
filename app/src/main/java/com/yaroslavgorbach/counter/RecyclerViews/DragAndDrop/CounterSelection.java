package com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop;

import android.app.Application;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Repo;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;

import java.util.ArrayList;
import java.util.List;

public class CounterSelection {
    private final Repo mRepo;

    public boolean isSelectionMod = false;
    private final List<Counter> mSelectedCounters = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private RecyclerView.ViewHolder mDraggingHolder;

    public CounterSelection(Application application){
        mRepo = new Repo(application);
    }


    public void selectCounter(Counter newCounter, RecyclerView.ViewHolder viewHolder) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                unSelectCounter(oldCounter, viewHolder);
                isAlreadySelected = true;
                break;
            }
        }
            if (!isAlreadySelected) {
                isSelectionMod = true;
                mSelectedCounters.add(newCounter);
                mSelectedVhs.add(viewHolder);
                viewHolder.itemView.setBackgroundResource(R.drawable.item_selected);
            }
        }

    private void unSelectCounter(Counter counter, RecyclerView.ViewHolder viewHolder){
        if (mSelectedVhs.size()==1){
            isSelectionMod = false;
        }
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
        viewHolder.itemView.setBackgroundResource(0);
        viewHolder.itemView.setBackgroundColor(0);
    }

    private void clearAllSelections(){
        isSelectionMod = false;
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            vh.itemView.setBackgroundResource(0);
            vh.itemView.setBackgroundColor(0);
            vh.itemView.setElevation(0F);
        }
        mSelectedVhs.clear();
        mSelectedCounters.clear();
    }

    public void dragHolder(RecyclerView.ViewHolder viewHolder){
        clearAllSelections();
        mDraggingHolder = viewHolder;
        mDraggingHolder.itemView.setBackgroundResource(0);
        mDraggingHolder.itemView.setBackgroundColor(0);
        mDraggingHolder.itemView.setBackgroundResource(R.drawable.item_dragging_2);
        mDraggingHolder.itemView.setElevation(30F);
    }

    public void clearDragHolderBackground(){
        if (mDraggingHolder!=null){
            mDraggingHolder.itemView.setBackgroundResource(0);
            mDraggingHolder.itemView.setBackgroundColor(0);
            mDraggingHolder.itemView.setElevation(0F);
            mDraggingHolder = null;
        }

    }

    public void setVhBackground(Counter newCounter, CountersAdapter.Vh vh) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                vh.itemView.setBackgroundResource(R.drawable.item_selected);
                isAlreadySelected = true;
                break;
            }
        }

        if (!isAlreadySelected) {
            if (mDraggingHolder==null){
                vh.itemView.setBackgroundResource(0);
            }
        }
    }

    public void incSelectedCounters(){
        for (Counter counter:mSelectedCounters){
            counter.value++;
            mRepo.updateCounter(counter);
        }
    }

    public void decSelectedCounters(){
        for (Counter counter:mSelectedCounters){
            counter.value--;
            mRepo.updateCounter(counter);
        }
    }

}
