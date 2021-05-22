package com.yaroslavgorbachh.counter.screen.counters.multyselection;

import android.graphics.drawable.Drawable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.ArrayList;
import java.util.List;

public class CounterMultiSelection implements MultiSelection {
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private Drawable mDefaultBackground = null;
    private final MutableLiveData<Boolean> mIaSelectionActive = new MutableLiveData<>(false);
    private final List<Counter> mSelectedCounters = new ArrayList<>();
    private final MutableLiveData<Integer> mSelectedCount = new MutableLiveData<>(mSelectedCounters.size());

    @Override
    public void select(Counter counter, RecyclerView.ViewHolder viewHolder) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (counter.id == oldCounter.id) {
                unselect(oldCounter, viewHolder);
                isAlreadySelected = true;
                break;
            }
        }
        if (!isAlreadySelected) {
            if (!mIaSelectionActive.getValue())
                mIaSelectionActive.setValue(true);

            mSelectedCounters.add(counter);
            mSelectedVhs.add(viewHolder);
            setSelectedBackground(viewHolder);
        }
        mSelectedCount.setValue(mSelectedCounters.size());
    }

    @Override
    public void unselect(Counter counter, RecyclerView.ViewHolder viewHolder) {
        setDefaultBackground(viewHolder);
        if (mSelectedCounters.size() == 1)
            mIaSelectionActive.setValue(false);
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
    }

    @Override
    public void selectAll(List<Counter> counters) {
        mSelectedCounters.clear();
        mSelectedCounters.addAll(counters);
        mIaSelectionActive.setValue(true);
        mSelectedCount.setValue(counters.size());
    }

    @Override
    public void bindBackground(Counter counter, RecyclerView.ViewHolder viewHolder) {
        if (mDefaultBackground == null) mDefaultBackground = viewHolder.itemView.getBackground();

        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (counter.id == oldCounter.id) {
                setSelectedBackground(viewHolder);
                isAlreadySelected = true;
                break;
            }
        }

        if (!isAlreadySelected) setDefaultBackground(viewHolder);
    }

    @Override
    public LiveData<Integer> getSelectedCount() {
        return mSelectedCount;
    }

    @Override
    public List<Counter> getSelected() {
        return mSelectedCounters;
    }

    @Override
    public Counter getFirstSelected() {
        return mSelectedCounters.get(0);
    }

    @Override
    public LiveData<Boolean> getIsSelectionActive() {
        return mIaSelectionActive;
    }

    @Override
    public void unselectAll() {
        mIaSelectionActive.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        mSelectedVhs.clear();
        mSelectedCounters.clear();
        mSelectedCount.setValue(0);
    }

    private void setDefaultBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.findViewById(R.id.item).setBackgroundResource(0);
    }

    private void setSelectedBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.findViewById(R.id.item).setBackgroundResource(R.drawable.i_counter_selected_bg);
    }

}
