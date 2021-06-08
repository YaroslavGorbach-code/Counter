package com.yaroslavgorbachh.counter.screen.counter;

import android.content.Context;
import android.content.IntentFilter;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;
import com.yaroslavgorbachh.counter.feature.FastCountButton;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CounterView {
    public interface Callback {
        void onDelete();
        void onEdit();
        void onHistory();
        void onAbout();
        void onFullScreen();
        void onBack();
        void onInc();
        void onDec();
        void onReset();
    }
    private final FragmentCounterBinding mBinding;
    private final Callback mCallback;
    private final VolumeButtonBroadcastReceiver mMessageReceiver;

    public CounterView(FragmentCounterBinding binding, FragmentActivity activity, int fastCountInterval, Callback callback) {
        mBinding = binding;
        mCallback = callback;
        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() { callback.onDec(); }
            @Override
            public void incCounters() { callback.onInc(); }
            @Override
            public void lowerVolume() {}
            @Override
            public void raiseVolume() {}
        });
        LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

        binding.toolbar.setOnMenuItemClickListener(i -> {
            if (i.getItemId() == R.id.delete) {
                new MaterialAlertDialogBuilder(mBinding.getRoot().getContext())
                        .setTitle(mBinding.getRoot().getContext().getString(R.string.deleteCounterDeleteDialog))
                        .setMessage(R.string.deleteCounterDialogText)
                        .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which) -> {
                            callback.onDelete();
                        })
                        .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
                        .show();
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
        new FastCountButton(binding.inc, mCallback::onInc, fastCountInterval);
        new FastCountButton(binding.dec, callback::onDec, fastCountInterval);
    }

    public void setCounter(Counter counter) {
        if (counter != null) {
            mBinding.inc.setIconTintResource(counter.colorId);
            mBinding.dec.setIconTintResource(counter.colorId);
            mBinding.inc.setRippleColorResource(counter.colorId);
            mBinding.dec.setRippleColorResource(counter.colorId);

            mBinding.value.setTextSize(ViewUtil.getCounterTvSize(counter.value));
            mBinding.value.setText(String.valueOf(counter.value));
            mBinding.toolbar.setTitle(counter.title);
            if (counter.grope!=null && !counter.grope.equals("")){
                mBinding.groupTitle.setVisibility(View.VISIBLE);
                mBinding.groupTitle.setText(counter.grope);
            }
        } else {
            mCallback.onBack();
        }
    }

    public void unregisterReceiver(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

}
