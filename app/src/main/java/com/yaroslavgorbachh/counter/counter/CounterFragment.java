package com.yaroslavgorbachh.counter.counter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.content.Context;
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
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.FastCountButton;
import com.yaroslavgorbachh.counter.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CounterViewModel;
import com.yaroslavgorbachh.counter.ViewModels.Factories.CounterViewModelFactory;
import com.yaroslavgorbachh.counter.R;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CounterFragment extends Fragment {
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private MaterialButton mResetButton;
    private CounterViewModel mViewModel;
    private TextView mCounterTitle;
    private View mLayout;
    private long mCounterId;
    private Button mSaveToHistoryButton;
    private ImageView mAllInclusiveMin_iv;
    private ImageView mAllInclusiveMAx_iv;
    private TextView mMaxValue_tv;
    private TextView mMinValue_tv;
    private TextView mGroupTitle;
    private VolumeButtonBroadcastReceiver mMessageReceiver;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;

        /*depending on pref set layout*/
        if (sharedPreferences.getBoolean("leftHandMod", false)) {
            view = inflater.inflate(R.layout.fragment_counter_left_hand, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_counter, container, false);
        }

        /*initialize fields*/
        mValue_tv = view.findViewById(R.id.value);
        mIncButton = view.findViewById(R.id.inc_value);
        mDecButton = view.findViewById(R.id.dec_value);
        mResetButton = view.findViewById(R.id.reset_value);
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
        Toolbar toolbar = view.findViewById(R.id.counterActivity_toolbar);
        toolbar.inflateMenu(R.menu.menu_counter_fragment);
        toolbar.setOnMenuItemClickListener(i -> {
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
                case R.id.fullScreen:
                    Navigation.findNavController(view).navigate(CounterFragmentDirections.
                            actionCounterFragmentToFullscreenCounterFragment().setCounterId(mCounterId));
                    break;
            }
            return true;
        });

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(i -> Navigation.findNavController(view).popBackStack());

        /*listener for current counter*/
        mViewModel.counter.observe(getViewLifecycleOwner(), counter -> {

            /*if counter == null that means it was deleted*/
            if (counter != null) {
                mValue_tv.setTextSize(Utility.getValueTvSize(mViewModel.counter.getValue()));
                mValue_tv.setText(String.valueOf(counter.value));
                mCounterTitle.setText(counter.title);
                mGroupTitle.setText(counter.grope);


                if (counter.maxValue != Counter.MAX_VALUE) {
                    mAllInclusiveMAx_iv.setVisibility(View.GONE);
                    mMaxValue_tv.setVisibility(View.VISIBLE);
                    mMaxValue_tv.setText(String.valueOf(counter.maxValue));
                } else {
                    mAllInclusiveMAx_iv.setVisibility(View.VISIBLE);
                    mMaxValue_tv.setVisibility(View.GONE);
                }

                if (counter.minValue != Counter.MIN_VALUE) {
                    mAllInclusiveMin_iv.setVisibility(View.GONE);
                    mMinValue_tv.setVisibility(View.VISIBLE);
                    mMinValue_tv.setText(String.valueOf(counter.minValue));
                } else {
                    mAllInclusiveMin_iv.setVisibility(View.VISIBLE);
                    mMinValue_tv.setVisibility(View.GONE);
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
        }, sharedPreferences);

        /*counter -*/
        new FastCountButton(mDecButton, () -> {
            mViewModel.decCounter(getView());
        }, sharedPreferences);

        /*reset counter*/
        mResetButton.setOnClickListener(v -> {
            mViewModel.resetCounter();
            Snackbar.make(mLayout, getResources().getString(R.string.counterReset), BaseTransientBottomBar.LENGTH_LONG)
                    .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                        mViewModel.restoreValue();
                    }).show();
        });

        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                mViewModel.decCounter(getView());
            }

            @Override
            public void incCounters() {
                mViewModel.incCounter(getView());
            }

            @Override
            public void lowerVolume() {}
            @Override
            public void raiseVolume() {}
        });

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
