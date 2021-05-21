package com.yaroslavgorbachh.counter.screen.counters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
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
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;
import com.yaroslavgorbachh.counter.util.Utility;

import java.util.List;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CountersFragment extends Fragment implements CounterCreateDialog.Host {
    @Inject AudioManager mAudioManager;
    @Inject Accessibility accessibility;
    @Inject SharedPreferences sharedPreferences;
    @Inject Repo repo;

    private VolumeButtonBroadcastReceiver mMessageReceiver;
    private Counters mCounters;

    public CountersFragment() {
        super(R.layout.fragment_counters);
    }

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
        CountersViewModel vm = new ViewModelProvider(this).get(CountersViewModel.class);
        mCounters = vm.getCountersComponent(repo);

        // init view
        CountersView v = new CountersView(FragmentCountersBinding.bind(view),
                requireActivity(), this, new CountersView.Callback() {
            @Override
            public void onSettings() {
                Intent startSettingsActivity = new Intent(getContext(), SettingsActivity.class);
                startActivity(startSettingsActivity);
            }

            @Override
            public void onInc(Counter counter) {
                mCounters.inc(counter.id);
            }

            @Override
            public void onDec(Counter counter) {
                mCounters.dec(counter.id);
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
            public void onExport(List<Counter> counters) {
                startActivity(Utility.getShareCountersInCSVIntent(counters));
            }

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
            public void onDecSelected(List<Counter> selected) {
                mCounters.decSelected(selected);
            }

            @Override
            public void onIncSelected(List<Counter> selected) {
                mCounters.incSelected(selected);
            }

            @Override
            public void onGroupItemSelected(String group) {
                mCounters.setGroup(group);
            }

            @Override
            public void onAllCountersItemSelected() {
                mCounters.setGroup(null);
            }
        });

        mCounters.getGroups().observe(getViewLifecycleOwner(), v::setGroups);
        mCounters.getCounters().observe(getViewLifecycleOwner(), counters -> {
            if (mCounters.getCurrentGroup() != null) {
                v.setCounters(mCounters.sortCounters(counters));
                v.setGroup(mCounters.getCurrentGroup());
            } else {
                v.setCounters(counters);
            }
        });

        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                // TODO: 5/16/2021 dec selected
            }

            @Override
            public void incCounters() {
                // TODO: 5/16/2021 inc selected
            }

            @Override
            public void lowerVolume() {
                mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
            }

            @Override
            public void raiseVolume() {
                mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
            }
        });

        /*Register to receive messages.*/
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

    }

    @Override
    public void onViewStateRestored(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mCounters.setGroup(mCounters.getCurrentGroup());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onCreateCounter(String title, String group) {
        mCounters.createCounter(title, group);
    }

    @Override
    public void onDetailed() {
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(CountersFragmentDirections.actionCountersFragmentToCreateEditCounterFragment2());
        Utility.hideKeyboard(requireActivity());
    }

}

