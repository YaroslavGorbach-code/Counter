package com.yaroslavgorbachh.counter.screen.about;

import android.graphics.Paint;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentAboutCounterBinding;

public class AboutCounterView {
    public interface Callback{
        void onBack();
    }

    private final FragmentAboutCounterBinding mBinding;

    public AboutCounterView(FragmentAboutCounterBinding binding, Callback callback){
        mBinding = binding;
        mBinding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
    }
    void setCounter(Counter counter){
        mBinding.name.setText(counter.title);
        mBinding.createData.setText(mBinding.getRoot().getContext().getString(
                R.string.created, DateAndTimeUtil.convertDateToString(counter.createDate)));
        mBinding.lastResetValue.setText(String.valueOf(counter.lastResetValue));
        mBinding.value.setText(String.valueOf(counter.value));
        mBinding.step.setText(String.valueOf(counter.step));
        if (counter.grope!=null){
            mBinding.group.setText(counter.grope);
        }else{
            mBinding.group.setText(mBinding.getRoot().getContext().getResources().getString(R.string.no));
        }
        mBinding.minValue.setText(String.valueOf(counter.counterMinValue));
        mBinding.maxValue.setText(String.valueOf(counter.counterMaxValue));
        if (counter.lastResetDate!=null)
            mBinding.lastResetDate.setText(DateAndTimeUtil.convertDateToString(counter.lastResetDate));
    }
}
