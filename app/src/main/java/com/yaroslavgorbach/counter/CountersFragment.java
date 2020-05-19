package com.yaroslavgorbach.counter;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class CountersFragment extends Fragment {
    private CounterList_rv mCountersList;
    private CounterViewModel mCounterViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.list_of_counters_fragment, container, false);
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        /*initialize RecyclerView and it listener*/
        mCountersList = new CounterList_rv(view.findViewById(R.id.countersList_rv), new CounterList_rv.Listener() {

            /*counter +*/
            @Override
            public void onPlusClick(Counter counter) {
                long maxValue;
                long incOn;
                long value = counter.value;
                maxValue = counter.maxValue;
                incOn = counter.step;
                value +=incOn;

                if (value > maxValue){

                    mCounterViewModel.setValue(counter, counter.maxValue);
                    Toast.makeText(view.getContext(), "This is maximum", Toast.LENGTH_SHORT).show();

                }else {

                    mCounterViewModel.setValue(counter, value);

                }

            }

            /*counter -*/
            @Override
            public void onMinusClick(Counter counter) {
                long minValue;
                long decOn;
                minValue = counter.minValue;
                long value = counter.value;
                decOn = counter.step;
                value -= decOn;

                if (value < minValue){

                    mCounterViewModel.setValue(counter, counter.minValue);

                    Toast.makeText(view.getContext(), "This is minimum", Toast.LENGTH_SHORT).show();


                }else {

                    mCounterViewModel.setValue(counter, value);

                }

            }

            /*open counterActivity*/
            @Override
            public void onOpen(Counter counter) {

                startActivity(new Intent(view.getContext(), CounterActivity.class).
                        putExtra(CounterActivity.EXTRA_COUNTER_ID, counter.id));
            }
        });

        /*if there are no arguments, then set all the
         counters in the list; if there are then the detectors
         which belong to the group indicated in the arguments
         updates the list of counters if something changes
         in the counter_table*/
        if(getArguments()!=null){

            mCounterViewModel.getCountersByGroup(getArguments().getString("group_title"))
                    .observe(getViewLifecycleOwner(), counters -> mCountersList.setCounters(counters));

        }else{

            mCounterViewModel.getAllCounters()
                    .observe(getViewLifecycleOwner(), counters -> mCountersList.setCounters(counters));
        }

        return view;
    }
}
