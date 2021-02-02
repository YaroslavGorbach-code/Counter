package com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop;

import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;

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
    private final Application application;

    private final MutableLiveData<Boolean> _isSelectionMod = new MutableLiveData<>(false);
    public LiveData<Boolean> isSelectionMod = _isSelectionMod;
    private final List<Counter> mSelectedCounters = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private RecyclerView.ViewHolder mDraggingHolder;

    public CounterSelection(Application application){
        mRepo = new Repo(application);
        this.application = application;
    }


    private void setDefaultBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.setBackgroundResource(R.drawable.item_background);
        vh.itemView.setElevation(0F);
        Log.println(Log.VERBOSE,"CounterSelection", "setDefBg");
    }

    private void setItemSelectedBackground(RecyclerView.ViewHolder vh){
        vh.itemView.setBackgroundResource(R.drawable.item_selected);
        vh.itemView.setElevation(20);
    }

    private void setItemDraggingBackground(RecyclerView.ViewHolder viewHolder){
        mDraggingHolder = viewHolder;
        mDraggingHolder.itemView.setBackgroundResource(R.drawable.item_dragging_2);
        mDraggingHolder.itemView.setElevation(20F);
    }

    private void unSelectCounter(Counter counter, RecyclerView.ViewHolder viewHolder){
        Log.println(Log.VERBOSE,"CounterSelection", "itemUnSelected");
        setDefaultBackground(viewHolder);
        if (mSelectedVhs.size()==1){
            _isSelectionMod.setValue(false);
        }
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
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
                Log.println(Log.VERBOSE,"CounterSelection", "itemSelected");
                mSelectedCounters.add(newCounter);
                mSelectedVhs.add(viewHolder);
                setItemSelectedBackground(viewHolder);
            }
        }

    public void clearAllSelections(){
        _isSelectionMod.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        Log.println(Log.VERBOSE,"CounterSelection", "selectionsCleared");
        mSelectedVhs.clear();
        mSelectedCounters.clear();
    }

    public void dragHolder(RecyclerView.ViewHolder viewHolder){
        Log.println(Log.VERBOSE,"CounterSelection", "startDragging");
        clearAllSelections();
        setItemDraggingBackground(viewHolder);
    }

    public void clearDragHolderBackground(){
        if (mDraggingHolder!=null){
            setDefaultBackground(mDraggingHolder);
            mDraggingHolder = null;
        }

    }

    public void bingVhBackground(Counter newCounter, CountersAdapter.Vh vh) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                setItemSelectedBackground(vh);
                isAlreadySelected = true;
                break;
            }
        }

        if (!isAlreadySelected) {
            if (mDraggingHolder==null){
                setDefaultBackground(vh);
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
