package com.yaroslavgorbachh.counter.screen.counter;

import android.view.View;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;
import com.yaroslavgorbachh.counter.util.Utility;

public class CounterView {

    private final FragmentCounterBinding mBinding;
    private final Callback mCallback;

    public CounterView(FragmentCounterBinding binding, Callback callback) {
        mBinding = binding;
        mCallback = callback;

        binding.toolbar.setOnMenuItemClickListener(i -> {
            if (i.getItemId() == R.id.delete) {
                callback.onDelete();
            }
            if (i.getItemId() == R.id.edit) {
                callback.onEdit();
            }
            if (i.getItemId() == R.id.history) {
                callback.onHistory();
            }
            if (i.getItemId() == R.id.about) {
                callback.onAbout();
            }
            if (i.getItemId() == R.id.fullScreen) {
                callback.onFullScreen();
            }
            if (i.getItemId() == R.id.reset) {
                callback.onReset();
            }
            return true;
        });
        binding.toolbar.setNavigationOnClickListener(i -> callback.onBack());
        binding.inc.setOnClickListener(callback::onInc);
        binding.dec.setOnClickListener(callback::onDec);
    }

    public void setCounter(Counter counter) {
        if (counter != null) {
            mBinding.value.setTextSize(Utility.getValueTvSize(counter.value));
            mBinding.value.setText(String.valueOf(counter.value));
            mBinding.title.setText(counter.title);
            mBinding.groupTitle.setText(counter.grope);
        } else {
            mCallback.onBack();
        }
    }

    public interface Callback {
        void onDelete();

        void onEdit();

        void onHistory();

        void onAbout();

        void onFullScreen();

        void onBack();

        void onInc(View view);

        void onDec(View view);

        void onReset();
    }
}
