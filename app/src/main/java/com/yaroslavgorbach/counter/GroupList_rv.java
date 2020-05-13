package com.yaroslavgorbach.counter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GroupList_rv {

    public interface Listener{

        void onOpen(String tittle);

    }

    private final GroupAdapter mAdapter = new GroupAdapter();
    private Listener mListener;

    public GroupList_rv(RecyclerView rv, Listener Listener) {

        mListener = Listener;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rv.getContext());
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(mAdapter);
        rv.setHasFixedSize(true);

    }

        public void setGroups (List<String> list) {

        mAdapter.setData(list);

        }

        private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.Vh> {
            private List<String> mData = new ArrayList<>();

            private void setData(List<String> data) {

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
                private LinearLayout mItem;
                private TextView mTitle;

                public Vh(@NonNull ViewGroup parent) {

                    super(LayoutInflater.from(parent.getContext()).inflate(R.layout.group_i, parent, false));
                   mItem = itemView.findViewById(R.id.item_group_i);
                   mTitle = itemView.findViewById(R.id.group_title_i);

                    mItem.setOnClickListener(v-> mListener.onOpen(mData.get(getAdapterPosition())));

                }

                private void bind(String string){

                    mTitle.setText(string);

                }
            }

        }


    }

