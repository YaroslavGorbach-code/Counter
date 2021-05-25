package com.yaroslavgorbachh.counter.screen.about;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.View;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutCounter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentAboutCounterBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AboutCounterFragment extends Fragment {

    @Inject AboutCounter aboutCounter;

    public AboutCounterFragment() {
        super(R.layout.fragment_about_counter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inject component
        long id = AboutCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        AboutCounterViewModel vm = new ViewModelProvider(this).get(AboutCounterViewModel.class);
        vm.getAboutCounterComponent(id).inject(this);

        // init view
        AboutCounterView v = new AboutCounterView(FragmentAboutCounterBinding.bind(view), () -> Navigation.findNavController(view).popBackStack());
        aboutCounter.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v::setCounter);
    }
}