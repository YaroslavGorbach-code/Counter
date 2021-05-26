package com.yaroslavgorbachh.counter.screen.history;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.transition.MaterialFade;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.History;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterHistoryBinding;

import java.util.List;

public class HistoryView {
    public interface Callback {
        void onBack();
        void onClear();
        void onRemove(History history);
        void onUndo(History item);
    }

    private final HistoryAdapter mAdapter;
    private final FragmentCounterHistoryBinding mBinding;

    public HistoryView(FragmentCounterHistoryBinding bind, Callback callback) {
        mBinding = bind;
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
                if (position == 0){
                    mAdapter.setSort(HistoryAdapter.Sort.DATE);
                }else {
                    mAdapter.setSort(HistoryAdapter.Sort.VALUE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mAdapter = new HistoryAdapter();
        bind.list.setAdapter(mAdapter);
        bind.list.setLayoutManager(new LinearLayoutManager(bind.getRoot().getContext()));
        SwipeDeleteDecor swipeDeleteDecor = new SwipeDeleteDecor(viewHolder -> {
            History item = mAdapter.getData().get(viewHolder.getBindingAdapterPosition());
            callback.onRemove(item);
            Snackbar.make(bind.getRoot(), R.string.history_item_removed, BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(R.string.counterResetUndo, v -> callback.onUndo(item)).show();
        }, ContextCompat.getDrawable(bind.getRoot().getContext(), R.drawable.remove_history_item));
        swipeDeleteDecor.attachToRecyclerView(bind.list);
    }

    public void setHistory(List<History> histories) {
        showNoHistoryIcon(histories.isEmpty());
        mAdapter.setData(histories);
    }


    private void showNoHistoryIcon(boolean show) {
        Transition transition = new MaterialFade();
        transition.setDuration(400);
        transition.addTarget(R.id.no_counters);
        TransitionManager.beginDelayedTransition(mBinding.getRoot(), transition);
        if (show) {
            mBinding.noHistory.setVisibility(View.VISIBLE);
        } else {
            mBinding.noHistory.setVisibility(View.GONE);
        }
    }
}
