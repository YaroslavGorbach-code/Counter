package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Activity;
import android.app.Instrumentation;
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
import com.yaroslavgorbachh.counter.screen.settings.SettingsFragment;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class CountersFragment extends Fragment implements CounterCreateDialog.Host {
    private CountersView mV;
    private final CompositeDisposable mDisposables = new CompositeDisposable();
    @Inject Counters counters;

    public CountersFragment() {
        super(R.layout.fragment_counters);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SettingsActivity.RECREATE_RESULT_CODE && resultCode == Activity.RESULT_OK){
            requireActivity().recreate();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // inject component
        CountersViewModel vm = new ViewModelProvider(this).get(CountersViewModel.class);
        vm.countersComponent.inject(this);

        // init view
        mV = new CountersView(FragmentCountersBinding.bind(view), counters.getFastCountInterval(), requireActivity(), this, new CountersView.Callback() {
            @Override
            public void onSettings() {
                startActivityForResult(
                        new Intent(getContext(), SettingsActivity.class),
                        SettingsActivity.RECREATE_RESULT_CODE);
            }

            @Override
            public void onInc(Counter counter) {
                counters.inc(counter);
            }

            @Override
            public void onDec(Counter counter) {
                counters.dec(counter);
            }

            @Override
            public void onOpen(Counter counter) {
                NavDirections action = CountersFragmentDirections
                        .actionCountersFragmentToCounterFragment().setCounterId(counter.id);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onMoved(Counter from, Counter to) {
                counters.onMove(from, to);
            }

            @Override
            public void onEdit(Counter counter) {
                Navigation.findNavController(requireView()).navigate(CountersFragmentDirections.
                        actionCountersFragmentToCreateEditCounterFragment().setCounterId(counter.id));
            }

            @Override
            public void onReset(List<Counter> counters) {
                CountersFragment.this.counters.reset(counters, copy -> Snackbar.make(requireView(), getResources().getString(R.string
                        .counterReset), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                            CountersFragment.this.counters.update(copy);
                        }).show());
            }

            @Override
            public void onExport(Intent intent) { startActivity(intent); }

            @Override
            public void onRemove(List<Counter> counters) {
                CountersFragment.this.counters.remove(counters);
            }

            @Override
            public void onShowCreateDialog() {
                CounterCreateDialog.newInstance(counters.getCurrentGroup())
                        .show(getChildFragmentManager(), "addCounter");
            }

            @Override
            public void onDecSelected(List<Counter> selected) { counters.decSelected(selected); }

            @Override
            public void onIncSelected(List<Counter> selected) { counters.incSelected(selected); }

            @Override
            public void onGroupItemSelected(String group) {
                counters.setGroup(group);
            }

            @Override
            public void onAllCountersItemSelected() {
                counters.setGroup(null);
            }

            @Override
            public void onLoverVolume() { counters.onLoverVolume(); }

            @Override
            public void onRaiseVolume() { counters.onRaiseVolume(); }
        });

        counters.getGroups().observe(getViewLifecycleOwner(), mV::setGroups);
        mDisposables.add(counters.getCounters()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(counters -> {
                    if (this.counters.getCurrentGroup() != null) {
                        mV.setCounters(this.counters.sortCounters(counters));
                        mV.setGroup(this.counters.getCurrentGroup());
                    } else {
                        mV.setCounters(counters);
                    }
                }));
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        counters.setGroup(counters.getCurrentGroup());
    }

    @Override
    public void onCreateCounter(String title, String group) {
        counters.createCounter(title, group);
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

