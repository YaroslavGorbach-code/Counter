package com.yaroslavgorbachh.counter.screen.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.yaroslavgorbachh.counter.component.edit.EditCounter;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentEditCounterBinding;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.util.Utility;

import javax.inject.Inject;


public class EditCounterFragment extends Fragment {

    public EditCounterFragment() {
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
        long id = EditCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        EditCounterViewModel vm = new ViewModelProvider(this).get(EditCounterViewModel.class);
        EditCounter editCounter = vm.getEditCounter(repo, id);

        // init view
        EditCounterView v = new EditCounterView(FragmentEditCounterBinding.bind(view), new EditCounterView.Callback() {
            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onSave(Counter counter) {
                editCounter.updateCounter(counter);
                Navigation.findNavController(view).popBackStack();
                Utility.hideKeyboard(requireActivity());
            }
        });

        editCounter.getCounter().observe(getViewLifecycleOwner(), v::setCounter);
        editCounter.getGroups().observe(getViewLifecycleOwner(), v::setGroups);
    }

}