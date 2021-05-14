package com.yaroslavgorbachh.counter.counter;
import android.view.View;
import com.google.android.material.appbar.MaterialToolbar;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;

public class CounterView {

    public interface Callback{
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

    private final FragmentCounterBinding mBinding;
    private final Callback mCallback;

    public CounterView(FragmentCounterBinding binding, Callback callback){
        mBinding = binding;
        mCallback = callback;

        ((MaterialToolbar)binding.toolbar).setOnMenuItemClickListener(i -> {
            switch (i.getItemId()) {
                case R.id.counterDelete:
                  callback.onDelete();
                    break;
                case R.id.counterEdit:
                   callback.onEdit();
                    break;
                case R.id.counterHistory:
                  callback.onHistory();
                    break;
                case R.id.aboutCounter:
                    callback.onAbout();
                    break;
                case R.id.fullScreen:
                   callback.onFullScreen();
                    break;
            }
            return true;
        });
        ((MaterialToolbar)binding.toolbar).setNavigationOnClickListener(i -> callback.onBack());
        binding.inc.setOnClickListener(callback::onInc);
        binding.dec.setOnClickListener(callback::onDec);
        binding.reset.setOnClickListener(v -> callback.onReset());
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
}
