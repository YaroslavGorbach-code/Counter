package com.yaroslavgorbach.counter.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners.CounterItemClickListener;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners.CounterItemMovedListener;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.DividerItemDecoration;
import com.yaroslavgorbach.counter.ViewModels.CountersViewModel;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersFragment extends Fragment {
    private CountersViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CountersAdapter mAdapter;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_of_counters_fragment, container, false);
       mViewModel = new ViewModelProvider(this).get(CountersViewModel.class);
       mRecyclerView = view.findViewById(R.id.countersList_rv);

        mAdapter = new CountersAdapter(new CounterItemClickListener() {
            @Override
            public void onPlusClick(Counter counter) {
                mViewModel.incCounter(counter);
            }

            @Override
            public void onMinusClick(Counter counter) {
                mViewModel.decCounter(counter);
            }

            @Override
            public void onOpen(Counter counter) {
                NavDirections action = CountersFragmentDirections
                        .actionCountersFragmentToCounterFragment().setCounterId(counter.id);
                Navigation.findNavController(getView()).navigate(action);
            }
        }, new CounterItemMovedListener() {
            @Override
            public void onMove(Counter counterFrom, Counter counterTo) {
                mViewModel.countersMoved(counterFrom, counterTo);
            }
        });
            /*if there are no arguments, then set all the
            counters in the list; if there are then the detectors
            which belong to the group indicated in the arguments
            updates the list of counters if something changes
            in the counter_table*/

        if(getArguments()!=null){
            mViewModel.getCountersByGroup(getArguments().getString("group_title"))
                    .observe(getViewLifecycleOwner(), counters -> mAdapter.setData(counters));
        }else{
            mViewModel.getAllCounters()
                    .observe(getViewLifecycleOwner(), counters -> mAdapter.setData(counters));
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mAdapter.itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.println(Log.VERBOSE,"navigation","onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.println(Log.VERBOSE,"navigation","onPAUSE");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.println(Log.VERBOSE,"navigation","ondestroy");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.println(Log.VERBOSE,"navigation","onCreateView");
    }
}
