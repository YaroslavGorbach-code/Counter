package com.yaroslavgorbachh.counter.fullscreenCounter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
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

import com.yaroslavgorbachh.counter.Animations;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.di.ViewModelProviderFactory;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class FullscreenCounterFragment extends Fragment {

    private static final int UI_ANIMATION_DELAY = 300;
    private int mSavedFlags;
    private ConstraintLayout mContentView;
    private LinearLayout mSwipeHelperLayout;
    private TextView mCounterValue_tv;
    private final Handler mHideHandler = new Handler();

    private FullscreenCounterViewModel mViewModel;
    private VolumeButtonBroadcastReceiver mMessageReceiver;
    @Inject ViewModelProviderFactory viewModelProviderFactory;

    private final Runnable mHidePart2Runnable = () -> {
        // Delayed removal of status and navigation bar
        int flags = View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        Activity activity = getActivity();
        if (activity != null
                && activity.getWindow() != null) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    };
    private final Runnable mHideRunnable = () -> mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication app = (MyApplication) requireActivity().getApplication();
        app.appComponent.fullscreenCounterComponent().create().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_counter, container, false);
        mContentView = view.findViewById(R.id.viewGroup);
        mCounterValue_tv = view.findViewById(R.id.value);
        mSwipeHelperLayout = view.findViewById(R.id.swipe_helper);
        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(FullscreenCounterViewModel.class);
        mViewModel.setCounterId(FullscreenCounterFragmentArgs.fromBundle(requireArguments()).getCounterId());

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        /* save previous status bar configuration to restore it when fullscreen fragment is destroyed */
        mSavedFlags = requireActivity().getWindow().getDecorView().getSystemUiVisibility();

        return view;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                mViewModel.decCounter(requireContext());
            }

            @Override
            public void incCounters() {
                mViewModel.incCounter(requireContext());
            }

            @Override
            public void lowerVolume() { }

            @Override
            public void raiseVolume() { }
        });

        mViewModel.counter.observe(getViewLifecycleOwner(), counter -> {
            if (mCounterValue_tv.getText().length()>1)
                Animations.hideSwipeHelper(mSwipeHelperLayout);
            mCounterValue_tv.setTextSize(Utility.getValueTvSize(counter));
            mCounterValue_tv.setText(String.valueOf(counter.value));

        });

        mContentView.setOnTouchListener(new CounterSwipeListener(getActivity()){
            @Override
            public void onSwipeTop() {
                mViewModel.incCounter(requireContext());
            }

            @Override
            public void onSwipeBottom() {
                mViewModel.decCounter(requireContext());
            }
        });
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