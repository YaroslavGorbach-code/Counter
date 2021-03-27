package com.yaroslavgorbachh.counter.createEditCounter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.InputFilters;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.di.ViewModelProviderFactory;

import java.util.Objects;

import javax.inject.Inject;


public class CreateEditCounterFragment extends Fragment {
    private String mTitle;
    private long mValue;
    private long mStep;
    private String mGroup;
    private long mMaxValue;
    private long mMinValue;

    private TextInputEditText mTitle_et;
    private TextInputEditText mValue_et;
    private TextInputEditText mStep_et;
    private TextInputEditText mMaxValue_et;
    private TextInputEditText mMinValue_et;
    private AutoCompleteTextView mGroups_et;
    private Toolbar mToolbar;

    private CreateEditCounterViewModel mViewModel;
    @Inject ViewModelProviderFactory viewModelProviderFactory;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.createEditCounterComponent().create().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_edit_counter, container, false);

        /*initialize fields*/
        mTitle_et = view.findViewById(R.id.counterTitle_addCounter_detailed);
        mValue_et = view.findViewById(R.id.counterValue_addCounter_detailed);
        mStep_et = view.findViewById(R.id.counterStep_addCounter_detailed);
        mMaxValue_et = view.findViewById(R.id.counterMaxValue_addCounter_detailed);
        mMinValue_et = view.findViewById(R.id.counterMinValue_addCounter_detailed);
        mToolbar = view.findViewById(R.id.toolbar_counterCreateActivity);
        mGroups_et = view.findViewById(R.id.filled_exposed_dropdown);

        long counterId = CreateEditCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(CreateEditCounterViewModel.class);
        mViewModel.setCounterId(counterId);

        /*set navigationIcon, inflate menu, and set listeners*/
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setNavigationOnClickListener(i -> {
            Navigation.findNavController(view).popBackStack();
        });
        mToolbar.inflateMenu(R.menu.menu_counter_create_fragment);

        mViewModel.getCounter().observe(getViewLifecycleOwner(), counter -> {
            /*if counter == null that means that counter will be created*/
            if (counter == null) {
                mTitle = "";
                mValue = 0;
                mStep = 1;
                mGroup = "";
                mToolbar.setTitle(getResources().getString(R.string.createEditCounterCounterTitleText));
            /*if counter != null that means counter will
             be updated and we need to get old counter values*/
            } else {
                mTitle = counter.title;
                mValue = counter.value;
                mStep = counter.step;
                mGroup = counter.grope;
                mMaxValue = counter.maxValue;
                mMinValue = counter.minValue;
                mTitle_et.setText(counter.title);
                mValue_et.setText(String.valueOf(counter.value));
                mStep_et.setText(String.valueOf(counter.step));
                mGroups_et.setText(counter.grope);
                mToolbar.setTitle(getResources().getString(R.string.createEditCounterEditToolbarTitle));

                if (counter.maxValue != Counter.MAX_VALUE) {
                    mMaxValue_et.setText(String.valueOf(counter.maxValue));
                }
                if (counter.minValue != Counter.MIN_VALUE) {
                    mMinValue_et.setText(String.valueOf(counter.minValue));
                }
            }

            /*create new counter*/
            mToolbar.setOnMenuItemClickListener(i -> {
                updateCreateCounter();
                return true;
            });
        });

        /*each new group sets into dropdown_menu*/
        mViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(),
                    R.layout.dropdown_menu_popup_item,
                    Utility.deleteTheSameGroups(groups));
            mGroups_et.setAdapter(adapter);
        });
        return view;
    }

    private void updateCreateCounter() {
        if (InputFilters.titleFilter(mTitle_et)
                && InputFilters.valueFilter(mValue_et)
                && InputFilters.stepFilter(mStep_et)){
            mTitle = Objects.requireNonNull(mTitle_et.getText()).toString();
            mValue = Long.parseLong(String.valueOf(mValue_et.getText()));
            mStep = Long.parseLong(String.valueOf(mStep_et.getText()));
        }else {
            return;
        }

        mMaxValue = InputFilters.maxValueFilter(mMaxValue_et);
        mMinValue = InputFilters.minValue(mMinValue_et);
        mGroup = InputFilters.groupsFilter(mGroups_et);

        /*if all fields are filled create counter*/
        mViewModel.updateCreateCounter(mTitle, mValue, mMaxValue, mMinValue, mStep, mGroup);
        Navigation.findNavController(getView()).popBackStack();
        Utility.hideKeyboard(requireActivity());
    }
}