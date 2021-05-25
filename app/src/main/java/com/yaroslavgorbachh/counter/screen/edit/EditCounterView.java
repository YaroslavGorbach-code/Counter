package com.yaroslavgorbachh.counter.screen.edit;

import android.widget.ArrayAdapter;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentEditCounterBinding;
import com.yaroslavgorbachh.counter.screen.edit.colorpicker.ColorPicker;
import com.yaroslavgorbachh.counter.screen.edit.colorpicker.ScrollColorPicker;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import java.util.Date;
import java.util.List;

public class EditCounterView {
    private final FragmentEditCounterBinding mBinding;
    private Counter mCounter;
    private final ColorPicker mColorPicker;

    public EditCounterView(FragmentEditCounterBinding binding, Callback callback) {
        mBinding = binding;
        mColorPicker = new ScrollColorPicker(mBinding.colorPickerParent, binding.getRoot().getResources());
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.saveCreateCounter) {
                if (ViewUtil.titleFilter(mBinding.title)
                        && ViewUtil.valueFilter(mBinding.value, mBinding.min, mBinding.max)
                        && ViewUtil.stepFilter(mBinding.step)
                        && ViewUtil.maxAndMinValueFilter(mBinding.max, mBinding.min)) {

                    mBinding.group.setText(ViewUtil.groupsFilter(mBinding.group));
                    Counter counter = new Counter(
                            mBinding.title.getText().toString(),
                            Long.parseLong(mBinding.value.getText().toString()),
                            Long.parseLong(ViewUtil.maxValueFilter(mBinding.max)),
                            Long.parseLong(ViewUtil.minValueFilter(mBinding.min)),
                            Long.parseLong(mBinding.step.getText().toString()),
                            mColorPicker.getColorId(),
                            mBinding.group.getText().toString(),
                            new Date(),
                            new Date(),
                            new Date(),
                            0L,
                            0L,
                            0L,
                            0);
                    if (mCounter != null) {
                        counter.setId(mCounter.id);
                        counter.createDateSort = mCounter.createDateSort;
                        counter.lastResetDate = mCounter.lastResetDate;
                        counter.createDate = mCounter.createDate;
                        counter.lastResetValue = mCounter.lastResetValue;
                        counter.counterMaxValue = mCounter.counterMaxValue;
                        counter.counterMinValue = mCounter.counterMinValue;
                        counter.widgetId = mCounter.widgetId;
                    }
                    callback.onSave(counter);
                }
            }
            return true;
        });

    }

    public void setCounter(Counter counter) {
        /*if counter == null that means that counter will be created*/
        if (counter == null) {
            mBinding.title.setText("");
            mBinding.value.setText("0");
            mBinding.step.setText("1");
            mBinding.group.setText("");
            mBinding.toolbar.setTitle(mBinding.getRoot().getContext().getString(R.string.createEditCounterCounterTitleText));
        } else {
            mCounter = counter;
            mBinding.title.setText(counter.title);
            mBinding.value.setText(String.valueOf(counter.value));
            mBinding.step.setText(String.valueOf(counter.step));
            mBinding.group.setText(counter.grope);
            mColorPicker.setColorId(counter.colorId);
            if (counter.maxValue != Counter.MAX_VALUE) {
                mBinding.max.setText(String.valueOf(counter.maxValue));
            }
            if (counter.minValue != Counter.MIN_VALUE) {
                mBinding.min.setText(String.valueOf(counter.minValue));
            }
            mBinding.toolbar.setTitle(mBinding.getRoot().getContext().getResources().getString(R.string.createEditCounterEditToolbarTitle));
        }
    }

    public void setGroups(List<String> groups) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                mBinding.getRoot().getContext(),
                R.layout.item_popup,
                groups);
        mBinding.group.setAdapter(adapter);
    }

    interface Callback {
        void onBack();
        void onSave(Counter counter);
    }

}
