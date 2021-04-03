package com.yaroslavgorbachh.counter.counterHistory.recyclerView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.counterHistory.recyclerView.DividerItemDecoration;
import com.yaroslavgorbachh.counter.countersList.DragAndDrop.CounterItemTouchHelper;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.R;

import java.util.ArrayList;
import java.util.List;

public class CounterHistoryRv {


    public interface CounterHistorySwipeListener{
        void onSwipe(CounterHistory counterHistory);
    }

    private final Adapter mAdapter = new Adapter();
    private static CounterHistorySwipeListener mCounterHistorySwipeListener;


    public CounterHistoryRv(RecyclerView rv, CounterHistorySwipeListener counterHistorySwipeListener) {
        mCounterHistorySwipeListener = counterHistorySwipeListener;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rv.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rv.getContext());
        ItemTouchHelper.Callback callback = new HistoryItemTouchHelper();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);
        rv.addItemDecoration(dividerItemDecoration);
    }

    public void setHistory(List<CounterHistory> list) {
        mAdapter.setData(list);
    }

    private static class Adapter extends RecyclerView.Adapter<Adapter.Vh> {
        private List<CounterHistory> mData = new ArrayList<>();

        private Adapter() {
            setHasStableIds(true);
        }

        private void setData(List<CounterHistory> data) {
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

        @Override
        public long getItemId(int position) {
            return mData.get(position).id;
        }

        private class Vh extends RecyclerView.ViewHolder implements ItemTouchHelperSwipeListener{
            private final TextView mValue;
            private final TextView mCreateData;

            public Vh(@NonNull ViewGroup parent) {
                super(LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_history_i,
                        parent, false));
                mValue = itemView.findViewById(R.id.history_value);
                mCreateData = itemView.findViewById(R.id.historyData);
            }

            @Override
            public void onSwipe() {
                mCounterHistorySwipeListener.onSwipe(mData.get(getBindingAdapterPosition()));
            }

            private void bind(CounterHistory counterHistory) {
                mValue.setText(String.valueOf(counterHistory.value));
                mCreateData.setText(counterHistory.data);
            }


        }
    }
}

