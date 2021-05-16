package com.yaroslavgorbachh.counter.screen.counters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.yaroslavgorbachh.counter.component.counters.Counters;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Repo;

import javax.inject.Inject;

import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CountersFragment extends Fragment {
   public CountersFragment(){
        super(R.layout.fragment_counters);
    }
    private static final String CURRENT_GROUP = "CURRENT_GROUP";

    private VolumeButtonBroadcastReceiver mMessageReceiver;
    private CountersAdapter mCountersAdapter;

    @Inject AudioManager mAudioManager;
    @Inject Accessibility accessibility;
    @Inject SharedPreferences sharedPreferences;
    @Inject Repo repo;
    private Counters mCounters;

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
        CountersView v = new CountersView(FragmentCountersBinding.bind(view), requireActivity(), new CountersView.Callback() {
            @Override
            public void onSettings() {
                Intent startSettingsActivity = new Intent(getContext(), SettingsActivity.class);
                startActivity(startSettingsActivity);
            }

            @Override
            public void onNoCounters() {
                new CreateCounterDialog().show(getParentFragmentManager(), "Add Counter");
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
        });

        /*callback for callback for handling back press button*/
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (v.getMultiSelection().getSelectionModState().getValue()) {
                    mCountersAdapter.clearSelectedCounters();
                } else {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        mCounters.getGroups().observe(getViewLifecycleOwner(), v::setGroups);
        mCounters.getCounters().observe(getViewLifecycleOwner(), v::setCounters);


        // Handling receiver witch MainActivity sends when volume buttons presed
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


//        /*set up listeners for selection mod*/
//        counterMultiCount.getSelectionModState().observe(getViewLifecycleOwner(), isSelectionMod -> {
//            setUpUiConfigurationForSelectionMod(isSelectionMod);
//            mMessageReceiver.setSelectionMod(isSelectionMod);
//            counterMultiCount.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
//                mToolbar.setTitle(getString(R.string.selectionModTitle, String.valueOf(count)));
//                if (count == 0)
//                    mToolbar.setTitle(currentItem);
//                mToolbar.getMenu().getItem(0).setVisible(count <= 1);
//            });
//        });
//
//    }
    }
    @Override
    public void onStart() {
        super.onStart();
        /*we initialise all this methods in on start because their behavior can change depends on pref*/
//        if (sharedPreferences.getBoolean("leftHandMod", false) && !mCountersAdapter.getLeftHandMod()) {
//            requireActivity().recreate();
//        }
//        if (!sharedPreferences.getBoolean("leftHandMod", false) && mCountersAdapter.getLeftHandMod()) {
//            requireActivity().recreate();
//        }
//
//        if (sharedPreferences.getBoolean("lockOrientation", true)) {
//            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        } else {
//            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        }

//        if (sharedPreferences.getBoolean("leftHandMod", false)) {
//            mIncAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_minus,  requireActivity().getTheme()));
//            mDecAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_plus,  requireActivity().getTheme()));
//            new FastCountButton(mDecAllSelectedCounters_bt, this::incSelectedCounters, sharedPreferences, null);
//            new FastCountButton(mIncAllSelectedCounters_bt, this::decSelectedCounters, sharedPreferences, null);
//        } else {
//            mDecAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_minus, requireActivity().getTheme()));
//            mIncAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_plus,  requireActivity().getTheme()));
//            new FastCountButton(mDecAllSelectedCounters_bt, this::decSelectedCounters, sharedPreferences, null);
//            new FastCountButton(mIncAllSelectedCounters_bt, this::incSelectedCounters, sharedPreferences, null);
//        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
////        long counterId = requireActivity().getIntent().getLongExtra(START_MAIN_ACTIVITY_EXTRA, -1);
////        if (counterId > 0) {
////            NavDirections action = CountersFragmentDirections
////                    .actionCountersFragmentToCounterFragment().setCounterId(counterId);
////            Navigation.findNavController(requireView()).navigate(action);
////            requireActivity().getIntent().putExtra(START_MAIN_ACTIVITY_EXTRA, -1);
////        }
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(CURRENT_GROUP, currentItem);
//    }
//
//    private void showIconIfListIsEmpty(int size) {
//        if (size <= 0) {
//            mIconAndTextThereAreNoCounters.setVisibility(View.VISIBLE);
//        } else {
//            mIconAndTextThereAreNoCounters.setVisibility(View.GONE);
//        }
//    }
//
//    private void incSelectedCounters() {
//        counterMultiCount.incAll();
//    }
//
//    private void decSelectedCounters() {
//        counterMultiCount.decAll();
//    }
//
//    /*set up toolbar configuration depending on selection mod*/
//    private void setUpUiConfigurationForSelectionMod(boolean isSelectionMod) {
//        if (isSelectionMod) {
//            Animations.showButtonsMultiSelection(mDecAllSelectedCounters_bt, mIncAllSelectedCounters_bt);
//            mToolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close, getActivity().getTheme()));
//            mToolbar.getMenu().clear();
//            mToolbar.inflateMenu(R.menu.menu_selection_mod);
//
//            mToolbar.setNavigationOnClickListener(v -> {
//                mCountersAdapter.clearSelectedCounters();
//            });
//
//            mToolbar.setOnMenuItemClickListener(menuItem -> {
//                switch (menuItem.getItemId()) {
//                    case R.id.editSelected:
//                        Counter counter = counterMultiCount.getFirstSelected();
//                        Navigation.findNavController(requireView()).navigate(CountersFragmentDirections.
//                                actionCountersFragmentToCreateEditCounterFragment().setCounterId(counter.id));
//                        break;
//                    case R.id.selectAllCounter:
//                        mCountersAdapter.selectAllCounters();
//                        break;
//                    case R.id.resetSelected:
//                        counterMultiCount.resetAll();
//                        Snackbar.make(requireView(), getResources().getString(R.string
//                                .counterReset), BaseTransientBottomBar.LENGTH_LONG)
//                                .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
//                                    counterMultiCount.undoResetAll();
//                                }).show();
//                        break;
//                    case R.id.exportSelected: {
//                        startActivity(Utility.getShareCountersInCSVIntent(counterMultiCount.getAllSelected()));
//                        break;
//                    }
//                    case R.id.deleteSelected:
//                        String title;
//                        if (counterMultiCount.getSelectedCount().getValue() >1){
//                            title = getString(R.string.deleteCountersDeleteDialog);
//                        }else {
//                            title = getString(R.string.deleteCounterDeleteDialog);
//                        }
//                        new MaterialAlertDialogBuilder(requireContext())
//                                .setTitle(title)
//                                .setMessage(R.string.deleteCounterDialogText)
//                                .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which)
//                                        -> counterMultiCount.deleteAll())
//                                .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
//                                .show();
//                        break;
//                }
//                return true;
//            });
//
//        } else {
//            Animations.hideButtonsMultiSelection(mDecAllSelectedCounters_bt, mIncAllSelectedCounters_bt);
//            mToolbar.setNavigationIcon(mNavigationIcon);
//            mToolbar.getMenu().clear();
//            mToolbar.inflateMenu(R.menu.menu_counter_main_fragment);
//            mToolbar.setTitle(currentItem);
//
//            mToolbar.setOnMenuItemClickListener(i -> {
//                if (i.getItemId() == R.id.counterAdd) {
//                    if (currentItem.equals(getResources().getString(R.string.allCountersItem))) {
//                        CreateCounterDialog.newInstance(null).show(getParentFragmentManager(), "addCounter");
//                    } else {
//                        CreateCounterDialog.newInstance(currentItem).show(getParentFragmentManager(), "addCounter");
//                    }
//                }
//                return true;
//            });
//
//            mToolbar.setNavigationOnClickListener(v -> mDrawer.open());
//        }
//    }

}

