package com.yaroslavgorbach.counter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.h6ah4i.android.widget.advrecyclerview.draggable.DraggableItemAdapter;
import com.h6ah4i.android.widget.advrecyclerview.draggable.ItemDraggableRange;
import com.h6ah4i.android.widget.advrecyclerview.draggable.RecyclerViewDragDropManager;
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractDraggableItemViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CounterList_rv {

    public interface MoveListener{

        void onMove(Counter counterFrom, Counter counterTo);

    }

    public interface ItemClickListener {

        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);

    }

    private final CounterAdapter mAdapter = new CounterAdapter();
    private ItemClickListener mItemClickListener;
    private MoveListener mMoveListener;

    public CounterList_rv(RecyclerView rv, ItemClickListener itemClickListener,
                          MoveListener moveListener) {

        mMoveListener = moveListener;
        mItemClickListener = itemClickListener;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rv.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext());
        RecyclerViewDragDropManager dragDropManager = new RecyclerViewDragDropManager();
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);
        rv.setLayoutManager(mLayoutManager);

        RecyclerView.Adapter  wrappedAdapter = dragDropManager.createWrappedAdapter(mAdapter);
        rv.setAdapter(wrappedAdapter);

        // disable change animations
                ((SimpleItemAnimator) Objects.requireNonNull(rv.getItemAnimator())).setSupportsChangeAnimations(false);

         dragDropManager.setInitiateOnTouch(false);
         dragDropManager.setInitiateOnMove(false);
         dragDropManager.setInitiateOnLongPress(true);
         dragDropManager.attachRecyclerView(rv);



    }

        public void setCounters (List<Counter> list) {

        mAdapter.setData(list);

        }

        private class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.Vh>
                implements DraggableItemAdapter {

            private List<Counter> mData = new ArrayList<>();
            private void setData(List<Counter> data) {
                mData = data;
                notifyDataSetChanged();
            }

            CounterAdapter(){
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
            public boolean onCheckCanStartDrag(@NonNull RecyclerView.ViewHolder holder, int position, int x, int y) {
                View itemView = holder.itemView;
                View dragHandle = itemView.findViewById(R.id.dragHandle);

                int handleWidth = dragHandle.getWidth();
                int handleHeight = dragHandle.getHeight();
                int handleLeft = dragHandle.getLeft();
                int handleTop = dragHandle.getTop();

                return (x >= handleLeft) && (x < handleLeft + handleWidth) &&
                        (y >= handleTop) && (y < handleTop + handleHeight);
            }

            @Nullable
            @Override
            public ItemDraggableRange onGetItemDraggableRange(@NonNull RecyclerView.ViewHolder holder, int position) {

                return null;

            }

            @Override
            public void onMoveItem(int fromPosition, int toPosition) {
                Counter removed = mData.remove(fromPosition);
                mData.add(toPosition, removed);
                mMoveListener.onMove(removed, mData.get(toPosition));

            }

            @Override
            public boolean onCheckCanDrop(int draggingPosition, int dropPosition) {

                return false;
            }

            @Override
            public void onItemDragStarted(int position) {
                notifyDataSetChanged();
            }

            @Override
            public void onItemDragFinished(int fromPosition, int toPosition, boolean result) {
                notifyDataSetChanged();

            }

            private  class Vh extends AbstractDraggableItemViewHolder {

                private ConstraintLayout mItem;
                private TextView mTitle;
                private TextView mValue;
                private TextView mPlus;
                private TextView mMinus;

                private TextView mTestDta;


                public Vh(@NonNull ViewGroup parent) {

                    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_i, parent, false));
                   mItem = itemView.findViewById(R.id.counterItem);
                   mTitle = itemView.findViewById(R.id.title_i);
                   mValue = itemView.findViewById(R.id.value_i);
                   mPlus = itemView.findViewById(R.id.plus_i);
                   mMinus = itemView.findViewById(R.id.minus_i);
                   mTestDta = itemView.findViewById(R.id.testData);

                   new FastCountButton(mPlus, ()->
                           mItemClickListener.onPlusClick(mData.get(getAdapterPosition())));

                   new FastCountButton(mMinus, ()->
                           mItemClickListener.onMinusClick(mData.get(getAdapterPosition())));


                    mItem.setOnClickListener(v->{

                        mItemClickListener.onOpen(mData.get(getAdapterPosition()));

                    });

                }

                private void bind(Counter counter){
                    mTitle.setText(counter.title);
                    mValue.setText(String.valueOf(counter.value));
                    mTestDta.setText(counter.createData);

                }
            }

        }


    }

