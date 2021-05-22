package com.yaroslavgorbachh.counter.screen.counter;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.feature.FastCountButton;
import com.yaroslavgorbachh.counter.util.ViewUtil;

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

    public CounterView(FragmentCounterBinding binding, Accessibility accessibility, Callback callback) {
        mBinding = binding;
        mCallback = callback;

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

        new FastCountButton(binding.inc, () -> {
            accessibility.playIncFeedback(mBinding.value.getText().toString());
            mCallback.onInc();
        }, null);

        new FastCountButton(binding.dec, () -> {
            accessibility.playDecFeedback(mBinding.value.getText().toString());
            callback.onDec();
        }, null);

    }

    public void setCounter(Counter counter) {
        if (counter != null) {
            mBinding.value.setTextSize(ViewUtil.getCounterTvSize(counter.value));
            mBinding.value.setText(String.valueOf(counter.value));
            mBinding.title.setText(counter.title);
            mBinding.groupTitle.setText(counter.grope);
        } else {
            mCallback.onBack();
        }
    }

}
