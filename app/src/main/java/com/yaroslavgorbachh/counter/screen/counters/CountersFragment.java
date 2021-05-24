package com.yaroslavgorbachh.counter.screen.counters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.component.counters.CountersComponent;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;

import java.util.List;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CountersFragment extends Fragment implements CounterCreateDialog.Host {
    @Inject AudioManager mAudioManager;
    @Inject Repo repo;
    private CountersComponent mCountersComponent;
    private CountersView mV;
    public CountersFragment() {
        super(R.layout.fragment_counters);
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
        CountersViewModel vm = new ViewModelProvider(this).get(CountersViewModel.class);
        mCountersComponent = vm.getCountersComponent(repo);

        // init view
        mV = new CountersView(FragmentCountersBinding.bind(view), requireActivity(), this, new CountersView.Callback() {
            @Override
            public void onSettings() {
                startActivity(new Intent(getContext(), SettingsActivity.class));
            }

            @Override
            public void onInc(Counter counter) {
                mCountersComponent.inc(counter.id);
            }

            @Override
            public void onDec(Counter counter) {
                mCountersComponent.dec(counter.id);
            }

            @Override
            public void onOpen(Counter counter) {
                NavDirections action = CountersFragmentDirections
                        .actionCountersFragmentToCounterFragment().setCounterId(counter.id);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onMoved(Counter from, Counter to) {
                mCountersComponent.onMove(from, to);
            }

            @Override
            public void onEdit(Counter counter) {
                Navigation.findNavController(requireView()).navigate(CountersFragmentDirections.
                        actionCountersFragmentToCreateEditCounterFragment().setCounterId(counter.id));
            }

            @Override
            public void onReset(List<Counter> counters) {
                mCountersComponent.reset(counters, copy -> Snackbar.make(requireView(), getResources().getString(R.string
                        .counterReset), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                            mCountersComponent.update(copy);
                        }).show());

            }

            @Override
            public void onExport(Intent intent) { startActivity(intent); }

            @Override
            public void onRemove(List<Counter> counters) {
                mCountersComponent.remove(counters);
            }

            @Override
            public void onShowCreateDialog() {
                CounterCreateDialog.newInstance(mCountersComponent.getCurrentGroup())
                        .show(getChildFragmentManager(), "addCounter");
            }

            @Override
            public void onDecSelected(List<Counter> selected) {
                mCountersComponent.decSelected(selected);
            }

            @Override
            public void onIncSelected(List<Counter> selected) {
                mCountersComponent.incSelected(selected);
            }

            @Override
            public void onGroupItemSelected(String group) {
                mCountersComponent.setGroup(group);
            }

            @Override
            public void onAllCountersItemSelected() {
                mCountersComponent.setGroup(null);
            }

            @Override
            public void onLoverVolume() {
                mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void onRaiseVolume() {
                mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

        mCountersComponent.getGroups().observe(getViewLifecycleOwner(), mV::setGroups);
        mCountersComponent.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if (mCountersComponent.getCurrentGroup() != null) {
                mV.setCounters(mCountersComponent.sortCounters(counters));
                mV.setGroup(mCountersComponent.getCurrentGroup());
            } else {
                mV.setCounters(counters);
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mCountersComponent.setGroup(mCountersComponent.getCurrentGroup());
    }

    @Override
    public void onCreateCounter(String title, String group) {
        mCountersComponent.createCounter(title, group);
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
    }
}

