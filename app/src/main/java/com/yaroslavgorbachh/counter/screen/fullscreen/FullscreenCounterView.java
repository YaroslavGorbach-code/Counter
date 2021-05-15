package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenCounterBinding;
import com.yaroslavgorbachh.counter.utill.Utility;

public class FullscreenCounterView {
    public interface Callback {
        void onBack();
        void onSwipeTop();
        void onSwipeBottom();
    }

    private final FragmentFullscreenCounterBinding mBinding;
    @SuppressLint("ClickableViewAccessibility")
    public FullscreenCounterView(FragmentFullscreenCounterBinding binding, Callback callback) {
        mBinding = binding;
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        mBinding.viewGroup.setOnTouchListener(new CounterSwipeListener(binding.getRoot().getContext()){
            @Override
            public void onSwipeTop() {
                callback.onSwipeTop();
            }
            @Override
            public void onSwipeBottom() {
                callback.onSwipeBottom();
            }
        });
    }

    public void setCounter(Counter counter) {
        mBinding.value.setTextSize(Utility.getValueTvSize(counter.value));
        mBinding.value.setText(String.valueOf(counter.value));
    }

}
