package com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Repo;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;

import java.util.ArrayList;
import java.util.List;

public class CounterSelection {
    private final Repo mRepo;

    private final MutableLiveData<Boolean> _isSelectionMod = new MutableLiveData<>(false);
    public LiveData<Boolean> isSelectionMod = _isSelectionMod;
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
                if (!_isSelectionMod.getValue())
                _isSelectionMod.setValue(true);
                mSelectedCounters.add(newCounter);
                mSelectedVhs.add(viewHolder);
                setDefaultBackground(viewHolder);
                viewHolder.itemView.setBackgroundResource(R.drawable.item_selected);
            }
        }

    private void unSelectCounter(Counter counter, RecyclerView.ViewHolder viewHolder){
        if (mSelectedVhs.size()==1){
            _isSelectionMod.setValue(false);
        }
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
        setDefaultBackground(viewHolder);
    }

    public void clearAllSelections(){
        _isSelectionMod.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        mSelectedVhs.clear();
        mSelectedCounters.clear();
    }

    public void dragHolder(RecyclerView.ViewHolder viewHolder){
        clearAllSelections();
        setDefaultBackground(viewHolder);
        mDraggingHolder = viewHolder;
        mDraggingHolder.itemView.setBackgroundResource(R.drawable.item_dragging_2);
        mDraggingHolder.itemView.setElevation(30F);
    }

    public void clearDragHolderBackground(){
        if (mDraggingHolder!=null){
            setDefaultBackground(mDraggingHolder);
            mDraggingHolder = null;
        }

    }

    private void setDefaultBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.setBackgroundResource(0);
        vh.itemView.setBackgroundColor(0);
        vh.itemView.setElevation(0F);
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
