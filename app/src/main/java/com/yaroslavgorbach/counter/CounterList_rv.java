package com.yaroslavgorbach.counter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CounterList_rv {

    public interface Listener{

        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);

    }

    private final CounterAdapter mAdapter = new CounterAdapter();
    private Listener mListener;

    public CounterList_rv(RecyclerView rv, Listener Listener) {

        mListener = Listener;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rv.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext(), DividerItemDecoration.VERTICAL);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);
    }

        public void setCounters (List<Counter> list) {

        mAdapter.setData(list);

        }

        private  class CounterAdapter extends RecyclerView.Adapter<CounterAdapter.Vh> {
            private List<Counter> mData = new ArrayList<>();
            private void setData(List<Counter> data) {
                mData = data;
                notifyDataSetChanged();
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


            private  class Vh extends RecyclerView.ViewHolder {

                private FrameLayout mItem;
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

                   mPlus.setOnClickListener(v->{

                       mListener.onPlusClick(mData.get(getAdapterPosition()));

                   });

                    mMinus.setOnClickListener(v->{

                        mListener.onMinusClick(mData.get(getAdapterPosition()));

                    });

                    mItem.setOnClickListener(v->{

                        mListener.onOpen(mData.get(getAdapterPosition()));

                    });

                }

                private void bind(Counter counter){
                    mTitle.setText(counter.title);
                    mValue.setText(String.valueOf(counter.value));

                }
            }

        }


    }

