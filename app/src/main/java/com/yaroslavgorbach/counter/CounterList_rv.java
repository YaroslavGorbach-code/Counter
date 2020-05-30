package com.yaroslavgorbach.counter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbach.counter.Models.Counter;
import com.yaroslavgorbach.counter.RecyclerViews.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class CounterList_rv {

    public interface ItemClickListener {

        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);

    }



    public interface MoveListener{

        void onMove( Counter counter, Counter counter2);

    }

    private final CounterAdapter mAdapter = new CounterAdapter();
    private ItemClickListener mItemClickListener;
    private MoveListener mMoveListener;
    private boolean test = false;

    public CounterList_rv(RecyclerView rv, ItemClickListener ItemClickListener, MoveListener moveListener) {

        mItemClickListener = ItemClickListener;
        mMoveListener = moveListener;

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext());
        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rv);


    }

        public void setCounters (List<Counter> list) {

        mAdapter.setData(list);

        }

        private class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.Vh>  {

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




            private  class Vh extends RecyclerView.ViewHolder {

                private ConstraintLayout mItem;
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
                   mTestTime = itemView.findViewById(R.id.testData);

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
                    mTestTime.setText(counter.createData);

                }

            }

        }


        private ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP
        | ItemTouchHelper.DOWN, 0) {
    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

//        Collections.swap(mAdapter.mData, viewHolder.getAdapterPosition(), target.getAdapterPosition());
//        mAdapter.notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        mMoveListener.onMove(mAdapter.mData.get(viewHolder.getAdapterPosition()),
                mAdapter.mData.get(target.getAdapterPosition()));
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

    }

};

    }

