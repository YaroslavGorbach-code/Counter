package com.yaroslavgorbachh.counter.screen.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;

import java.util.ArrayList;
import java.util.List;

public class WidgetsAdapter extends RecyclerView.Adapter<WidgetsAdapter.Vh> {
    public interface Callback {
        void onClick(Counter counter);
    }

    private List<Counter> mData = new ArrayList<>();
    private final Callback mListener;

    WidgetsAdapter(Callback clickListener){
        mListener = clickListener;
    }

    public void setData(List<Counter> counters){
        mData = counters;
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

   public class Vh extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView value;
        public Vh(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_widget, parent, false));
            title = itemView.findViewById(R.id.title);
            value = itemView.findViewById(R.id.value);
            itemView.setOnClickListener(v -> mListener.onClick(mData.get(getBindingAdapterPosition())));
        }

        public void bind(Counter counter) {
            title.setText(counter.title);
            value.setText(String.valueOf(counter.value));
        }
    }
}
