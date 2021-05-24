package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class FullscreenView {
    public interface Callback {
        void onBack();
        void onSwipeTop();
        void onSwipeBottom();
    }
    private final VolumeButtonBroadcastReceiver mMessageReceiver;

    private final FragmentFullscreenBinding mBinding;
    @SuppressLint("ClickableViewAccessibility")
    public FullscreenView(FragmentFullscreenBinding binding, Callback callback) {
        mBinding = binding;

        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() { callback.onSwipeBottom(); }

            @Override
            public void incCounters() { callback.onSwipeTop(); }

            @Override
            public void lowerVolume() { }

            @Override
            public void raiseVolume() { }
        });
        LocalBroadcastManager.getInstance(mBinding.getRoot().getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

        showHelp();
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        mBinding.viewGroup.setOnTouchListener(new SwipeListener(binding.getRoot().getContext()){
            @Override
            public void onSwipeTop() { callback.onSwipeTop(); }
            @Override
            public void onSwipeBottom() { callback.onSwipeBottom(); }
        });
    }

    public void setCounter(Counter counter) {
        mBinding.value.setTextSize(ViewUtil.getCounterTvSize(counter.value));
        mBinding.value.setText(String.valueOf(counter.value));
    }

    public void unregisterReceiver(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    private void showHelp(){
        new Handler(Looper.getMainLooper()).postDelayed(() ->
                mBinding.help.setVisibility(View.GONE),1500);
    }

}
