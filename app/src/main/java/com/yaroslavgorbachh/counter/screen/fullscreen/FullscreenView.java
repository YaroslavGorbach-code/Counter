package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenBinding;
import com.yaroslavgorbachh.counter.util.Utility;
import com.yaroslavgorbachh.counter.util.ViewUtil;

public class FullscreenView {
    public interface Callback {
        void onBack();
        void onSwipeTop();
        void onSwipeBottom();
    }

    private final FragmentFullscreenBinding mBinding;
    @SuppressLint("ClickableViewAccessibility")
    public FullscreenView(FragmentFullscreenBinding binding, Callback callback) {
        mBinding = binding;
        showHelp();
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        mBinding.viewGroup.setOnTouchListener(new SwipeListener(binding.getRoot().getContext()){
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
        mBinding.value.setTextSize(ViewUtil.getCounterTvSize(counter.value));
        mBinding.value.setText(String.valueOf(counter.value));
    }

    private void showHelp(){
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                mBinding.help.setVisibility(View.GONE),2000);
    }
}
