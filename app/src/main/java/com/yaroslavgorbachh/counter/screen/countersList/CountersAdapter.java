package com.yaroslavgorbachh.counter.screen.countersList;

import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.feature.FastCountButton;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.CounterItemTouchHelper;
import com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.ItemTouchHelperAdapter;
import com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.ItemTouchHelperViewHolder;
import com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.MultiSelection.MultiSelection;
import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.ArrayList;
import java.util.List;

public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.Vh> implements ItemTouchHelperAdapter {

    private final ItemTouchHelper.Callback callback = new CounterItemTouchHelper(this);
    public final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
    private final MultiSelection mCounterMultiSelection;
    private final CounterItemClickListener mCounterItemClickListener;
    private final SharedPreferences mSharedPreferences;
    private List<Counter> mData = new ArrayList<>();
    private final boolean leftHandMod;

    public CountersAdapter(CounterItemClickListener counterItemClickListener, SharedPreferences sharedPreferences, MultiSelection multiSelection) {
        setHasStableIds(true);
        mCounterMultiSelection = multiSelection;
        mCounterItemClickListener = counterItemClickListener;
        mSharedPreferences = sharedPreferences;
        leftHandMod = mSharedPreferences.getBoolean("leftHandMod", false);
    }

    public void setData(List<Counter> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public void selectAllCounters() {
        mCounterMultiSelection.selectAll(mData);
        notifyDataSetChanged();
    }

    public void clearSelectedCounters() {
        mCounterMultiSelection.clearAllSelections();
        notifyDataSetChanged();
    }

    public boolean getLeftHandMod() {
        return leftHandMod;
    }

    @NonNull
    @Override
    public CountersAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (leftHandMod) {
            return new CountersAdapter.Vh(parent, R.layout.i_counter_left);
        } else {
            return new CountersAdapter.Vh(parent, R.layout.i_counter);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CountersAdapter.Vh holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

    @Override
    public void onMoved(int fromPos, int toPos) {
        if (fromPos != -1 && toPos != -1) {
            Counter deleted = mData.remove(fromPos);
            mData.add(toPos, deleted);
            mCounterItemClickListener.onMoved(mData.get(fromPos), mData.get(toPos));
            notifyItemMoved(fromPos, toPos);
        }
    }

    public interface CounterItemClickListener {
        void onPlusClick(Counter counter);

        void onMinusClick(Counter counter);

        void onOpen(Counter counter);

        void onMoved(Counter counterFrom, Counter counterTo);
    }

    public class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,
            View.OnTouchListener, GestureDetector.OnGestureListener {
        private final GestureDetector mGestureDetector;
        private final TextView mTitle;
        private final TextView mValue;
        private final TextView mPlus;
        private final TextView mMinus;

        public Vh(@NonNull ViewGroup parent, int idLayout) {
            super(LayoutInflater.from(parent.getContext()).inflate(idLayout, parent, false));
            mTitle = itemView.findViewById(R.id.title);
            mValue = itemView.findViewById(R.id.value);
            mPlus = itemView.findViewById(R.id.inc);
            mMinus = itemView.findViewById(R.id.dec);
            mGestureDetector = new GestureDetector(parent.getContext(), this);
            itemView.setOnTouchListener(this);

            new FastCountButton(mPlus, () -> {
                if (getBindingAdapterPosition() != -1)
                    mCounterItemClickListener.onPlusClick(mData.get(getBindingAdapterPosition()));
            }, mSharedPreferences, null);

            new FastCountButton(mMinus, () -> {
                if (getBindingAdapterPosition() != -1)
                    mCounterItemClickListener.onMinusClick(mData.get(getBindingAdapterPosition()));
            }, mSharedPreferences, null);

            /*if selection mod is active we need to disable buttons plus and minos*/
            mCounterMultiSelection.getSelectionModState().observe((LifecycleOwner) itemView.getContext(), aBoolean -> {
                if (aBoolean) {
                    mMinus.setClickable(false);
                    mPlus.setClickable(false);
                    mPlus.setEnabled(false);
                    mMinus.setEnabled(false);
                } else {
                    mMinus.setClickable(true);
                    mPlus.setClickable(true);
                    mPlus.setEnabled(true);
                    mMinus.setEnabled(true);
                }
            });
        }

        private void bind(Counter counter) {
            mTitle.setText(counter.title);
            mValue.setText(String.valueOf(counter.value));
            mCounterMultiSelection.bindBackground(counter, this, itemView.getBackground());
        }


        @Override
        public void clearView() {
            mCounterMultiSelection.stopDragging();
        }

        @Override
        public void onDragging(RecyclerView.ViewHolder holder) {
            mCounterMultiSelection.startDragging(holder);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if (mCounterMultiSelection.getSelectionModState().getValue()) {
                if (getBindingAdapterPosition()!=-1)
                mCounterMultiSelection.select(mData.get(getBindingAdapterPosition()), this);
            } else {
                if (getBindingAdapterPosition()!=-1)
                    mCounterItemClickListener.onOpen(mData.get(getBindingAdapterPosition()));
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (!mCounterMultiSelection.getSelectionModState().getValue()) {
                itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                itemTouchHelper.startDrag(this);
            }
            if (getBindingAdapterPosition() != -1)
                mCounterMultiSelection.select(mData.get(getBindingAdapterPosition()), this);
        }


        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mGestureDetector.onTouchEvent(event);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    itemView.setPressed(!mCounterMultiSelection.getSelectionModState().getValue());
                    break;
                case MotionEvent.ACTION_UP:
                    itemView.performClick();
                    //no break
                case MotionEvent.ACTION_CANCEL:
                    itemView.setPressed(false);
                    break;
            }
            return true;
        }
    }
}
