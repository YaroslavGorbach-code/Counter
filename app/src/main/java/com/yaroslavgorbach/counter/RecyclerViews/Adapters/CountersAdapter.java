package com.yaroslavgorbach.counter.RecyclerViews.Adapters;

import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners.CounterItemClickListener;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners.CounterItemMovedListener;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.ItemTouchHelperAdapter;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.ItemTouchHelperViewHolder;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.MyItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class CountersAdapter extends RecyclerView.Adapter<CountersAdapter.Vh> implements ItemTouchHelperAdapter{

        private List<Counter> mData = new ArrayList<>();
        private final CounterItemClickListener mCounterItemClickListener;
        private final CounterItemMovedListener mCounterItemMovedListener;
        private final ItemTouchHelper.Callback callback = new MyItemTouchHelper(this);
        public ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);

    public CountersAdapter(CounterItemClickListener counterItemClickListener,
                           CounterItemMovedListener counterItemMovedListener) {
        setHasStableIds(true);
        mCounterItemClickListener = counterItemClickListener;
        mCounterItemMovedListener = counterItemMovedListener;
       }

        public void setData(List<Counter> data) {
            mData = data;
            notifyDataSetChanged();
        }

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
        public void onMove(int fromPos, int toPos) {
            Counter deleted = mData.remove(fromPos);
            mData.add(toPos, deleted);
            notifyDataSetChanged();
            mCounterItemMovedListener.onMove(mData.get(fromPos), mData.get(toPos));
        }


        public class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,
                View.OnTouchListener, GestureDetector.OnGestureListener {
            private GestureDetector mGestureDetector;
            private ConstraintLayout mItem;
            private TextView mTitle;
            private TextView mValue;
            private TextView mPlus;
            private TextView mMinus;


            public Vh(@NonNull ViewGroup parent) {

                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_i, parent, false));
                mItem = itemView.findViewById(R.id.counterItem);
                mTitle = itemView.findViewById(R.id.title_i);
                mValue = itemView.findViewById(R.id.value_i);
                mPlus = itemView.findViewById(R.id.plus_i);
                mMinus = itemView.findViewById(R.id.minus_i);
                mGestureDetector = new GestureDetector(parent.getContext(),this);
                itemView.setOnTouchListener(this);

                new FastCountButton(mPlus, () -> {
                    mCounterItemClickListener.onPlusClick(mData.get(getAdapterPosition()));
                    // mPlus.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                });

                new FastCountButton(mMinus, () ->{
                    mCounterItemClickListener.onMinusClick(mData.get(getAdapterPosition()));
                    // mMinus.performHapticFeedback(HapticFeedbackConstants. LONG_PRESS);
                });
            }

            private void bind(Counter counter) {
                mTitle.setText(counter.title);
                mValue.setText(String.valueOf(counter.value));
            }

            @Override
            public void onSelectedChanged() {
                mItem.setBackgroundResource(R.drawable.item_background);
            }

            @Override
            public void clearView() {
                mItem.setBackgroundResource(0);
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
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                itemView.performLongClick();
                itemTouchHelper.startDrag(this);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        itemView.setPressed(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        itemView.performClick();
                        mCounterItemClickListener.onOpen(mData.get(getAdapterPosition()));
                        //no break
                    case MotionEvent.ACTION_CANCEL:
                        itemView.setPressed(false);
                        break;
                }
                return true;
            }
        }

}
