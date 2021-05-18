package com.yaroslavgorbachh.counter.screen.counters;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.ICounterBinding;
import com.yaroslavgorbachh.counter.feature.FastCountButton;
import com.yaroslavgorbachh.counter.feature.multyselection.MultiSelection;


import java.util.ArrayList;
import java.util.List;

public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.Vh> implements DragAndDropItemTouchHelper.CallbackAdapter {

    public interface Callback {
        void onInc(Counter counter);
        void onDec(Counter counter);
        void onOpen(Counter counter);
        void onMoved(Counter counterFrom, Counter counterTo);
        void onMultiSelectionStateChange(boolean isActive);
        void onSelect(int count);
    }

    private final ItemTouchHelper.Callback itemTouchHelperCallback = new DragAndDropItemTouchHelper(this);
    public final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchHelperCallback);
    private final Callback mCallback;
    private List<Counter> mData = new ArrayList<>();
    private final MultiSelection mMultiSelection;

    public CountersAdapter(MultiSelection multiSelection, Callback callback, LifecycleOwner lifecycleOwner) {
        setHasStableIds(true);
        mCallback = callback;
        mMultiSelection = multiSelection;
        mMultiSelection.getIsSelectionActive().observe(lifecycleOwner, callback::onMultiSelectionStateChange);
        multiSelection.getSelectedCount().observe(lifecycleOwner, callback::onSelect);
    }

    public void setData(List<Counter> data) {
        mData = data;
        notifyDataSetChanged();
    }

    public Counter getFirsSelected() {
        return mMultiSelection.getFirstSelected();
    }

    public void selectAllCounters() {
        mMultiSelection.selectAll(mData);
        notifyDataSetChanged();
    }

    public List<Counter> getSelected() {
        return mMultiSelection.getSelected();
    }

    public void unselectAll() {
        mMultiSelection.unselectAll();
    }

    @NonNull
    @Override
    public CountersAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CountersAdapter.Vh(ICounterBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CountersAdapter.Vh holder, int position) {
        holder.bind(mData.get(position));
        mMultiSelection.bindBackground(mData.get(position), holder);
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
            mCallback.onMoved(mData.get(fromPos), mData.get(toPos));
            notifyItemMoved(fromPos, toPos);
            mMultiSelection.unselectAll();
        }
    }

    public class Vh extends RecyclerView.ViewHolder implements DragAndDropItemTouchHelper.CallbackViewHolder,
            View.OnTouchListener, GestureDetector.OnGestureListener {
        private final GestureDetector mGestureDetector;
        private final ICounterBinding mBinding;

        public Vh(ICounterBinding binding) {
            super(binding.getRoot());
            mBinding= binding;
            mGestureDetector = new GestureDetector(binding.getRoot().getContext(), this);
            itemView.setOnTouchListener(this);

            new FastCountButton(binding.inc, () -> {
                if (getBindingAdapterPosition() != -1)
                    mCallback.onInc(mData.get(getBindingAdapterPosition()));
            }, null, null);

            new FastCountButton(binding.dec, () -> {
                if (getBindingAdapterPosition() != -1)
                    mCallback.onDec(mData.get(getBindingAdapterPosition()));
            }, null, null);

        }

        private void bind(Counter counter) {
            mBinding.title.setText(counter.title);
            mBinding.value.setText(String.valueOf(counter.value));
        }

        @Override
        public void clearView(RecyclerView.ViewHolder vh) {
            vh.itemView.setActivated(false);
        }

        @Override
        public void onSelectedChange(RecyclerView.ViewHolder holder) {
            holder.itemView.setActivated(true);
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
            if (mMultiSelection.getIsSelectionActive().getValue()){
                mMultiSelection.select(mData.get(getBindingAdapterPosition()), this);
            }else if (getBindingAdapterPosition() != -1){
                mCallback.onOpen(mData.get(getBindingAdapterPosition()));
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            if (mMultiSelection.getIsSelectionActive().getValue()){
                mMultiSelection.select(mData.get(getBindingAdapterPosition()), this);
            }else {
                itemView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                itemTouchHelper.startDrag(this);
                if(mMultiSelection.getSelected().isEmpty()){
                    mMultiSelection.select(mData.get(getBindingAdapterPosition()), this);
                }
            }
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
                    itemView.setPressed(!mMultiSelection.getIsSelectionActive().getValue());
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
