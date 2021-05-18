package com.yaroslavgorbachh.counter.screen.counters.drawer;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.databinding.IGroupBinding;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Vh>{
    public interface Callback{
        void onGroup(String string);
    }

    private List<String> mData = new ArrayList<>();
    private final Callback mCallback;
    private final DrawerItemSelector mDrawerItemSelector;

    public GroupsAdapter(Callback callback) {
        mDrawerItemSelector = new CounterDrawerItemSelector();
        mCallback = callback;
    }

    public void setGroups (List<String> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void onAllCountersSelected() {
        mDrawerItemSelector.clearSelected();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(IGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Vh holder, int position) {
        holder.bind(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class Vh extends RecyclerView.ViewHolder {
        private final IGroupBinding mBinding;
        public Vh(IGroupBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(v -> {
                mCallback.onGroup(mData.get(getBindingAdapterPosition()));
                mDrawerItemSelector.selectItem(mData.get(getBindingAdapterPosition()), this);
            });

        }

        public void bind(String s) {
            mBinding.title.setText(s);
        }

    }
}
