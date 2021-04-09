package com.yaroslavgorbachh.counter.countersList.DragAndDrop.MultiSelection;

import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.CopyCounterBeforeReset;
import com.yaroslavgorbachh.counter.counterHistory.HistoryManager;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.countersList.CountersAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class CounterMultiSelection implements MultiCount {
    private final Accessibility mAccessibility;
    private final Repo mRepo;
    private CopyCounterBeforeReset mCopyCounterBeforeReset;

    private Drawable mDefaultBackground = null;
    private RecyclerView.ViewHolder mDraggingHolder;
    private final Context mContext;

    private final MutableLiveData<Boolean> mMultiSelectionState = new MutableLiveData<>(false);
    private List<Counter> mSelectedCounters = new ArrayList<>();
    private final List<RecyclerView.ViewHolder> mSelectedVhs = new ArrayList<>();
    private final MutableLiveData<Integer> mCountSelected = new MutableLiveData<>(mSelectedCounters.size());


    @Inject
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
            if (!mMultiSelectionState.getValue())
                mMultiSelectionState.setValue(true);

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
            mMultiSelectionState.setValue(false);
        mSelectedCounters.remove(counter);
        mSelectedVhs.remove(viewHolder);
    }

    @Override
    public void selectAll(List<Counter> counters) {
        mSelectedCounters.clear();
        mSelectedCounters.addAll(counters);
        mMultiSelectionState.setValue(true);
        mCountSelected.setValue(counters.size());
    }

    @Override
    public void clearAllSelections() {
        mMultiSelectionState.setValue(false);
        for (RecyclerView.ViewHolder vh : mSelectedVhs) {
            setDefaultBackground(vh);
        }
        mSelectedVhs.clear();
        mSelectedCounters.clear();
        mCountSelected.setValue(0);
    }

    @Override
    public void bindBackground(Counter newCounter, RecyclerView.ViewHolder viewHolder, Drawable background) {
        if (mDefaultBackground == null)
            mDefaultBackground = background;

        boolean isAlreadySelected = false;
        for (Counter oldCounter : mSelectedCounters) {
            if (newCounter.id == oldCounter.id) {
                setItemSelectedBackground(viewHolder);
                isAlreadySelected = true;
                break;
            }
        }

        if (!isAlreadySelected) {
            if (mDraggingHolder == null) {
                setDefaultBackground(viewHolder);
            }
        }
    }

    @Override
    public LiveData<Boolean> getSelectionModState() {
        return mMultiSelectionState;
    }

    @Override
    public LiveData<Integer> getSelectedCount() {
        return mCountSelected;
    }

    @Override
    public List<Counter> getAllSelected() {
        return mSelectedCounters;
    }

    @Override
    public Counter getFirstSelected() {
        return mSelectedCounters.get(0);
    }


    @Override
    public void startDragging(RecyclerView.ViewHolder viewHolder) {
        clearAllSelections();
        setItemDraggingBackground(viewHolder);
    }

    @Override
    public void stopDragging() {
        if (mDraggingHolder != null) {
            setDefaultBackground(mDraggingHolder);
            mDraggingHolder = null;
        }
    }

    @Override
    public void decAll() {
        for (Counter counter : mSelectedCounters) {
            counter.dec(mContext, mRepo, null);
        }
    //we do it here because wen a lot of counters increments simultaneously feedback is incorrect
    mAccessibility.playDecFeedback(null);
    }

    @Override
    public void incAll() {
        for (Counter counter : mSelectedCounters) {
            counter.inc(mContext, mRepo, null);
        }
        //we do it here because wen a lot of counters increments simultaneously feedback is incorrect
        mAccessibility.playIncFeedback(null);
    }

    @Override
    public void resetAll() {
        mCopyCounterBeforeReset = new CopyCounterBeforeReset();
        for (Counter counter : mSelectedCounters) {
            mCopyCounterBeforeReset.addCounter(counter);
            counter.reset(mRepo);
        }
        mCountSelected.setValue(mSelectedCounters.size());
    }

    @Override
    public void undoResetAll() {
        for (Counter counter : mCopyCounterBeforeReset.getCounters()) {
            mRepo.updateCounter(counter);
        }
        mSelectedCounters = mCopyCounterBeforeReset.getCounters();
        mMultiSelectionState.setValue(mSelectedCounters != null);
        mCountSelected.setValue(mSelectedCounters.size());
        mCopyCounterBeforeReset = null;
    }

    @Override
    public void deleteAll() {
        for (Counter counter : mSelectedCounters) {
            mRepo.deleteCounter(counter);
        }
        mMultiSelectionState.setValue(false);
        mCountSelected.setValue(0);
        mSelectedCounters.clear();
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
