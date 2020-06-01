package com.yaroslavgorbach.counter.RecyclerViews;

import android.graphics.drawable.Drawable;
import android.view.GestureDetector;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.Models.Counter;
import com.yaroslavgorbach.counter.RecyclerViews.DividerItemDecoration;

import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.Models.Counter;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.DividerItemDecoration;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.ItemTouchHelperAdapter;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.ItemTouchHelperViewHolder;
import com.yaroslavgorbach.counter.RecyclerViews.DragAndDrop.MyItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class CounterList_rv {

    public interface ItemClickListener {
        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);
    }


    public interface MoveListener {
        void onMove(Counter counterFrom, Counter counterTo);
    }

    private final CounterAdapter mAdapter = new CounterAdapter();
    private ItemClickListener mItemClickListener;
    private MoveListener mMoveListener;
    private ItemTouchHelper mItemTouchHelper;

    public CounterList_rv(RecyclerView rv, ItemClickListener ItemClickListener, MoveListener moveListener) {

        mItemClickListener = ItemClickListener;
        mMoveListener = moveListener;
        ItemTouchHelper.Callback callback = new MyItemTouchHelper(mAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext());
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);
        mItemTouchHelper.attachToRecyclerView(rv);

    }

    public void setCounters(List<Counter> list) {
        mAdapter.setData(list);
    }

    private class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.Vh> implements ItemTouchHelperAdapter {

        private List<Counter> mData = new ArrayList<>();

        private void setData(List<Counter> data) {
            mData = data;
            notifyDataSetChanged();
        }

        CounterAdapter() {
            setHasStableIds(true);
        }

        @NonNull
        @Override
        public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new Vh(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull Vh holder, int position) {
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
            mMoveListener.onMove(mAdapter.mData.get(fromPos), mAdapter.mData.get(toPos));
        }


        private class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder,
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
                    mItemClickListener.onPlusClick(mData.get(getAdapterPosition()));
                    mPlus.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                });

                new FastCountButton(mMinus, () ->{
                    mItemClickListener.onMinusClick(mData.get(getAdapterPosition()));
                    mMinus.performHapticFeedback(HapticFeedbackConstants. LONG_PRESS);
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
                mItemClickListener.onOpen(mData.get(getAdapterPosition()));
                itemView.setPressed(true);
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
             mItemTouchHelper.startDrag(this);
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                return true;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        }

    }

}


