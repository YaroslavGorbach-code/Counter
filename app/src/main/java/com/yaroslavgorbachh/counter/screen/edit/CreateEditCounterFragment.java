package com.yaroslavgorbachh.counter.screen.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.component.edit.EditCounter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentEditCounterBinding;
import com.yaroslavgorbachh.counter.feature.InputFilters;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.utill.Utility;

import java.util.Objects;

import javax.inject.Inject;


public class CreateEditCounterFragment extends Fragment {

    public CreateEditCounterFragment() {
        super(R.layout.fragment_edit_counter);
    }

    @Inject Repo repo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        long id = CreateEditCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        CreateEditCounterViewModel vm = new ViewModelProvider(this).get(CreateEditCounterViewModel.class);
        EditCounter editCounter = vm.getEditCounter(repo, id);

        // init view
        EditCounterView v = new EditCounterView(FragmentEditCounterBinding.bind(view), new EditCounterView.Callback() {
            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onSave(String title, long value, int step, long max, long min, String group) {
                editCounter.editCounter(title, value, max, min, step, group);
                Navigation.findNavController(view).popBackStack();
                Utility.hideKeyboard(requireActivity());
            }

        });
        editCounter.getCounter().observe(getViewLifecycleOwner(), v::setCounter);
        editCounter.getGroups().observe(getViewLifecycleOwner(), v::setGroups);
    }

}