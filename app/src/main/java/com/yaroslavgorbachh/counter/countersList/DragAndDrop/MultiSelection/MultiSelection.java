package com.yaroslavgorbachh.counter.countersList.DragAndDrop.MultiSelection;

import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.database.Models.Counter;

import java.util.ArrayList;
import java.util.List;

public interface MultiSelection extends DragAndDrop{
    void select(Counter counter, RecyclerView.ViewHolder viewHolder);
    void unSelect(Counter counter, RecyclerView.ViewHolder viewHolder);
    void selectAll(List<Counter> counters);
    void clearAllSelections();
    void bindBackground(Counter counter, RecyclerView.ViewHolder viewHolder, Drawable background);
    LiveData<Boolean> getSelectionModState();
    LiveData<Integer> getSelectedCount();
    List<Counter> getAllSelected();
    Counter getFirstSelected();
}