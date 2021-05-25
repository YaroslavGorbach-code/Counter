package com.yaroslavgorbachh.counter.screen.counters;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountersFragment extends Fragment implements CounterCreateDialog.Host {

    private CountersView mV;
    private final CompositeDisposable mDisposables = new CompositeDisposable();
    @Inject Counters mCounters;

    public CountersFragment() {
        super(R.layout.fragment_counters);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inject component
        CountersViewModel vm = new ViewModelProvider(this).get(CountersViewModel.class);
        vm.countersComponent.inject(this);

        // init view
        mV = new CountersView(FragmentCountersBinding.bind(view), requireActivity(), this, new CountersView.Callback() {
            @Override
            public void onSettings() {
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }

            @Override
            public void onInc(Counter counter) {
                mCounters.inc(counter);
            }

            @Override
            public void onDec(Counter counter) {
                mCounters.dec(counter);
            }

            @Override
            public void onOpen(Counter counter) {
                NavDirections action = CountersFragmentDirections
                        .actionCountersFragmentToCounterFragment().setCounterId(counter.id);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onMoved(Counter from, Counter to) {
                mCounters.onMove(from, to);
            }

            @Override
            public void onEdit(Counter counter) {
                Navigation.findNavController(requireView()).navigate(CountersFragmentDirections.
                        actionCountersFragmentToCreateEditCounterFragment().setCounterId(counter.id));
            }

            @Override
            public void onReset(List<Counter> counters) {
                mCounters.reset(counters, copy -> Snackbar.make(requireView(), getResources().getString(R.string
                        .counterReset), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                            mCounters.update(copy);
                        }).show());
            }

            @Override
            public void onExport(Intent intent) { startActivity(intent); }

            @Override
            public void onRemove(List<Counter> counters) {
                mCounters.remove(counters);
            }

            @Override
            public void onShowCreateDialog() {
                CounterCreateDialog.newInstance(mCounters.getCurrentGroup())
                        .show(getChildFragmentManager(), "addCounter");
            }

            @Override
            public void onDecSelected(List<Counter> selected) { mCounters.decSelected(selected); }

            @Override
            public void onIncSelected(List<Counter> selected) { mCounters.incSelected(selected); }

            @Override
            public void onGroupItemSelected(String group) {
                mCounters.setGroup(group);
            }

            @Override
            public void onAllCountersItemSelected() {
                mCounters.setGroup(null);
            }

            @Override
            public void onLoverVolume() { mCounters.onLoverVolume(); }

            @Override
            public void onRaiseVolume() { mCounters.onRaiseVolume(); }
        });

        mCounters.getGroups().observe(getViewLifecycleOwner(), mV::setGroups);
        mDisposables.add(mCounters.getCounters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(counters -> {
                    if (mCounters.getCurrentGroup() != null) {
                        mV.setCounters(mCounters.sortCounters(counters));
                        mV.setGroup(mCounters.getCurrentGroup());
                    } else {
                        mV.setCounters(counters);
                    }
                }));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mCounters.setGroup(mCounters.getCurrentGroup());
    }

    @Override
    public void onCreateCounter(String title, String group) {
        mCounters.createCounter(title, group);
    }

    @Override
    public void onDetailed() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(CountersFragmentDirections.actionCountersFragmentToCreateEditCounterFragment2());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mV.unregisterReceiver(requireContext());
        mDisposables.clear();
    }
}

