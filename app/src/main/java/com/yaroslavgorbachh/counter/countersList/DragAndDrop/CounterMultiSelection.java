package com.yaroslavgorbachh.counter.countersList.DragAndDrop;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.CopyCounterBeforeReset;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.countersList.CountersAdapter;

import java.util.ArrayList;
import java.util.List;

public class CounterMultiSelection implements MultiSelection {

    private final Repo mRepo;
    private  Drawable mDefaultBackground = null;
    private final MutableLiveData<Boolean> mIsMultiSelection = new MutableLiveData<>(false);
    private List<Counter> mSelectedCounters = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private RecyclerView.ViewHolder mDraggingHolder;
    private final MutableLiveData<Integer> mCountSelected = new MutableLiveData<>(mSelectedCounters.size());
    private final Context mContext;
    private final Accessibility mAccessibility;
    public LiveData<Boolean> isMultiSelection = mIsMultiSelection;
    public CopyCounterBeforeReset mCopyCounterBeforeReset;

    public CounterMultiSelection(Repo repo, Context context, Accessibility accessibility) {
        mRepo = repo;
        mContext = context;
        mAccessibility = accessibility;
    }


    @Override
    public void select(Counter counter, RecyclerView.ViewHolder viewHolder) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (counter.id == oldCounter.id) {
                unSelect(oldCounter, viewHolder);
                isAlreadySelected = true;
                break;
            }
        }
        if (!isAlreadySelected) {
            if (!mIsMultiSelection.getValue())
                mIsMultiSelection.setValue(true);

            mSelectedCounters.add(counter);
            mSelectedVhs.add(viewHolder);
            setItemSelectedBackground(viewHolder);
        }
        mCountSelected.setValue(mSelectedCounters.size());
    }

    @Override
    public void unSelect(Counter counter, RecyclerView.ViewHolder viewHolder) {
        setDefaultBackground(viewHolder);
        if (mSelectedCounters.size() == 1)
            mIsMultiSelection.setValue(false);
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
    }

    @Override
    public void selectAll(List<Counter> counters) {
        mSelectedCounters.clear();
        mSelectedCounters.addAll(counters);
        mIsMultiSelection.setValue(true);
        mCountSelected.setValue(counters.size());
    }

    @Override
    public void clearAllSelections() {
        mIsMultiSelection.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        mSelectedVhs.clear();
        mSelectedCounters.clear();
        mCountSelected.setValue(0);
    }


    public void dragHolder(RecyclerView.ViewHolder viewHolder) {
        clearAllSelections();
        setItemDraggingBackground(viewHolder);
    }

    public void clearDragHolderBackground() {
        if (mDraggingHolder != null) {
            setDefaultBackground(mDraggingHolder);
            mDraggingHolder = null;
        }
    }

    public void bindVhBackground(Counter newCounter, CountersAdapter.Vh vh) {
        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                setItemSelectedBackground(vh);
                isAlreadySelected = true;
                break;
            }
        }

        if (!isAlreadySelected) {
            if (mDraggingHolder == null) {
                setDefaultBackground(vh);
            }
        }
    }

    public void incSelectedCounters() {
        for (Counter counter : mSelectedCounters) {
            counter.inc(mContext, mRepo, null);
        }
        mAccessibility.playIncFeedback(null);
    }

    public void decSelectedCounters() {
        for (Counter counter : mSelectedCounters) {
            counter.dec(mContext, mRepo, null);
        }
        mAccessibility.playDecFeedback(null);
    }

    public void resetSelectedCounters() {
        mCopyCounterBeforeReset = new CopyCounterBeforeReset();
        for (Counter counter : mSelectedCounters) {
            mCopyCounterBeforeReset.addCounter(counter);
            counter.reset(mRepo);
        }
        mCountSelected.setValue(mSelectedCounters.size());
    }

    public void undoReset() {
        for (Counter counter : mCopyCounterBeforeReset.getCounters()) {
            mRepo.updateCounter(counter);
        }
        mSelectedCounters = mCopyCounterBeforeReset.getCounters();
        mIsMultiSelection.setValue(mSelectedCounters != null);
        mCountSelected.setValue(mSelectedCounters.size());
        mCopyCounterBeforeReset = null;
    }

    public void deleteSelectedCounters() {
        for (Counter counter : mSelectedCounters) {
            mRepo.deleteCounter(counter);
        }
        mIsMultiSelection.setValue(false);
        mCountSelected.setValue(0);
        mSelectedCounters.clear();
    }

    public LiveData<Integer> getSelectedCountersCount() {
        return mCountSelected;
    }

    public List<Counter> getSelectedCounters() {
        return mSelectedCounters;
    }

    public Counter getSelectedCounter() {
        return mSelectedCounters.get(0);
    }

    public void setDefaultBackground(Drawable background) {
        if (mDefaultBackground == null)
            mDefaultBackground = background;
    }


    private void setDefaultBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.setBackground(mDefaultBackground);
        vh.itemView.findViewById(R.id.counter_item).setBackgroundResource(0);
        vh.itemView.setElevation(7F);
    }

    private void setItemSelectedBackground(RecyclerView.ViewHolder vh) {
        vh.itemView.findViewById(R.id.counter_item).setBackgroundResource(R.drawable.item_selected);
        vh.itemView.setElevation(8f);
    }

    private void setItemDraggingBackground(RecyclerView.ViewHolder viewHolder) {
        mDraggingHolder = viewHolder;
        mDraggingHolder.itemView.setBackground(mDefaultBackground);
        mDraggingHolder.itemView.setElevation(25F);
    }

}
