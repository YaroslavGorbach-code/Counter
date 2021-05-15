package com.yaroslavgorbachh.counter.aboutCounter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textview.MaterialTextView;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;

import javax.inject.Inject;

public class AboutCounterFragment extends Fragment {
    private MaterialTextView mCounterName;
    private MaterialTextView mCreateData;
    private MaterialTextView mLastResetedValue;
    private MaterialTextView mCounterValue;
    private MaterialTextView mCounterStep;
    private MaterialTextView mCounterGroup;
    private MaterialTextView mCounterMinValue;
    private MaterialTextView mCounterMaxValue;
    private MaterialTextView mLastResetData;
    private AboutCounterViewModel viewModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication app = (MyApplication) (requireActivity().getApplication());
        app.appComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_counter, container, false);
        long counterId = AboutCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        viewModel = new ViewModelProvider(this).get(AboutCounterViewModel.class);
        viewModel.setCounterId(counterId);

        mCounterName = view.findViewById(R.id.name);
        mCreateData = view.findViewById(R.id.create_data);
        mLastResetedValue = view.findViewById(R.id.last_reset_value);
        mCounterValue = view.findViewById(R.id.value);
        mCounterStep = view.findViewById(R.id.step);
        mCounterGroup = view.findViewById(R.id.group);
        mCounterMinValue = view.findViewById(R.id.min_value);
        mCounterMaxValue = view.findViewById(R.id.max_value);
        mLastResetData = view.findViewById(R.id.last_reset);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());

        viewModel.counter.observe(getViewLifecycleOwner(), counter -> {
            mCounterName.setText(counter.title);
            mCreateData.setText(getString(R.string.created, Utility.convertDateToString(counter.createDate)));
            mLastResetedValue.setText(String.valueOf(counter.lastResetValue));
            mCounterValue.setText(String.valueOf(counter.value));
            mCounterStep.setText(String.valueOf(counter.step));
            if (counter.grope!=null)
            mCounterGroup.setText(counter.grope);
            mCounterMinValue.setText(String.valueOf(counter.counterMinValue));
            mCounterMaxValue.setText(String.valueOf(counter.counterMaxValue));
            if (counter.lastResetDate!=null)
            mLastResetData.setText(Utility.convertDateToString(counter.lastResetDate));
        });

        return view;
    }
}