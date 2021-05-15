package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenCounter;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenCounterBinding;
import com.yaroslavgorbachh.counter.feature.Animations;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.utill.Utility;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class FullscreenCounterFragment extends Fragment {

    public FullscreenCounterFragment(){
      super(R.layout.fragment_fullscreen_counter);
    }

    private static final int UI_ANIMATION_DELAY = 300;
    private int mSavedFlags;
    private final Handler mHideHandler = new Handler();
    private VolumeButtonBroadcastReceiver mMessageReceiver;
    private FullscreenCounter mFullscreenCounter;
    @Inject Repo repo;

    private final Runnable mHidePart2Runnable = () -> {
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if (requireActivity().getWindow() != null) {
            requireActivity().getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    };
    private final Runnable mHideRunnable = () -> mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication app = (MyApplication) requireActivity().getApplication();
        app.appComponent.inject(this);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSavedFlags = requireActivity().getWindow().getDecorView().getSystemUiVisibility();
        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                mFullscreenCounter.dec();
            }

            @Override
            public void incCounters() {
                mFullscreenCounter.inc();
            }

            @Override
            public void lowerVolume() { }

            @Override
            public void raiseVolume() { }
        });

        // init component
        long id = FullscreenCounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        FullscreenCounterViewModel vm = new ViewModelProvider(this).get(FullscreenCounterViewModel.class);
        mFullscreenCounter = vm.getFullscreenCounter(repo, id);

        //init view
        FullscreenCounterView v = new FullscreenCounterView(FragmentFullscreenCounterBinding.bind(view), new FullscreenCounterView.Callback() {
            @Override
            public void onBack() { Navigation.findNavController(view).popBackStack(); }

            @Override
            public void onSwipeTop() { mFullscreenCounter.inc(); }

            @Override
            public void onSwipeBottom() { mFullscreenCounter.dec(); }
        });

        mFullscreenCounter.getCounter().observe(getViewLifecycleOwner(), v::setCounter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        delayedHide();
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(mSavedFlags);
        }
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 100);
    }

}