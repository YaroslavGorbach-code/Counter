package com.yaroslavgorbachh.counter.RecyclerViews;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Models.CounterHistory;

import java.util.ArrayList;
import java.util.List;

public class CounterHistoryList_rv {

    public interface HistoryItemClickListener{
        void onDelete(CounterHistory counterHistory);
    }

    private final Adapter mAdapter = new Adapter();
    private final HistoryItemClickListener mHistoryItemClickListener;

    public CounterHistoryList_rv(RecyclerView rv, HistoryItemClickListener historyItemClickListener) {
        mHistoryItemClickListener = historyItemClickListener;
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
                private TextView mCreateData;
                private ImageView mHistoryDelete;



                public Vh(@NonNull ViewGroup parent) {

                    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_history_i, parent, false));

                   mValue = itemView.findViewById(R.id.history_value);
                   mCreateData = itemView.findViewById(R.id.historyData);
                   mHistoryDelete = itemView.findViewById(R.id.deleteHistory);

                   mHistoryDelete.setOnClickListener(v ->{
                       if(getAdapterPosition()!=-1) {
                           mHistoryItemClickListener.onDelete(mData.get(getAdapterPosition()));
                       }
                   });

                }

                private void bind(CounterHistory counterHistory){

                    mValue.setText(String.valueOf(counterHistory.value));
                    mCreateData.setText(counterHistory.data);

                }
            }

        }


    }

