package com.yaroslavgorbachh.counter.RecyclerViews.Adapters;

import android.app.Application;
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
import androidx.lifecycle.LiveData;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;
import com.yaroslavgorbachh.counter.FastCountButton;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.RecyclerViews.DragAndDrop.ItemTouchHelperAdapter;
import com.yaroslavgorbachh.counter.RecyclerViews.DragAndDrop.ItemTouchHelperViewHolder;
import com.yaroslavgorbachh.counter.RecyclerViews.DragAndDrop.MyItemTouchHelper;
import com.yaroslavgorbachh.counter.RecyclerViews.DragAndDrop.CounterMultiSelection;

import java.util.ArrayList;
import java.util.List;

public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.Vh> implements ItemTouchHelperAdapter {

    public interface CounterItemListener {
        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);
        void onMoved(Counter counterFrom, Counter counterTo);
    }

        private final CounterMultiSelection mCounterMultiSelection;
        private List<Counter> mData = new ArrayList<>();
        private final CounterItemListener mCounterItemListener;
        private final ItemTouchHelper.Callback callback = new MyItemTouchHelper(this);
        public ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        public final boolean leftHandMod;

        public CountersAdapter(CounterItemListener counterItemListener, Application application) {
            setHasStableIds(true);
            mCounterMultiSelection = new CounterMultiSelection(new Repo(application), application, application.getResources());
            mCounterItemListener = counterItemListener;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application);
            leftHandMod = sharedPreferences.getBoolean("leftHandMod", false);
        }

        public void setData(List<Counter> data) {
            mData = data;
            notifyDataSetChanged();
        }

        public void selectAllCounters(){
            mCounterMultiSelection.selectAll(mData);
            notifyDataSetChanged();
        }

         public void clearSelectedCounters(){
            mCounterMultiSelection.clearAllSelections();
            notifyDataSetChanged();
         }

         public void decSelectedCounters() {
            mCounterMultiSelection.decSelectedCounters();
         }

         public void incSelectedCounters() {
            mCounterMultiSelection.incSelectedCounters();
         }

         public void resetSelectedCounters() {
            mCounterMultiSelection.resetSelectedCounters();
         }

         public void undoReset() {
            mCounterMultiSelection.undoReset();
         }

         public void deleteSelectedCounters() {
            mCounterMultiSelection.deleteSelectedCounters();
         }

         public LiveData<Integer> getSelectedCountersCount(){return mCounterMultiSelection.getSelectedCountersCount();}

         public Counter getSelectedCounter() {
            return mCounterMultiSelection.getSelectedCounter();
          }

         public List<Counter> getSelectedCounters(){
            return mCounterMultiSelection.getSelectedCounters();
         }

         public LiveData<Boolean> getSelectionMod(){
            return mCounterMultiSelection.isMultiSelection;
         }

        @NonNull
        @Override
        public CountersAdapter.Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            if (leftHandMod){
                return new CountersAdapter.Vh(parent, R.layout.counter_left_hend_i);
            }else {
                return new CountersAdapter.Vh(parent, R.layout.counter_i);
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
            if (fromPos != -1 && toPos != -1){
                Counter deleted = mData.remove(fromPos);
                mData.add(toPos, deleted);
                mCounterItemListener.onMoved(mData.get(fromPos), mData.get(toPos));
                notifyItemMoved(fromPos, toPos);
            }
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
                mTitle = itemView.findViewById(R.id.title_i);
                mValue = itemView.findViewById(R.id.value_i);
                mPlus = itemView.findViewById(R.id.plus_i);
                mMinus = itemView.findViewById(R.id.minus_i);
                mGestureDetector = new GestureDetector(parent.getContext(),this);
                itemView.setOnTouchListener(this);

                 new FastCountButton(mPlus, () -> {
                    if (getBindingAdapterPosition()!=-1)
                        mCounterItemListener.onPlusClick(mData.get(getBindingAdapterPosition()));
                });

                new FastCountButton(mMinus, () ->{
                    if (getBindingAdapterPosition()!=-1)
                        mCounterItemListener.onMinusClick(mData.get(getBindingAdapterPosition()));
                });


            }

            private void bind(Counter counter) {
                mTitle.setText(counter.title);
                mValue.setText(String.valueOf(counter.value));
                mCounterMultiSelection.setDefaultBackground(itemView.getBackground());
                mCounterMultiSelection.bindVhBackground(counter,this);

                /*if selection mod is active we need to disable buttons plus and minos*/
                mCounterMultiSelection.isMultiSelection.observe((LifecycleOwner) itemView.getContext(), aBoolean -> {
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

            }


            @Override
            public void clearView() {
                mCounterMultiSelection.clearDragHolderBackground();
            }

            @Override
            public void onDragging(RecyclerView.ViewHolder holder) {
                mCounterMultiSelection.dragHolder(holder);
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) { }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mCounterMultiSelection.isMultiSelection.getValue()){
                    mCounterMultiSelection.select(mData.get(getBindingAdapterPosition()),this);
                }else {
                    mCounterItemListener.onOpen(mData.get(getBindingAdapterPosition()));
                }
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if(!mCounterMultiSelection.isMultiSelection.getValue()){
                    itemView.performHapticFeedback(HapticFeedbackConstants. LONG_PRESS);
                    itemTouchHelper.startDrag(this);
                }
                if (getBindingAdapterPosition()!=-1)
                mCounterMultiSelection.select(mData.get(getBindingAdapterPosition()),this);
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
                        itemView.setPressed(!mCounterMultiSelection.isMultiSelection.getValue());
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
