package com.yaroslavgorbachh.counter.screen.widget;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.ActivityWidgetConfigurationBinding;

import java.util.List;

public class WidgetConfigView {
    interface Callback {
        void onBack();
        void onWidget(Counter counter);
    }
    private final WidgetsAdapter mAdapter;
    public WidgetConfigView(ActivityWidgetConfigurationBinding binding, Callback callback) {
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        mAdapter = new WidgetsAdapter(callback::onWidget);
        binding.list.setLayoutManager(new LinearLayoutManager(binding.getRoot().getContext()));
        binding.list.setAdapter(mAdapter);
    }

    public void setCounters(List<Counter> counters) {
        mAdapter.setData(counters);
    }

}
