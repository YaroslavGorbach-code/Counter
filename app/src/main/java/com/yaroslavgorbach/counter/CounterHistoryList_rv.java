package com.yaroslavgorbach.counter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CounterHistoryList_rv {


    private final Adapter mAdapter = new Adapter();

    public CounterHistoryList_rv(RecyclerView rv) {

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rv.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);
    }

        public void setHistory (List<CounterHistory> list) {

        mAdapter.setData(list);

        }

        private class Adapter extends RecyclerView.Adapter<Adapter.Vh> {

            private List<CounterHistory> mData = new ArrayList<>();
            private void setData(List<CounterHistory> data) {
                mData = data;
                notifyDataSetChanged();
            }

            Adapter(){
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


                private TextView mValue;
                private TextView mData;



                public Vh(@NonNull ViewGroup parent) {

                    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_history_i, parent, false));

                   mValue = itemView.findViewById(R.id.history_value);
                   mData = itemView.findViewById(R.id.historyData);

                }

                private void bind(CounterHistory counterHistory){

                    mValue.setText(String.valueOf(counterHistory.value));
                    mData.setText(counterHistory.data);

                }
            }

        }


    }
