package com.yaroslavgorbachh.counter.counter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.Navigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.FastCountButton;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.di.ViewModelProviderFactory;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CounterFragment extends Fragment {
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private MaterialButton mResetButton;
    private TextView mCounterTitle;
    private View mLayout;
    private long mCounterId;
    private TextView mGroupTitle;
    private VolumeButtonBroadcastReceiver mMessageReceiver;

    private CounterViewModel mViewModel;

    @Inject ViewModelProviderFactory viewModelProviderFactory;
    @Inject SharedPreferences sharedPreferences;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.counterComponentFactory().create().inject(this);
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
        mGroupTitle = view.findViewById(R.id.groupTitle);

        mCounterId = CounterFragmentArgs.fromBundle(requireArguments()).getCounterId();

        mViewModel = new ViewModelProvider(this, viewModelProviderFactory).get(CounterViewModel.class);
        mViewModel.setCounterId(mCounterId);
        /*inflating menu, navigationIcon and set listeners*/
        Toolbar toolbar = view.findViewById(R.id.counterActivity_toolbar);
        toolbar.inflateMenu(R.menu.menu_counter_fragment);
        toolbar.setOnMenuItemClickListener(i -> {
            switch (i.getItemId()) {
                case R.id.counterDelete:
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle(getString(R.string.deleteCounterDeleteDialog))
                            .setMessage(R.string.deleteCounterDialogText)
                            .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which) -> {
                                mViewModel.deleteCounter();
                                Navigation.findNavController(view).popBackStack();
                            })
                            .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
                            .show();
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
            } else {
                Navigation.findNavController(view).popBackStack();
            }
        });

        /*counter +*/
        new FastCountButton(mIncButton, () -> {
            mViewModel.incCounter(requireContext());
        }, sharedPreferences);

        /*counter -*/
        new FastCountButton(mDecButton, () -> {
            mViewModel.decCounter(requireContext());
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
                mViewModel.decCounter(requireContext());
            }

            @Override
            public void incCounters() {
                mViewModel.incCounter(requireContext());
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
