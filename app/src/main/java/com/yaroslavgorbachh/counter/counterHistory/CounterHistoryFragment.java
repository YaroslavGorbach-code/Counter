package com.yaroslavgorbachh.counter.counterHistory;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.Animations;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.counterHistory.recyclerView.CounterHistoryRv;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.di.ViewModelProviderFactory;

import java.util.Collections;

import javax.inject.Inject;

public class CounterHistoryFragment extends Fragment {
    private CounterHistoryRv mHistoryList;
    private Spinner mSpinner;
    private long mCounterId;
    private ConstraintLayout mIconAndTextThereNoHistory;
    private LinearLayout mSwipeHelper;

    private CounterHistoryViewModel mViewModel;
    @Inject ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.counterHistoryComponent().create().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter_history, container, false);

        /*initialize fields*/
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        mSpinner = view.findViewById(R.id.spinner);
        mIconAndTextThereNoHistory = view.findViewById(R.id.no_history);
        mSwipeHelper = view.findViewById(R.id.swipe_helper);

        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(CounterHistoryViewModel.class);
        mCounterId = CounterHistoryFragmentArgs.fromBundle(requireArguments()).getCounterId();

        /*initialize navigation listener*/
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(i -> Navigation.findNavController(view).popBackStack());

        toolbar.setOnMenuItemClickListener(item -> {
            new MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.clear_history_title))
                    .setMessage(getString(R.string.clear_history_message))
                    .setPositiveButton(getString(R.string.clear_history_pos_button), (dialog, which) -> mViewModel.clean(mCounterId))
                    .setNegativeButton(getString(R.string.clear_history_neg_button), null)
                    .create()
                    .show();
            return true;
        });

        setAdapterForSpinner();
        mHistoryList = new CounterHistoryRv(view.findViewById(R.id.rv), counterHistory -> {
            CounterHistory copy = new CounterHistory(counterHistory.value, counterHistory.data, counterHistory.counterId);
            copy.setId(counterHistory.id);
            mViewModel.deleteHistoryItem(counterHistory);
            Snackbar.make(view, getString(R.string.history_item_removed), BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(getString(R.string.counterResetUndo), v -> mViewModel.addHistoryItem(copy)).show();
        });

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

                    if (counterHistories.size() <= 0) {
                        mIconAndTextThereNoHistory.setVisibility(View.VISIBLE);
                        mSwipeHelper.setVisibility(View.GONE);
                    } else {
                        mIconAndTextThereNoHistory.setVisibility(View.GONE);
                        Animations.hideSwipeHelperWithDelay(mSwipeHelper);
                    }

                    if (position == 0) {
                        Collections.sort(counterHistories, (o1, o2) -> Long.compare(o1.id, o2.id));
                    } else {
                        Collections.sort(counterHistories, (o1, o2) -> Long.compare(o1.value, o2.value));
                    }
                    Collections.reverse(counterHistories);
                    mHistoryList.setHistory(counterHistories);

                });
    }

    private void setAdapterForSpinner() {
        /*creating adapter for spinner*/
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(requireContext(), R.array.history_sort_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
    }
}
