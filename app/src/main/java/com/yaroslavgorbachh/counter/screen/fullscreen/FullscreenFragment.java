package com.yaroslavgorbachh.counter.screen.fullscreen;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.yaroslavgorbachh.counter.component.fullscreen.Fullscreen;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.databinding.FragmentFullscreenBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class FullscreenFragment extends Fragment {

    public FullscreenFragment(){
      super(R.layout.fragment_fullscreen);
    }

    private static final int UI_ANIMATION_DELAY = 300;
    private int mSavedFlags;
    private final Handler mHideHandler = new Handler();
    private FullscreenView mV;
    @Inject Fullscreen mFullscreen;

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


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSavedFlags = requireActivity().getWindow().getDecorView().getSystemUiVisibility();

        // init component
        long id = FullscreenFragmentArgs.fromBundle(requireArguments()).getCounterId();
        FullscreenViewModel vm = new ViewModelProvider(this).get(FullscreenViewModel.class);
        vm.getFullscreenCounter(id).inject(this);

        //init view
        mV = new FullscreenView(FragmentFullscreenBinding.bind(view), new FullscreenView.Callback() {
            @Override
            public void onBack() { Navigation.findNavController(view).popBackStack(); }

            @Override
            public void onSwipeTop() { mFullscreen.inc(); }

            @Override
            public void onSwipeBottom() { mFullscreen.dec(); }
        });

        mFullscreen.getCounter()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mV::setCounter);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        delayedHide();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getActivity() != null && getActivity().getWindow() != null) {
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(0);
            getActivity().getWindow().getDecorView().setSystemUiVisibility(mSavedFlags);
        }
    }

    private void delayedHide() {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 100);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mV.unregisterReceiver(requireContext());
    }
}