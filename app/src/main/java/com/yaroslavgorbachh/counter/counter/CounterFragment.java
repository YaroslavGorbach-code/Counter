package com.yaroslavgorbachh.counter.counter;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.FastCountButton;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.component.CounterComp;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;
import com.yaroslavgorbachh.counter.databinding.FragmentCounterBinding;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CounterFragment extends Fragment {

    private VolumeButtonBroadcastReceiver mMessageReceiver;
    @Inject SharedPreferences sharedPreferences;
    @Inject Repo repo;
    @Inject Accessibility accessibility;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        boolean leftMod = sharedPreferences.getBoolean("leftHand", false);
        View view;
        if (leftMod){
            view = inflater.inflate(R.layout.fragment_counter, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_counter_left_hand, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        long counterId = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();

        // init component
        long id = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        CounterViewModel vm = new ViewModelProvider(this).get(CounterViewModel.class);
        CounterComp counter = vm.getCounterComponent(repo, id);

        // init view
        CounterView v = new CounterView(FragmentCounterBinding.bind(requireView()), new CounterView.Callback() {
            @Override
            public void onDelete() {
                new MaterialAlertDialogBuilder(requireContext())
                        .setTitle(getString(R.string.deleteCounterDeleteDialog))
                        .setMessage(R.string.deleteCounterDialogText)
                        .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which) -> {
                            counter.delete();
                            Navigation.findNavController(view).popBackStack();
                        })
                        .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
                        .show();
            }

            @Override
            public void onEdit() {
                NavDirections action = (NavDirections) CounterFragmentDirections
                        .actionCounterFragmentToCreateEditCounterFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onHistory() {
                NavDirections action = (NavDirections) CounterFragmentDirections
                        .actionCounterFragmentToCounterHistoryFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onAbout() {
                NavDirections action = (NavDirections) CounterFragmentDirections
                        .actionCounterFragmentToAboutCounterFragment()
                        .setCounterId(counterId);
                Navigation.findNavController(view)
                        .navigate(action);
            }

            @Override
            public void onFullScreen() {
                NavDirections action = (NavDirections) CounterFragmentDirections.
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
            public void onInc(View view) {
                new FastCountButton(view, counter::incCounter, sharedPreferences, accessibility);
            }

            @Override
            public void onDec(View view) {
                new FastCountButton(view, counter::decCounter, sharedPreferences, accessibility);
            }

            @Override
            public void onReset() {
                counter.resetCounter();
                Snackbar.make(requireView(), getResources().getString(R.string.counterReset), Snackbar.LENGTH_LONG)
                        .setAction(getResources().getString(R.string.counterResetUndo), v1 -> counter.undoReset()).show();
            }

        });

        counter.getCounter().observe(getViewLifecycleOwner(), v::setCounter);

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
