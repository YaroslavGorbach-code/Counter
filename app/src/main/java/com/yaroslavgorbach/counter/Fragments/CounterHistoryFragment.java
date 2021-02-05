package com.yaroslavgorbach.counter.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CounterHistoryList_rv;
import com.yaroslavgorbach.counter.ViewModels.CounterHistoryViewModel;

public class CounterHistoryFragment extends Fragment {

    private CounterHistoryList_rv mHistoryList;
    private Spinner mSpinner;
    private Toolbar mToolbar;
    private CounterHistoryViewModel mViewModel;
    private long mCounterId;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter_history, container, false);
        mCounterId = CounterHistoryFragmentArgs.fromBundle(getArguments()).getCounterId();

        /*initialize fields*/
        mViewModel = new ViewModelProvider(this).get(CounterHistoryViewModel.class);
        mToolbar = view.findViewById(R.id.toolbar_history);
        mSpinner = view.findViewById(R.id.spinner);

        /*initialize navigation listener*/
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i-> Navigation.findNavController(view).popBackStack());

        mToolbar.setOnMenuItemClickListener(item -> {
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
        String[] choose = getResources().getStringArray(R.array.history_sort_items);
        if(choose[position].equals("Sort by time") || choose[position].equals("Сортировка по дате")){
            /*update list of history sort by time*/
            mViewModel.getCounterHistoryList(mCounterId)
                    .observe(CounterHistoryFragment.this, counterHistories -> {
                        mHistoryList.setHistory(counterHistories);
                    });
        }else {
            /*update list of history sort by value*/
            mViewModel.getCounterHistoryListSortByValue(mCounterId)
                    .observe(CounterHistoryFragment.this, counterHistories -> {
                        mHistoryList.setHistory(counterHistories);
                    });
        }
    }

    private void setAdapterForSpinner() {
        /*set toolTipText*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSpinner.setTooltipText("Chose sort");
        }

        /*creating adapter for spinner*/
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(requireContext(), R.array.history_sort_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }
}