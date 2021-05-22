package com.yaroslavgorbachh.counter.screen.counters.drawer;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.databinding.ItemGroupBinding;

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
        mDrawerItemSelector = new GroupsDrawerItemSelector();
        mCallback = callback;
    }

    public void setGroups (List<String> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void unselect() {
        mDrawerItemSelector.clearSelected();
        notifyDataSetChanged();
    }

    public void select(String group) {
        mDrawerItemSelector.selectItem(group);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Vh(ItemGroupBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
        private final ItemGroupBinding mBinding;
        public Vh(ItemGroupBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.getRoot().setOnClickListener(v -> {
                mCallback.onGroup(mData.get(getBindingAdapterPosition()));
                mDrawerItemSelector.selectItem(mData.get(getBindingAdapterPosition()));
                notifyDataSetChanged();
            });
        }

        public void bind(String s) {
            mBinding.title.setText(s);
            mDrawerItemSelector.bindBackground(s, this);
        }

    }
}
