package com.yaroslavgorbach.counter.RecyclerViews.Adapters;

import android.app.Application;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.ItemTouchHelperAdapter;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.ItemTouchHelperViewHolder;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.MyItemTouchHelper;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.CounterSelection;

import java.util.ArrayList;
import java.util.List;

public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.Vh> implements ItemTouchHelperAdapter{

    public interface CounterItemListeners {
        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);
        void onMoved(Counter counterFrom, Counter counterTo);
    }

        public LiveData<Boolean> selectionMod;

        private final CounterSelection mCounterSelection;
        private List<Counter> mData = new ArrayList<>();
        private final CounterItemListeners mCounterItemListeners;
        private final ItemTouchHelper.Callback callback = new MyItemTouchHelper(this);
        public ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

        public CountersAdapter(CounterItemListeners counterItemListeners, Application application) {
            setHasStableIds(true);
            mCounterSelection = new CounterSelection(application);
            mCounterItemListeners = counterItemListeners;
            selectionMod = mCounterSelection.selectionMod;
        }


        public void setData(List<Counter> data) {
            mData = data;
            notifyDataSetChanged();
        }
        
        public void selectAllCounters(){
            mCounterSelection.selectAllCounters(mData);
            notifyDataSetChanged();
        }

         public void clearSelectedCounters(){
            mCounterSelection.clearAllSelectedCounters();
            notifyDataSetChanged();
         }

         public void decSelectedCounters() {
            mCounterSelection.decSelectedCounters();
         }

         public void incSelectedCounters() {
            mCounterSelection.incSelectedCounters();
         }

         public void resetSelectedCounters() {
            mCounterSelection.resetSelectedCounters();
         }

         public void undoReset() {
            mCounterSelection.undoReset();
         }

         public void deleteSelectedCounters() {
            mCounterSelection.deleteSelectedCounters();
         }

         public LiveData<Integer> getSelectedCountersCount(){return mCounterSelection.getSelectedCountersCount();}

    @NonNull
        @Override
        public CountersAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CountersAdapter.Vh(parent);
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
            Counter deleted = mData.remove(fromPos);
            mData.add(toPos, deleted);
            notifyItemMoved(fromPos, toPos);
            mCounterItemListeners.onMoved(mData.get(fromPos), mData.get(toPos));
        }


        public class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,
                View.OnTouchListener, GestureDetector.OnGestureListener {
            private final GestureDetector mGestureDetector;
            private final TextView mTitle;
            private final TextView mValue;
            private final TextView mPlus;
            private final TextView mMinus;

            public Vh(@NonNull ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_i, parent, false));
                mTitle = itemView.findViewById(R.id.title_i);
                mValue = itemView.findViewById(R.id.value_i);
                mPlus = itemView.findViewById(R.id.plus_i);
                mMinus = itemView.findViewById(R.id.minus_i);
                mGestureDetector = new GestureDetector(parent.getContext(),this);
                itemView.setOnTouchListener(this);

                mCounterSelection.selectionMod.observe((LifecycleOwner) itemView.getContext(), aBoolean -> {
                    if (aBoolean){
                        mMinus.setClickable(false);
                        mPlus.setClickable(false);
                        mPlus.setEnabled(false);
                        mMinus.setEnabled(false);
                    }else {
                        mMinus.setClickable(true);
                        mPlus.setClickable(true);
                        mPlus.setEnabled(true);
                        mMinus.setEnabled(true);
                    }
                });

                new FastCountButton(mPlus, () -> {
                     mCounterItemListeners.onPlusClick(mData.get(getBindingAdapterPosition()));
                    // mPlus.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                });

                new FastCountButton(mMinus, () ->{
                     mCounterItemListeners.onMinusClick(mData.get(getBindingAdapterPosition()));
                    // mMinus.performHapticFeedback(HapticFeedbackConstants. LONG_PRESS);
                });
            }

            private void bind(Counter counter) {
                mTitle.setText(counter.title);
                mValue.setText(String.valueOf(counter.value));
                mCounterSelection.bingVhBackground(counter,this);


            }

            @Override
            public void onSelectedChanged() {
            }

            @Override
            public void clearView() {
                mCounterSelection.clearDragHolderBackground();
            }

            @Override
            public void onDragging(RecyclerView.ViewHolder holder) {
                mCounterSelection.dragHolder(holder);
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
                if (mCounterSelection.selectionMod.getValue()){
                    mCounterSelection.selectCounter(mData.get(getBindingAdapterPosition()),this);
                }else {
                    mCounterItemListeners.onOpen(mData.get(getBindingAdapterPosition()));
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                itemView.performHapticFeedback(HapticFeedbackConstants. LONG_PRESS);
                if(!mCounterSelection.selectionMod.getValue()){
                    itemTouchHelper.startDrag(this);
                }
                mCounterSelection.selectCounter(mData.get(getBindingAdapterPosition()),this);
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
                        itemView.setPressed(!mCounterSelection.selectionMod.getValue());
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
