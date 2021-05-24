package com.yaroslavgorbachh.counter.screen.counter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.counter.CounterComponent;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CounterFragment extends Fragment {
    @Inject Repo repo;
    private CounterView mV;
    public CounterFragment() {
        super(R.layout.fragment_counter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        App application = (App) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init component
        long id = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        CounterViewModel vm = new ViewModelProvider(this).get(CounterViewModel.class);
        CounterComponent counter = vm.getCounterComponent(repo, id);

        // init view
        mV = new CounterView(FragmentCounterBinding.bind(requireView()), requireActivity(), new CounterView.Callback() {
            @Override
            public void onDelete() {
                counter.delete();
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onEdit() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToCreateEditCounterFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onHistory() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToCounterHistoryFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onAbout() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToAboutCounterFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onFullScreen() {
                NavDirections action = CounterFragmentDirections.
                        actionCounterFragmentToFullscreenCounterFragment()
                        .setCounterId(id);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onInc() {
                counter.incCounter();
            }

            @Override
            public void onDec() {
                counter.decCounter();
            }

            @Override
            public void onReset() {
                counter.resetCounter(copy -> {
                    Snackbar.make(requireView(), getResources().getString(R.string.counterReset), Snackbar.LENGTH_LONG)
                            .setAction(getResources().getString(R.string.counterResetUndo), v1 -> counter.insert(copy)).show();
                });
            }
        });

        counter.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mV::setCounter);


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mV.unregisterReceiver(requireContext());
    }
}
