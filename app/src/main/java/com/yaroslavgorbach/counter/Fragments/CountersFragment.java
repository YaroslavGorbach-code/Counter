package com.yaroslavgorbach.counter.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.DividerItemDecoration;
import com.yaroslavgorbach.counter.ViewModels.CountersViewModel;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersFragment extends Fragment {
    private CountersViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CountersAdapter mAdapter;
    private String mGroup;

    private TextView mIncAllSelectedCounters_bt;
    private TextView mDecAllSelectedCounters_bt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mAdapter.counterSelection.isSelectionMod.getValue()){
                    mAdapter.counterSelection.clearAllSelections();
                }else {
                    getActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_of_counters_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CountersViewModel.class);
        mRecyclerView = view.findViewById(R.id.countersList_rv);
        mGroup = CountersFragmentArgs.fromBundle(getArguments()).getGroup();
        mDecAllSelectedCounters_bt = view.findViewById(R.id.allSelectedDec);
        mIncAllSelectedCounters_bt = view.findViewById(R.id.allSelectedInc);


        mAdapter = new CountersAdapter(new CountersAdapter.CounterItemListeners() {
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

            @Override
            public void onMoved(Counter counterFrom, Counter counterTo) {
                mViewModel.countersMoved(counterFrom, counterTo);
            }
        }, getActivity().getApplication());

        if (mGroup != null && !mGroup.equals("null")) {
            mViewModel.getCountersByGroup(mGroup).observe(getViewLifecycleOwner(), counters -> {
                mAdapter.setData(counters);
                MaterialToolbar materialToolbar = getActivity().findViewById(R.id.toolbar_mainActivity);
                materialToolbar.setTitle(mGroup);
            });
        } else {
            mViewModel.mCounters.observe(getViewLifecycleOwner(), counters -> {
                mAdapter.setData(counters);
            });
        }


        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter.itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new FastCountButton(mDecAllSelectedCounters_bt, ()-> mAdapter.counterSelection.decSelectedCounters());
        new FastCountButton(mIncAllSelectedCounters_bt, ()-> mAdapter.counterSelection.incSelectedCounters());

        mAdapter.counterSelection.isSelectionMod.observe(getViewLifecycleOwner(), isSelectionMod ->{
            if (isSelectionMod){
                mDecAllSelectedCounters_bt.setVisibility(View.VISIBLE);
                mIncAllSelectedCounters_bt.setVisibility(View.VISIBLE);
            }else {
                mDecAllSelectedCounters_bt.setVisibility(View.GONE);
                mIncAllSelectedCounters_bt.setVisibility(View.GONE);
            }
        });

    }
}

