package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.util.ViewUtil;

public class FullscreenView {
    public interface Callback {
        void onBack();
        void onSwipeTop();
        void onSwipeBottom();
    }

    private final FragmentFullscreenBinding mBinding;
    @SuppressLint("ClickableViewAccessibility")
    public FullscreenView(FragmentFullscreenBinding binding, Accessibility accessibility, Callback callback) {
        mBinding = binding;
        showHelp();
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        mBinding.viewGroup.setOnTouchListener(new SwipeListener(binding.getRoot().getContext()){
            @Override
            public void onSwipeTop() {
                callback.onSwipeTop();
                accessibility.playIncFeedback(null);
            }
            @Override
            public void onSwipeBottom() {
                callback.onSwipeBottom();
                accessibility.playDecFeedback(null);
            }
        });
    }

    public void setCounter(Counter counter) {
        mBinding.value.setTextSize(ViewUtil.getCounterTvSize(counter.value));
        mBinding.value.setText(String.valueOf(counter.value));
    }

    private void showHelp(){
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                mBinding.help.setVisibility(View.GONE),1500);
    }
}
