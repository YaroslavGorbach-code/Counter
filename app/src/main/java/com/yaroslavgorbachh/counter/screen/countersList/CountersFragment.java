package com.yaroslavgorbachh.counter.screen.countersList;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.screen.counterSettings.SettingsActivity;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.feature.Animations;
import com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.MultiSelection.CounterMultiSelection;
import com.yaroslavgorbachh.counter.screen.countersList.DragAndDrop.MultiSelection.MultiCount;
import com.yaroslavgorbachh.counter.screen.countersList.navigationDrawer.CounterDrawerMenuItemSelector;
import com.yaroslavgorbachh.counter.screen.countersList.navigationDrawer.DrawerItemSelector;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.feature.FastCountButton;
import com.yaroslavgorbachh.counter.screen.createEditCounter.CreateCounterDialog;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.utill.Utility;
import com.yaroslavgorbachh.counter.data.Repo;

import java.util.stream.Collectors;

import javax.inject.Inject;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;
import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;
import static com.yaroslavgorbachh.counter.screen.counterWidget.CounterWidgetProvider.START_MAIN_ACTIVITY_EXTRA;

public class CountersFragment extends Fragment {
    private static final String CURRENT_GROUP = "CURRENT_GROUP";

    private Toolbar mToolbar;
    private Drawable mNavigationIcon;
    private GroupsAdapter mGroupsAdapter;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_drawerItem;
    private MaterialButton mIncAllSelectedCounters_bt;
    private MaterialButton mDecAllSelectedCounters_bt;
    private String currentItem;
    private ConstraintLayout mIconAndTextThereAreNoCounters;
    private ConstraintLayout mThereAreNoGroupsTextAndIcon;
    private VolumeButtonBroadcastReceiver mMessageReceiver;

    private CountersViewModel mViewModel;

    private RecyclerView mCounters_rv;
    private CountersAdapter mCountersAdapter;

    @Inject AudioManager mAudioManager;
    @Inject Accessibility accessibility;
    @Inject SharedPreferences sharedPreferences;
    @Inject Repo repo;
    private MultiCount counterMultiCount;
    private DrawerItemSelector drawerItemSelector;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        MyApplication application = (MyApplication) requireActivity().getApplication();
        application.appComponent.inject(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            currentItem = savedInstanceState.getString(CURRENT_GROUP);

        counterMultiCount = new CounterMultiSelection(repo, requireContext(), accessibility);
        drawerItemSelector = new CounterDrawerMenuItemSelector();
        /*callback for callback for handling back press button*/
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (counterMultiCount.getSelectionModState().getValue()) {
                    mCountersAdapter.clearSelectedCounters();
                } else {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counters, container, false);
        mDecAllSelectedCounters_bt = view.findViewById(R.id.dec_selected);
        mIncAllSelectedCounters_bt = view.findViewById(R.id.inc_selected);
        mAllCounters_drawerItem = view.findViewById(R.id.all_counters);
        mToolbar = view.findViewById(R.id.toolbar);
        mDrawer = view.findViewById(R.id.drawer);
        mCounters_rv = view.findViewById(R.id.list);
        mIconAndTextThereAreNoCounters = view.findViewById(R.id.no_counters);
        mThereAreNoGroupsTextAndIcon = view.findViewById(R.id.no_groups);

        mViewModel = new ViewModelProvider(this, new CountersViewModel.CountersVmFactory(repo))
                .get(CountersViewModel.class);


        /*navController set up*/
        NavigationView navigationDrawerView = view.findViewById(R.id.navigationDrawerView);
        NavController mNavController = Navigation.findNavController(requireActivity(), R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setDrawerLayout(mDrawer)
                .build();
        NavigationUI.setupWithNavController(mToolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationDrawerView, mNavController);
        mNavigationIcon = mToolbar.getNavigationIcon();

        /*when click set up the adapter with all the counters*/
        mAllCounters_drawerItem.setOnClickListener(i -> {
            mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
            new Handler().postDelayed(() -> mDrawer.closeDrawer(GravityCompat.START), 200);
        });

        view.findViewById(R.id.settings).setOnClickListener(i -> {
            Intent startSettingsActivity = new Intent(getContext(), SettingsActivity.class);
            startActivity(startSettingsActivity);
        });

        view.findViewById(R.id.no_counters).setOnClickListener(v -> {
            new CreateCounterDialog().show(getParentFragmentManager(), "Add Counter");
        });

        /*initialize RecyclerView and listener for groups*/
        mGroupsAdapter = new GroupsAdapter(drawerItemSelector);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        RecyclerView groups_rv = view.findViewById(R.id.groups_list);
        groups_rv.setLayoutManager(mLayoutManager);
        groups_rv.setAdapter(mGroupsAdapter);
        groups_rv.setHasFixedSize(true);

        mViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
            if (groups.size() > 0) {
                groups_rv.setVisibility(View.VISIBLE);
                mThereAreNoGroupsTextAndIcon.setVisibility(View.GONE);
            } else {
                if (!counterMultiCount.getSelectionModState().getValue()) {
                    groups_rv.setVisibility(View.GONE);
                    mThereAreNoGroupsTextAndIcon.setVisibility(View.VISIBLE);
                }
            }
            mGroupsAdapter.setGroups(Utility.deleteTheSameGroups(groups));
        });

        /*set up rv with counters*/
        mCountersAdapter = new CountersAdapter(new CountersAdapter.CounterItemClickListener() {
            @Override
            public void onPlusClick(Counter counter) {
                mViewModel.incCounter(counter, accessibility, requireContext());
            }

            @Override
            public void onMinusClick(Counter counter) {
                mViewModel.decCounter(counter, accessibility, requireContext());
            }

            @Override
            public void onOpen(Counter counter) {
                NavDirections action = CountersFragmentDirections
                        .actionCountersFragmentToCounterFragment().setCounterId(counter.id);
                Navigation.findNavController(view).navigate(action);
            }

            @Override
            public void onMoved(Counter counterFrom, Counter counterTo) {
                mViewModel.countersMoved(counterFrom, counterTo);
            }
        }, sharedPreferences, counterMultiCount);

        // Handling receiver witch MainActivity sends when volume buttons presed
        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                decSelectedCounters();
            }

            @Override
            public void incCounters() {
                incSelectedCounters();
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(mCounters_rv.getContext());
        mCounters_rv.setLayoutManager(layoutManager);
        mCounters_rv.setHasFixedSize(true);
        mCountersAdapter.itemTouchHelper.attachToRecyclerView(mCounters_rv);
        mCountersAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);

        if (currentItem != null && !currentItem.equals(getResources().getString(R.string.allCountersItem))) {
            mGroupsAdapter.selectItem(currentItem);
        } else {
            /*set up all counters in the adapter when first open*/
            mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
        }

        /*filter the list depending on the selected group */
        drawerItemSelector.getSelectedItemTitle().observe(getViewLifecycleOwner(), selectedItem -> {
            mViewModel.getCounters().removeObservers(getViewLifecycleOwner());
            mViewModel.getCounters().observe(getViewLifecycleOwner(), counters -> {
                if (!selectedItem.equals(getResources().getString(R.string.allCountersItem))) {
                    mCountersAdapter.setData(counters.stream()
                            .filter(counter -> counter.grope != null && counter.grope.equals(selectedItem)).collect(Collectors.toList()));

                    /*when all counters of particular group was deleted */
                    if (mCountersAdapter.getItemCount() == 0)
                        mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);

                } else {
                    mCountersAdapter.setData(counters);
                }

                showIconIfListIsEmpty(counters.size());
            });
            currentItem = selectedItem;
            mToolbar.setTitle(currentItem);
            mCountersAdapter.clearSelectedCounters();
            new Handler().postDelayed(() -> mDrawer.closeDrawer(GravityCompat.START), 200);

        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*set up listeners for selection mod*/
        counterMultiCount.getSelectionModState().observe(getViewLifecycleOwner(), isSelectionMod -> {
            setUpUiConfigurationForSelectionMod(isSelectionMod);
            mMessageReceiver.setSelectionMod(isSelectionMod);
            counterMultiCount.getSelectedCount().observe(getViewLifecycleOwner(), count -> {
                mToolbar.setTitle(getString(R.string.selectionModTitle, String.valueOf(count)));
                if (count == 0)
                    mToolbar.setTitle(currentItem);
                mToolbar.getMenu().getItem(0).setVisible(count <= 1);
            });
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        /*we initialise all this methods in on start because their behavior can change depends on pref*/
        if (sharedPreferences.getBoolean("leftHandMod", false) && !mCountersAdapter.getLeftHandMod()) {
            requireActivity().recreate();
        }
        if (!sharedPreferences.getBoolean("leftHandMod", false) && mCountersAdapter.getLeftHandMod()) {
            requireActivity().recreate();
        }

        if (sharedPreferences.getBoolean("lockOrientation", true)) {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            requireActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        if (sharedPreferences.getBoolean("leftHandMod", false)) {
            mIncAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_minus,  requireActivity().getTheme()));
            mDecAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_plus,  requireActivity().getTheme()));
            new FastCountButton(mDecAllSelectedCounters_bt, this::incSelectedCounters, sharedPreferences, null);
            new FastCountButton(mIncAllSelectedCounters_bt, this::decSelectedCounters, sharedPreferences, null);
        } else {
            mDecAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_minus, requireActivity().getTheme()));
            mIncAllSelectedCounters_bt.setIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_plus,  requireActivity().getTheme()));
            new FastCountButton(mDecAllSelectedCounters_bt, this::decSelectedCounters, sharedPreferences, null);
            new FastCountButton(mIncAllSelectedCounters_bt, this::incSelectedCounters, sharedPreferences, null);
        }
        mCounters_rv.setAdapter(mCountersAdapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        long counterId = requireActivity().getIntent().getLongExtra(START_MAIN_ACTIVITY_EXTRA, -1);
        if (counterId > 0) {
            NavDirections action = CountersFragmentDirections
                    .actionCountersFragmentToCounterFragment().setCounterId(counterId);
            Navigation.findNavController(requireView()).navigate(action);
            requireActivity().getIntent().putExtra(START_MAIN_ACTIVITY_EXTRA, -1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(mMessageReceiver);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_GROUP, currentItem);
    }

    private void showIconIfListIsEmpty(int size) {
        if (size <= 0) {
            mIconAndTextThereAreNoCounters.setVisibility(View.VISIBLE);
        } else {
            mIconAndTextThereAreNoCounters.setVisibility(View.GONE);
        }
    }

    private void incSelectedCounters() {
        counterMultiCount.incAll();
    }

    private void decSelectedCounters() {
        counterMultiCount.decAll();
    }

    /*set up toolbar configuration depending on selection mod*/
    private void setUpUiConfigurationForSelectionMod(boolean isSelectionMod) {
        if (isSelectionMod) {
            Animations.showButtonsMultiSelection(mDecAllSelectedCounters_bt, mIncAllSelectedCounters_bt);
            mToolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close, getActivity().getTheme()));
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_selection_mod);

            mToolbar.setNavigationOnClickListener(v -> {
                mCountersAdapter.clearSelectedCounters();
            });

            mToolbar.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.editSelected:
                        Counter counter = counterMultiCount.getFirstSelected();
                        Navigation.findNavController(requireView()).navigate(CountersFragmentDirections.
                                actionCountersFragmentToCreateEditCounterFragment().setCounterId(counter.id));
                        break;
                    case R.id.selectAllCounter:
                        mCountersAdapter.selectAllCounters();
                        break;
                    case R.id.resetSelected:
                        counterMultiCount.resetAll();
                        Snackbar.make(requireView(), getResources().getString(R.string
                                .counterReset), BaseTransientBottomBar.LENGTH_LONG)
                                .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                                    counterMultiCount.undoResetAll();
                                }).show();
                        break;
                    case R.id.exportSelected: {
                        startActivity(Utility.getShareCountersInCSVIntent(counterMultiCount.getAllSelected()));
                        break;
                    }
                    case R.id.deleteSelected:
                        String title;
                        if (counterMultiCount.getSelectedCount().getValue() >1){
                            title = getString(R.string.deleteCountersDeleteDialog);
                        }else {
                            title = getString(R.string.deleteCounterDeleteDialog);
                        }
                        new MaterialAlertDialogBuilder(requireContext())
                                .setTitle(title)
                                .setMessage(R.string.deleteCounterDialogText)
                                .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which)
                                        -> counterMultiCount.deleteAll())
                                .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
                                .show();
                        break;
                }
                return true;
            });

        } else {
            Animations.hideButtonsMultiSelection(mDecAllSelectedCounters_bt, mIncAllSelectedCounters_bt);
            mToolbar.setNavigationIcon(mNavigationIcon);
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_counter_main_fragment);
            mToolbar.setTitle(currentItem);

            mToolbar.setOnMenuItemClickListener(i -> {
                if (i.getItemId() == R.id.counterAdd) {
                    if (currentItem.equals(getResources().getString(R.string.allCountersItem))) {
                        CreateCounterDialog.newInstance(null).show(getParentFragmentManager(), "addCounter");
                    } else {
                        CreateCounterDialog.newInstance(currentItem).show(getParentFragmentManager(), "addCounter");
                    }
                }
                return true;
            });

            mToolbar.setNavigationOnClickListener(v -> mDrawer.open());
        }
    }

}

