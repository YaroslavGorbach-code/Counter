package com.yaroslavgorbachh.counter.screen.history;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterHistoryBinding;

import java.util.List;

public class HistoryView {
    private final HistoryAdapter mAdapter;

    public HistoryView(FragmentCounterHistoryBinding bind, Callback callback) {
        bind.toolbar.setNavigationOnClickListener(i -> callback.onBack());
        bind.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.deleteHistory) {
                new MaterialAlertDialogBuilder(bind.getRoot().getContext())
                        .setTitle(bind.getRoot().getContext().getString(R.string.clear_history_title))
                        .setMessage(bind.getRoot().getContext().getString(R.string.clear_history_message))
                        .setPositiveButton(bind.getRoot().getContext().getString(R.string.clear_history_pos_button), (dialog, which) -> callback.onClear())
                        .setNegativeButton(bind.getRoot().getContext().getString(R.string.clear_history_neg_button), null)
                        .create()
                        .show();
            }
            return true;
        });
        ArrayAdapter<?> spinnerAdapter =
                ArrayAdapter.createFromResource(bind.getRoot().getContext(), R.array.history_sort_items, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bind.spinner.setAdapter(spinnerAdapter);

        bind.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO: 5/15/2021 sort history
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mAdapter = new HistoryAdapter();
        bind.list.setAdapter(mAdapter);
        bind.list.setLayoutManager(new LinearLayoutManager(bind.getRoot().getContext()));

    }

    public void setHistory(List<CounterHistory> histories) {
        mAdapter.setData(histories);
    }


    public interface Callback {
        void onBack();

        void onClear();
    }

}
