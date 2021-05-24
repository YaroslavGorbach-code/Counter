package com.yaroslavgorbachh.counter.screen.counter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.counter.CounterComponent;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CounterFragment extends Fragment {

    private VolumeButtonBroadcastReceiver mMessageReceiver;
    @Inject Repo repo;

    public CounterFragment(){
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
        long counterId = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();

        // init component
        long id = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        CounterViewModel vm = new ViewModelProvider(this).get(CounterViewModel.class);
        CounterComponent counter = vm.getCounterComponent(repo, id);

        // init view
        CounterView v = new CounterView(FragmentCounterBinding.bind(requireView()), new CounterView.Callback() {
            @Override
            public void onDelete() {
                counter.delete();
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onEdit() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToCreateEditCounterFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onHistory() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToCounterHistoryFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onAbout() {
                NavDirections action = CounterFragmentDirections
                        .actionCounterFragmentToAboutCounterFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onFullScreen() {
                NavDirections action = CounterFragmentDirections.
                        actionCounterFragmentToFullscreenCounterFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onBack() {
                Navigation.findNavController(view).popBackStack();
            }

            @Override
            public void onInc() { counter.incCounter(); }

            @Override
            public void onDec() { counter.decCounter(); }

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
                .subscribe(v::setCounter);

        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                counter.decCounter();
            }

            @Override
            public void incCounters() {
                counter.incCounter();
            }

            @Override
            public void lowerVolume() {}

            @Override
            public void raiseVolume() {}
        });

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }
}
