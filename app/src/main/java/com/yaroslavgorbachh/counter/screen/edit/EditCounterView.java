package com.yaroslavgorbachh.counter.screen.edit;


import android.view.MenuItem;
import android.widget.ArrayAdapter;

import androidx.appcompat.widget.Toolbar;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentEditCounterBinding;
import com.yaroslavgorbachh.counter.feature.InputFilters;
import com.yaroslavgorbachh.counter.utill.Utility;

import java.util.List;
import java.util.Objects;

public class EditCounterView {

    interface Callback{
        void onBack();
        void onSave(String title, long value, int step, long max, long min, String group);
    }
    private FragmentEditCounterBinding mBinding;
    public EditCounterView(FragmentEditCounterBinding binding, Callback callback){
        mBinding = binding;
        binding.toolbar.setNavigationOnClickListener(v -> callback.onBack());
        binding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.saveCreateCounter){
                if (!InputFilters.titleFilter(mBinding.title)
                        && !InputFilters.valueFilter(mBinding.value)
                        && !InputFilters.stepFilter(mBinding.step)) {
                    return true;
                }

                mBinding.max.setText(InputFilters.maxValueFilter(mBinding.max));
                mBinding.min.setText(InputFilters.minValueFilter(mBinding.min));
                mBinding.dropdown.setText(InputFilters.groupsFilter(mBinding.dropdown));

                callback.onSave(
                        mBinding.title.getText().toString(),
                        Long.parseLong(mBinding.value.getText().toString()),
                        Integer.parseInt(mBinding.step.getText().toString()),
                        Long.parseLong(mBinding.max.getText().toString()),
                        Long.parseLong(mBinding.min.getText().toString()),
                        mBinding.dropdown.getText().toString()
                );
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
            mBinding.dropdown.setText("");
            mBinding.toolbar.setTitle(mBinding.getRoot().getContext().getString(R.string.createEditCounterCounterTitleText));
            /*if counter != null that means counter will
             be updated and we need to get old counter values*/
        } else {
            mBinding.title.setText(counter.title);
            mBinding.value.setText(String.valueOf(counter.value));
            mBinding.step.setText(String.valueOf(counter.step));
            mBinding.dropdown.setText(counter.grope);
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
        mBinding.dropdown.setAdapter(adapter);
    }

}
