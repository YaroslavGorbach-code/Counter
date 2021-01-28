package com.yaroslavgorbachh.counter.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import com.yaroslavgorbachh.counter.RecyclerViews.CounterList_rv;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Activityes.CounterActivity;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.ViewModels.CountersViewModel;

import java.util.Date;

public class CountersFragment extends Fragment {
    private CounterList_rv mCountersList;
    private CountersViewModel mViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_of_counters_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CountersViewModel.class);

        /*initialize RecyclerView and it listener*/
        mCountersList = new CounterList_rv(view.findViewById(R.id.countersList_rv), new CounterList_rv.ItemClickListener() {
            /*counter +*/
            @Override
            public void onPlusClick(Counter counter) {
                mViewModel.incCounter(counter);
            }

            /*counter -*/
            @Override
            public void onMinusClick(Counter counter) {
               mViewModel.decCounter(counter);
            }

            /*open counterActivity*/
            @Override
            public void onOpen(Counter counter) {
                startActivity(new Intent(view.getContext(), CounterActivity.class).
                        putExtra(CounterActivity.EXTRA_COUNTER_ID, counter.id));
            }
        }, new CounterList_rv.MoveListener() {
            //item moved
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
                    .observe(getViewLifecycleOwner(), counters -> mCountersList.setCounters(counters));

        }else{

            mViewModel.getAllCounters()
                    .observe(getViewLifecycleOwner(), counters -> mCountersList.setCounters(counters));
        }

        return view;
    }
}
