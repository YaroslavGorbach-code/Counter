package com.yaroslavgorbachh.counter.aboutCounter;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentAboutCounterBinding;

import javax.inject.Inject;

public class AboutCounterFragment extends Fragment {

    public AboutCounterFragment() {
        super(R.layout.fragment_about_counter);
    }

    @Inject Repo repo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication app = (MyApplication) (requireActivity().getApplication());
        app.appComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        long id = AboutCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        AboutCounterViewModel vm = new ViewModelProvider(this).get(AboutCounterViewModel.class);
        AboutCounter aboutCounter = vm.getAboutCounter(repo, id);

        // init view
        AboutCounterView v = new AboutCounterView(FragmentAboutCounterBinding.bind(view), () -> Navigation.findNavController(view).popBackStack());
        aboutCounter.getCounter().observe(getViewLifecycleOwner(), v::setCounter);
    }
}