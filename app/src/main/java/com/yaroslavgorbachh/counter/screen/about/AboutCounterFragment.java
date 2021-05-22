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
import com.yaroslavgorbachh.counter.component.aboutcounter.AboutComponent;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentAboutCounterBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AboutCounterFragment extends Fragment {

    public AboutCounterFragment() {
        super(R.layout.fragment_about_counter);
    }

    @Inject Repo repo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        App app = (App) (requireActivity().getApplication());
        app.appComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        long id = AboutCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        AboutCounterViewModel vm = new ViewModelProvider(this).get(AboutCounterViewModel.class);
        AboutComponent aboutComponent = vm.getAboutCounter(repo, id);

        // init view
        AboutCounterView v = new AboutCounterView(FragmentAboutCounterBinding.bind(view), () -> Navigation.findNavController(view).popBackStack());
        aboutComponent.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v::setCounter);
    }
}