package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.yaroslavgorbachh.counter.component.fullscreen.FullscreenComponent;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class FullscreenFragment extends Fragment {

    public FullscreenFragment(){
      super(R.layout.fragment_fullscreen);
    }

    private static final int UI_ANIMATION_DELAY = 300;
    private int mSavedFlags;
    private final Handler mHideHandler = new Handler();
    private VolumeButtonBroadcastReceiver mMessageReceiver;
    private FullscreenComponent mFullscreenComponent;
    @Inject Repo repo;
    @Inject Accessibility accessibility;

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
                mFullscreenComponent.dec();
            }

            @Override
            public void incCounters() {
                mFullscreenComponent.inc();
            }

            @Override
            public void lowerVolume() { }

            @Override
            public void raiseVolume() { }
        });

        // init component
        long id = FullscreenFragmentArgs.fromBundle(requireArguments()).getCounterId();
        FullscreenViewModel vm = new ViewModelProvider(this).get(FullscreenViewModel.class);
        mFullscreenComponent = vm.getFullscreenCounter(repo, id);

        //init view
        FullscreenView v = new FullscreenView(FragmentFullscreenBinding.bind(view), accessibility, new FullscreenView.Callback() {
            @Override
            public void onBack() { Navigation.findNavController(view).popBackStack(); }

            @Override
            public void onSwipeTop() { mFullscreenComponent.inc(); }

            @Override
            public void onSwipeBottom() { mFullscreenComponent.dec(); }
        });

        mFullscreenComponent.getCounter().observe(getViewLifecycleOwner(), v::setCounter);

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