package com.yaroslavgorbachh.counter.screen.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.databinding.ICounterHistoryBinding;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Vh> {

    private List<CounterHistory> mData = new ArrayList<>();

    public HistoryAdapter() {
        setHasStableIds(true);
    }

    public void setData(List<CounterHistory> list) {
        mData = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(ICounterHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

    public static class Vh extends RecyclerView.ViewHolder {
        private final ICounterHistoryBinding mBinding;

        public Vh(ICounterHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(CounterHistory counterHistory) {
            mBinding.value.setText(String.valueOf(counterHistory.value));
            mBinding.date.setText(counterHistory.data);
        }


    }
}


