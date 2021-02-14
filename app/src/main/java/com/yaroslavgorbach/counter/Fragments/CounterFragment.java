package com.yaroslavgorbach.counter.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbach.counter.ViewModels.CounterViewModel;
import com.yaroslavgorbach.counter.ViewModels.Factories.CounterViewModelFactory;
import com.yaroslavgorbach.counter.R;

import static com.yaroslavgorbach.counter.Activityes.MainActivity.KEYCODE_EXTRA;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.KEYCODE_VOLUME_DOWN;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.KEYCODE_VOLUME_UP;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.ON_KEY_DOWN_BROADCAST;

public class CounterFragment extends Fragment {
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private MaterialButton mResetButton;
    private CounterViewModel mViewModel;
    private Toolbar mToolbar;
    private TextView mCounterTitle;
    private View mLayout;
    private long mCounterId;
    private Button mSaveToHistoryButton;
    private ImageView mAllInclusiveMin_iv;
    private ImageView mAllInclusiveMAx_iv;
    private TextView mMaxValue_tv;
    private TextView mMinValue_tv;
    private TextView mGroupTitle;
    private BroadcastReceiver mMessageReceiver;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        /*depending on pref set layout*/
        if (sharedPreferences.getBoolean("leftHandMod", false )){
          view = inflater.inflate(R.layout.fragment_counter_left_hand, container, false);
        }else {
            view = inflater.inflate(R.layout.fragment_counter, container, false);
        }

        /*initialize fields*/
        mValue_tv = view.findViewById(R.id.value);
        mIncButton = view.findViewById(R.id.inc_value);
        mDecButton = view.findViewById(R.id.dec_value);
        mResetButton = view.findViewById(R.id.reset_value);
        mToolbar = view.findViewById(R.id.counterActivity_toolbar);
        mCounterTitle = view.findViewById(R.id.counterTitle);
        mLayout = view.findViewById(R.id.counterLayout);
        mSaveToHistoryButton = view.findViewById(R.id.saveToHistoryButton);
        mAllInclusiveMAx_iv = view.findViewById(R.id.iconAllInclusiveMax);
        mAllInclusiveMin_iv = view.findViewById(R.id.iconAllInclusiveMin);
        mMaxValue_tv = view.findViewById(R.id.maxValue);
        mMinValue_tv = view.findViewById(R.id.minValue);
        mGroupTitle = view.findViewById(R.id.groupTitle);
        mCounterId = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();
        mViewModel = new ViewModelProvider(this, new CounterViewModelFactory(requireActivity().getApplication(),
                mCounterId)).get(CounterViewModel.class);

        /*inflating menu, navigationIcon and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_activiry);
        mToolbar.setOnMenuItemClickListener(i -> {
            switch (i.getItemId()) {
                case R.id.counterDelete:
                    new DeleteCounterDialog(() -> {
                        mViewModel.deleteCounter();
                        Navigation.findNavController(view).popBackStack();
                    }, 1).show(getChildFragmentManager(), "DialogCounterDelete");
                    break;
                case R.id.counterEdit:
                    Navigation.findNavController(view).navigate(CounterFragmentDirections.
                            actionCounterFragmentToCreateEditCounterFragment().setCounterId(mCounterId));
                    break;
                case R.id.counterHistory:
                    Navigation.findNavController(view).navigate(CounterFragmentDirections.
                            actionCounterFragmentToCounterHistoryFragment().setCounterId(mCounterId));
                    break;
                case R.id.aboutCounter:
                    Navigation.findNavController(view).navigate(CounterFragmentDirections.
                            actionCounterFragmentToAboutCounterFragment().setCounterId(mCounterId));
                    break;
            }
            return true;
        });

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i -> Navigation.findNavController(view).popBackStack());

        /*listener for current counter*/
        mViewModel.mCounter.observe(getViewLifecycleOwner(), counter -> {

            /*if counter == null that means it was deleted*/
            if (counter != null) {
                mValue_tv.setTextSize(mViewModel.getValueTvSize());
                mValue_tv.setText(String.valueOf(counter.value));
                mCounterTitle.setText(counter.title);
                mGroupTitle.setText(counter.grope);

                if (counter.maxValue != Counter.MAX_VALUE) {
                    mAllInclusiveMAx_iv.setVisibility(View.GONE);
                    mMaxValue_tv.setVisibility(View.VISIBLE);
                    mMaxValue_tv.setText(String.valueOf(counter.maxValue));
                }

                if (counter.minValue != Counter.MIN_VALUE) {
                    mAllInclusiveMin_iv.setVisibility(View.GONE);
                    mMinValue_tv.setVisibility(View.VISIBLE);
                    mMinValue_tv.setText(String.valueOf(counter.minValue));
                }

            } else {
                Navigation.findNavController(view).popBackStack();
            }
        });

        /*saving counter value to history*/
        mSaveToHistoryButton.setOnClickListener(v -> {
            mViewModel.saveValueToHistory();
        });

        /*counter +*/
        new FastCountButton(mIncButton, () -> {
            mViewModel.incCounter(getView());
        });

        /*counter -*/
        new FastCountButton(mDecButton, () -> {
            mViewModel.decCounter(getView());
        });

        /*reset counter*/
        mResetButton.setOnClickListener(v -> {
            mViewModel.resetCounter();
            Snackbar.make(mLayout, getResources().getString(R.string.counterReset), BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                        mViewModel.restoreValue();
                    }).show();
        });

           mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getIntExtra(KEYCODE_EXTRA,-1)){
                    case KEYCODE_VOLUME_DOWN:
                        mViewModel.decCounter(getView());
                        break;
                    case KEYCODE_VOLUME_UP:
                        mViewModel.incCounter(getView());
                        break;
                }
            }
        };
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }
}
