package com.yaroslavgorbachh.counter.screen.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.data.domain.History;
import com.yaroslavgorbachh.counter.databinding.ItemHistoryBinding;
import com.yaroslavgorbachh.counter.util.TimeAndDataUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Vh> {
    private List<History> mData = new ArrayList<>();
    private Sort mSort = Sort.DATE;

    public enum Sort{
        DATE, VALUE
    }

    public HistoryAdapter() {
        setHasStableIds(true);
    }

    public void setData(List<History> list) {
        mData = (Observable.fromIterable(list)
                .sorted((o1, o2) -> {

                        if (mSort == Sort.VALUE) {
                            return Long.compare(o2.value, o1.value);
                        } else {
                                if (TimeAndDataUtil.convertStringToDate(o1.data)
                                        .before(TimeAndDataUtil.convertStringToDate(o2.data))) {
                                    return 1;
                                } else if (TimeAndDataUtil.convertStringToDate(o1.data)
                                        .after(TimeAndDataUtil.convertStringToDate(o2.data))) {
                                    return -1;
                                } else {
                                    return 0;
                            }
                        }

                }).toList().blockingGet());
        notifyDataSetChanged();
    }
    public void setSort(Sort sort) {
        mSort = sort;
        setData(mData);
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(ItemHistoryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

    public List<History> getData() {
        return mData;
    }

    public static class Vh extends RecyclerView.ViewHolder {
        private final ItemHistoryBinding mBinding;

        public Vh(ItemHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        private void bind(History history) {
            mBinding.value.setText(String.valueOf(history.value));
            mBinding.date.setText(history.data);
        }
    }
}


