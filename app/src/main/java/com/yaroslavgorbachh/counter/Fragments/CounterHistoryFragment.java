package com.yaroslavgorbachh.counter.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yaroslavgorbachh.counter.Database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.RecyclerViews.Adapters.CounterHistoryList_rv;
import com.yaroslavgorbachh.counter.ViewModels.CounterHistoryViewModel;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CounterHistoryFragment extends Fragment {
    private CounterHistoryList_rv mHistoryList;
    private Spinner mSpinner;
    private CounterHistoryViewModel mViewModel;
    private long mCounterId;
    private ConstraintLayout mIconAndTextThereNoHistory;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter_history, container, false);
        mCounterId = CounterHistoryFragmentArgs.fromBundle(getArguments()).getCounterId();

        /*initialize fields*/
        mViewModel = new ViewModelProvider(this).get(CounterHistoryViewModel.class);
        Toolbar toolbar = view.findViewById(R.id.toolbar_history);
        mSpinner = view.findViewById(R.id.spinner);

        mIconAndTextThereNoHistory = view.findViewById(R.id.iconAndTextThereAreNoHistory);

        /*initialize navigation listener*/
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(i-> Navigation.findNavController(view).popBackStack());

        toolbar.setOnMenuItemClickListener(item -> {
            mViewModel.clean(mCounterId);
            return true;
        });

        setAdapterForSpinner();
        mHistoryList = new CounterHistoryList_rv(view.findViewById(R.id.counterHistory_rv));

        /*setting listener for selected item in spinner*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortList(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return view;
    }

    private void sortList(int position) {
        mViewModel.getCounterHistoryList(mCounterId)
                    .observe(CounterHistoryFragment.this, counterHistories -> {
                        if (position == 0) {
                            Collections.sort(counterHistories, (o1, o2) -> Long.compare(o1.id, o2.id));
                        }else {
                            Collections.sort(counterHistories, (o1, o2) -> Long.compare(o1.value, o2.value));
                        }
                        Collections.reverse(counterHistories);
                        mHistoryList.setHistory(counterHistories);
                        if (counterHistories.size() <= 0){
                            mIconAndTextThereNoHistory.setVisibility(View.VISIBLE);
                        }else {
                            mIconAndTextThereNoHistory.setVisibility(View.GONE);
                        }

                    });
    }

    private void setAdapterForSpinner() {
        /*set toolTipText*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSpinner.setTooltipText("Choose sort");
        }

        /*creating adapter for spinner*/
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(requireContext(), R.array.history_sort_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }
}
