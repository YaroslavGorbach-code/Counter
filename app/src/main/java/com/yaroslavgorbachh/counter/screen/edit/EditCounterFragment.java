package com.yaroslavgorbachh.counter.screen.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.yaroslavgorbachh.counter.component.edit.Edit;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentEditCounterBinding;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.di.EditCounterComponent;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class EditCounterFragment extends Fragment {
    @Inject Edit edit;

    public EditCounterFragment() {
        super(R.layout.fragment_edit_counter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inject component
        long id = EditCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        EditCounterViewModel vm = new ViewModelProvider(this).get(EditCounterViewModel.class);
        vm.getEditCounterComponent(id).inject(this);

        // init view
        EditCounterView v = new EditCounterView(
                FragmentEditCounterBinding.bind(view),
                new EditCounterView.Callback() {
            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onSave(Counter counter) {
                edit.updateCounter(counter);
                Navigation.findNavController(view).popBackStack();
                ViewUtil.hideKeyboard(requireActivity());
            }
        });

        edit.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v::setCounter);
        edit.getGroups().observe(getViewLifecycleOwner(), v::setGroups);
    }

}