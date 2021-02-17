package com.yaroslavgorbachh.counter.Fragments;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.yaroslavgorbachh.counter.Activityes.MainActivity;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.OnSwipeTouchListener;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CounterViewModel;
import com.yaroslavgorbachh.counter.ViewModels.Factories.CounterViewModelFactory;
import com.yaroslavgorbachh.counter.ViewModels.Factories.FullscreenCounterViewModelFactory;
import com.yaroslavgorbachh.counter.ViewModels.FullscreenCounterViewModel;

public class FullscreenCounterFragment extends Fragment {

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private FullscreenCounterViewModel mViewModel;
    private int mSavedFlags;
    private BroadcastReceiver mMessageReceiver;
    private ConstraintLayout mContentView;
    private TextView mCounterValue_tv;
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


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fullscreen_counter, container, false);
        mContentView = view.findViewById(R.id.viewGroup);
        mCounterValue_tv = view.findViewById(R.id.value);
        mViewModel = new ViewModelProvider(this, new FullscreenCounterViewModelFactory(
                requireActivity().getApplication(), FullscreenCounterFragmentArgs.
                fromBundle(getArguments()).getCounterId())).get(FullscreenCounterViewModel.class);
        Toolbar toolbar = view.findViewById(R.id.fullScreenToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> Navigation.findNavController(view).popBackStack());
        /* save previous status bar configuration to restore it when fullscreen fragment is destroyed */
        mSavedFlags = getActivity().getWindow().getDecorView().getSystemUiVisibility();

        return view;
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getIntExtra(MainActivity.KEYCODE_EXTRA,-1)){
                    case MainActivity.KEYCODE_VOLUME_DOWN:
                        mViewModel.decCounter(getView());
                        break;
                    case MainActivity.KEYCODE_VOLUME_UP:
                        mViewModel.incCounter(getView());
                        break;
                }
            }
        };

        mViewModel.counter.observe(getViewLifecycleOwner(), counter -> {
            mCounterValue_tv.setTextSize(Utility.getValueTvSize(counter));
            mCounterValue_tv.setText(String.valueOf(counter.value));
        });

        mContentView.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
            @Override
            public void onSwipeTop() {
                mViewModel.incCounter(getView());
            }

            @Override
            public void onSwipeBottom() {
                mViewModel.decCounter(getView());
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        delayedHide(100);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(MainActivity.ON_KEY_DOWN_BROADCAST));
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

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

}