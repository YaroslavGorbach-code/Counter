package com.yaroslavgorbach.counter.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.MaterialToolbar;
import com.yaroslavgorbach.counter.Fragments.Dialogs.CreateCounterDialog;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners.CounterItemClickListener;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners.CounterItemMovedListener;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.DividerItemDecoration;
import com.yaroslavgorbach.counter.RecyclerViews.GroupList_rv;
import com.yaroslavgorbach.counter.Utility;
import com.yaroslavgorbach.counter.ViewModels.CountersViewModel;
import com.yaroslavgorbach.counter.ViewModels.MainActivityViewModel;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersFragment extends Fragment {
    private CountersViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CountersAdapter mAdapter;
    private String mGroup;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_of_counters_fragment, container, false);
       mViewModel = new ViewModelProvider(this).get(CountersViewModel.class);
       mRecyclerView = view.findViewById(R.id.countersList_rv);
       mGroup = CountersFragmentArgs.fromBundle(getArguments()).getGroup();

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

            if(mGroup!=null && !mGroup.equals("null")){
                mViewModel.getCountersByGroup(mGroup).observe(getViewLifecycleOwner(), counters->{
                    mAdapter.setData(counters);
                    MaterialToolbar materialToolbar = getActivity().findViewById(R.id.toolbar_mainActivity);
                    materialToolbar.setTitle(mGroup);
                });
            }else{
                mViewModel.mCounters.observe(getViewLifecycleOwner(), counters -> {
                    mAdapter.setData(counters);
                });
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
