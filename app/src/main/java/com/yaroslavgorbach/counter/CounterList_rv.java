package com.yaroslavgorbach.counter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CounterList_rv {

    public interface Listener{

        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);

    }



    public interface MoveListener{

        void onMove( Counter counter, Counter counter2, boolean test);

    }

    private final CounterAdapter mAdapter = new CounterAdapter();
    private Listener mListener;
    private MoveListener mMoveListener;
    private boolean test = false;

    public CounterList_rv(RecyclerView rv, Listener Listener, MoveListener moveListener) {

        mListener = Listener;
        mMoveListener = moveListener;

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext());
        rv.setLayoutManager(new WrapContentLinearLayoutManager(rv.getContext()));
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(rv);
    }

        public void setCounters (List<Counter> list) {

        mAdapter.setData(list);

        }

        private class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.Vh> implements ItemTouchHelperAdapter {

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
            public boolean onItemMove(int fromPosition, int toPosition) {

                if (fromPosition < toPosition) {

                    for (int i = fromPosition; i < toPosition; i++) {
                        if (i!=-1){
                            Collections.swap(mData, i, i + 1);
                        }

                    }

                } else {

                    for (int i = fromPosition; i > toPosition; i--) {

                        if (i!=-1){
                            Collections.swap(mData, i, i - 1);
                        }
                    }

                }

                notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onItemMoved(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    int fromPos, RecyclerView.ViewHolder target, int toPos, int x, int y) {

                        mMoveListener.onMove(mData.get(viewHolder.getAdapterPosition()), mData.get(target.getAdapterPosition()),test);

            }


            private  class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

                private FrameLayout mItem;
                private TextView mTitle;
                private TextView mValue;
                private TextView mPlus;
                private TextView mMinus;
                private TextView mTestTime;


                public Vh(@NonNull ViewGroup parent) {

                    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_i, parent, false));
                   mItem = itemView.findViewById(R.id.counterItem);
                   mTitle = itemView.findViewById(R.id.title_i);
                   mValue = itemView.findViewById(R.id.value_i);
                   mPlus = itemView.findViewById(R.id.plus_i);
                   mMinus = itemView.findViewById(R.id.minus_i);
                   mTestTime = itemView.findViewById(R.id.testTime);

                   new FastCountButton(mPlus, ()->
                           mListener.onPlusClick(mData.get(getAdapterPosition())));

                   new FastCountButton(mMinus, ()->
                           mListener.onMinusClick(mData.get(getAdapterPosition())));


                    mItem.setOnClickListener(v->{

                        mListener.onOpen(mData.get(getAdapterPosition()));


                    });

                }

                private void bind(Counter counter){
                    mTitle.setText(counter.title);
                    mValue.setText(String.valueOf(counter.value));
                    mTestTime.setText(counter.createData);

                }

                @Override
                public void onItemSelected() {
                    mItem.setBackgroundColor(Color.LTGRAY);
                }

                @Override
                public void onItemClear() {

                    mItem.setBackgroundColor(0);
                    test = true;

                }
            }

        }


    }

