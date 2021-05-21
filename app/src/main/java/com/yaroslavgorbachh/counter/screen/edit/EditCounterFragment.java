package com.yaroslavgorbachh.counter.screen.edit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.yaroslavgorbachh.counter.component.edit.EditComponent;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentEditCounterBinding;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.util.ViewUtil;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;


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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        long id = EditCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        EditCounterViewModel vm = new ViewModelProvider(this).get(EditCounterViewModel.class);
        EditComponent editComponent = vm.getEditCounter(repo, id);

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
                editComponent.updateCounter(counter);
                Navigation.findNavController(view).popBackStack();
                ViewUtil.hideKeyboard(requireActivity());
            }
        });

        editComponent.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v::setCounter);
        editComponent.getGroups().observe(getViewLifecycleOwner(), v::setGroups);
    }

}