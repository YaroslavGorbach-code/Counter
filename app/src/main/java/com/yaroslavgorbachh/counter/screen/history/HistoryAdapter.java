package com.yaroslavgorbachh.counter.screen.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.R;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Vh>{

    private List<CounterHistory> mData = new ArrayList<>();

    public HistoryAdapter(){
        setHasStableIds(true);
    }

    public void setData(List<CounterHistory> list) {
        mData = list;
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

    @Override
    public long getItemId(int position) {
        return mData.get(position).id;
    }

        public static class Vh extends RecyclerView.ViewHolder{
            private final TextView mValue;
            private final TextView mCreateData;

            public Vh(@NonNull ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_counter_history,
                        parent, false));
                mValue = itemView.findViewById(R.id.value);
                mCreateData = itemView.findViewById(R.id.date);
            }

            private void bind(CounterHistory counterHistory) {
                mValue.setText(String.valueOf(counterHistory.value));
                mCreateData.setText(counterHistory.data);
            }


        }
    }


