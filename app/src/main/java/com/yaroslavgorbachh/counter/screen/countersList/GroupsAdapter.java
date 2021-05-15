package com.yaroslavgorbachh.counter.screen.countersList;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.screen.countersList.navigationDrawer.DrawerItemSelector;

import java.util.ArrayList;
import java.util.List;

public class GroupsAdapter extends RecyclerView.Adapter<GroupsAdapter.Vh>{

    private final DrawerItemSelector mCounterDrawerItemSelector;
    private List<String> mData = new ArrayList<>();

    public GroupsAdapter(DrawerItemSelector drawerItemSelector) {
        mCounterDrawerItemSelector = drawerItemSelector;
    }

    public void setGroups (List<String> list) {
        mData = list;
        notifyDataSetChanged();
    }

    public void allCountersItemSelected(View view) {
        mCounterDrawerItemSelector.allCountersItemSelected(view);
        notifyDataSetChanged();
    }

    // this method needs for restoring already selected item after onStop
    public void selectItem(String string) {
        mCounterDrawerItemSelector.selectItem(string, null);
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


    public class Vh extends RecyclerView.ViewHolder implements View.OnTouchListener {
        private final TextView mTitle;

        public Vh(@NonNull ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.i_group, parent, false));
            mTitle = itemView.findViewById(R.id.group_title);
            itemView.setOnTouchListener(this);

        }

        public void bind(String s) {
            mTitle.setText(s);
            mCounterDrawerItemSelector.bindBackground(s,this);
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    itemView.setPressed(true);
                    break;
                case MotionEvent.ACTION_UP:
                    itemView.performClick();
                    mCounterDrawerItemSelector.selectItem(mData.get(getBindingAdapterPosition()), this);
                    notifyDataSetChanged();
                    //no break
                case MotionEvent.ACTION_CANCEL:
                    itemView.setPressed(false);
                    break;
            }
            return true;
        }
    }
}
