package com.yaroslavgorbachh.counter.screen.history;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.History;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterHistoryBinding;
import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

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
                mAdapter.setData(Observable.fromIterable(mAdapter.getData())
                        .sorted((o1, o2) -> {
                            if (position == 1) {
                                return Long.compare(o2.value, o1.value);
                            } else {
                                if (DateAndTimeUtil.convertStringToDate(o1.data)
                                        .before(DateAndTimeUtil.convertStringToDate(o2.data))) {
                                    return 1;
                                } else if (DateAndTimeUtil.convertStringToDate(o1.data)
                                        .after(DateAndTimeUtil.convertStringToDate(o2.data))) {
                                    return -1;
                                } else {
                                    return 0;
                                }
                            }
                        }).toList().blockingGet());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        mAdapter = new HistoryAdapter();
        bind.list.setAdapter(mAdapter);
        bind.list.setLayoutManager(new LinearLayoutManager(bind.getRoot().getContext()));

    }

    public void setHistory(List<History> histories) {
        mAdapter.setData(histories);
    }


    public interface Callback {
        void onBack();

        void onClear();
    }

}
