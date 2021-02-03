package com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop;

import android.app.Application;
import android.content.ContentValues;
import android.os.Bundle;
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

    private final MutableLiveData<Boolean> mSelectionMod = new MutableLiveData<>(false);
    public LiveData<Boolean> selectionMod = mSelectionMod;
    private List<Counter> mSelectedCounters = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private RecyclerView.ViewHolder mDraggingHolder;

    private  List<Counter> mCopyBeforeReset;


    public CounterSelection(Application application){
        mRepo = new Repo(application);
    }


    private void setDefaultBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.setBackgroundResource(R.drawable.item_background);
        vh.itemView.setElevation(7F);
        Log.println(Log.VERBOSE,"CounterSelection", "setDefBg");
    }

    private void setItemSelectedBackground(RecyclerView.ViewHolder vh){
        vh.itemView.setBackgroundResource(R.drawable.item_selected);
        vh.itemView.setElevation(8f);
    }

    private void setItemDraggingBackground(RecyclerView.ViewHolder viewHolder){
        mDraggingHolder = viewHolder;
        mDraggingHolder.itemView.setBackgroundResource(R.drawable.item_dragging);
        mDraggingHolder.itemView.setElevation(25F);
    }

    private void unSelectCounter(Counter counter, RecyclerView.ViewHolder viewHolder){
        Log.println(Log.VERBOSE,"CounterSelection", "itemUnSelected");
        setDefaultBackground(viewHolder);
        if (mSelectedVhs.size()==1){
            if (mSelectedCounters.size()==1)
            mSelectionMod.setValue(false);
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
                if (!mSelectionMod.getValue())
                mSelectionMod.setValue(true);
                Log.println(Log.VERBOSE,"CounterSelection", "itemSelected");
                mSelectedCounters.add(newCounter);
                mSelectedVhs.add(viewHolder);
                setItemSelectedBackground(viewHolder);
            }
        }

    public void clearAllSelectedCounters(){
        mSelectionMod.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        Log.println(Log.VERBOSE,"CounterSelection", "selectionsCleared");
        mSelectedVhs.clear();
        mSelectedCounters.clear();
    }

    public void selectAllCounters(List<Counter> counters) {
        mSelectedCounters.clear();
        mSelectedCounters.addAll(counters);
        mSelectionMod.setValue(true);
    }

    public void dragHolder(RecyclerView.ViewHolder viewHolder){
        Log.println(Log.VERBOSE,"CounterSelection", "startDragging");
        clearAllSelectedCounters();
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
            counter.value += counter.step;
            mRepo.updateCounter(counter);
        }
    }

    public void decSelectedCounters(){
        for (Counter counter:mSelectedCounters){
            counter.value -= counter.step;
            mRepo.updateCounter(counter);
        }
    }

    public void resetSelectedCounters() {
        mCopyBeforeReset = new ArrayList<>();
        for (Counter counter : mSelectedCounters) {
            Counter copy = new Counter(counter.title, counter.value,
                    counter.maxValue, counter.minValue, counter.step, counter.grope, counter.createData);
            copy.setId(counter.id);
            mCopyBeforeReset.add(copy);
            counter.value = 0;
            mRepo.updateCounter(counter);
        }
    }

    public void undoReset() {
        for (Counter counter: mCopyBeforeReset) {
            mRepo.updateCounter(counter);
        }
        mSelectedCounters = mCopyBeforeReset;
        mSelectionMod.setValue(mSelectedCounters != null);
    }

    public void deleteSelectedCounters() {
        for (Counter counter: mSelectedCounters) {
            mRepo.deleteCounter(counter);
        }
        mSelectionMod.setValue(false);
    }
}
