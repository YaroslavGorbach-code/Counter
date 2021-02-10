package com.yaroslavgorbach.counter.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbach.counter.Accessibility;
import com.yaroslavgorbach.counter.Activityes.SettingsActivity;
import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.Fragments.Dialogs.CreateCounterDialog;
import com.yaroslavgorbach.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.GroupsAdapter;
import com.yaroslavgorbach.counter.Utility;
import com.yaroslavgorbach.counter.ViewModels.CountersViewModel;
import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.KEYCODE_EXTRA;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.KEYCODE_VOLUME_DOWN;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.KEYCODE_VOLUME_UP;
import static com.yaroslavgorbach.counter.Activityes.MainActivity.ON_KEY_DOWN_BROADCAST;

public class CountersFragment extends Fragment  {
    private CountersViewModel mViewModel;
    private RecyclerView mCounters_rv;
    private RecyclerView mGroups_rv;
    private CountersAdapter mCountersAdapter;
    private Toolbar mToolbar;
    private Drawable mNavigationIcon;
    private GroupsAdapter mGroupsAdapter;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_drawerItem;
    private NavigationView mNavigationDrawerView;
    private NavController mNavController;
    private TextView mIncAllSelectedCounters_bt;
    private TextView mDecAllSelectedCounters_bt;
    private String currentItem;
    private LinearLayout mSettingsDrawerItem;
    private Accessibility mAccessibility;


    private static final String CURRENT_GROUP = "CURRENT_GROUP";
    private BroadcastReceiver mMessageReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null)
            currentItem = savedInstanceState.getString(CURRENT_GROUP);

        /*callback for callback for handling back press button*/
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mCountersAdapter.getSelectionMod().getValue()){
                    mCountersAdapter.clearSelectedCounters();
                }else {
                    requireActivity().finish();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.counters_fragment, container, false);

        mViewModel = new ViewModelProvider(this).get(CountersViewModel.class);
        mDecAllSelectedCounters_bt = view.findViewById(R.id.allSelectedDec);
        mIncAllSelectedCounters_bt = view.findViewById(R.id.allSelectedInc);
        mAllCounters_drawerItem = view.findViewById(R.id.AllCounters);
        mNavigationDrawerView = view.findViewById(R.id.navigationDrawerView);
        mToolbar = view.findViewById(R.id.toolbar_mainActivity);
        mDrawer = view.findViewById(R.id.drawer);
        mCounters_rv = view.findViewById(R.id.counters_list);
        mGroups_rv = view.findViewById(R.id.groupsList_rv);
        mSettingsDrawerItem = view.findViewById(R.id.settings);

        /*navController set up*/
        mNavController = Navigation.findNavController(requireActivity(), R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setDrawerLayout(mDrawer)
                .build();
        NavigationUI.setupWithNavController(mToolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationDrawerView, mNavController);
        mNavigationIcon = mToolbar.getNavigationIcon();

        /*when click set up the adapter with all the counters*/
        mAllCounters_drawerItem.setOnClickListener(i->{
            mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
            new Handler().postDelayed(()-> mDrawer.closeDrawer(GravityCompat.START), 200);
        });

        mSettingsDrawerItem.setOnClickListener(i->{
            Intent startSettingsActivity = new Intent(getContext(), SettingsActivity.class);
            startActivity(startSettingsActivity);
        });

        /*initialize RecyclerView and listener for groups*/
        mGroupsAdapter = new GroupsAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        mGroups_rv.setLayoutManager(mLayoutManager);
        mGroups_rv.setAdapter(mGroupsAdapter);
        mGroups_rv.setHasFixedSize(true);

        mViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
                mGroupsAdapter.setGroups(Utility.deleteTheSameGroups(groups));
        });

        /*set up rv with counters*/
        mCountersAdapter = new CountersAdapter(new CountersAdapter.CounterItemListeners() {
            @Override
            public void onPlusClick(Counter counter) {
                mViewModel.incCounter(counter);
                mAccessibility.playIncSoundEffect();
                mAccessibility.playIncVibrationEffect(getView());
            }

            @Override
            public void onMinusClick(Counter counter) {
                mViewModel.decCounter(counter);
                mAccessibility.playDecSoundEffect();
                mAccessibility.playDecVibrationEffect(getView());
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
        }, requireActivity().getApplication());


        // Our handler for received Intents. This will be called whenever an Intent
        // with an action named "custom-event-name" is broadcasted.
        mMessageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // Get extra data included in the Intent
                switch (intent.getIntExtra(KEYCODE_EXTRA,-1)){
                    case KEYCODE_VOLUME_DOWN:
                        mCountersAdapter.decSelectedCounters();
                        if (mCountersAdapter.getSelectionMod().getValue()){
                            mAccessibility.playDecSoundEffect();
                            mAccessibility.playDecVibrationEffect(getView());
                            break;
                        }else {
                            // TODO: 2/8/2021 уменьшить громкость
                        }
                    case KEYCODE_VOLUME_UP:
                        mCountersAdapter.incSelectedCounters();
                        if (mCountersAdapter.getSelectionMod().getValue()){
                            mAccessibility.playIncSoundEffect();
                            mAccessibility.playIncVibrationEffect(getView());
                            break;
                        }else {
                            // TODO: 2/8/2021 увеличить громкость
                        }
                }
            }
        };

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions named "custom-event-name".
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mCounters_rv.getContext());
        mCounters_rv.setLayoutManager(layoutManager);
        mCounters_rv.setHasFixedSize(true);
        mCounters_rv.getItemAnimator().setAddDuration(0);
        mCounters_rv.getItemAnimator().setChangeDuration(0);
        mCounters_rv.getItemAnimator().setRemoveDuration(0);
        mCountersAdapter.itemTouchHelper.attachToRecyclerView(mCounters_rv);
        mCountersAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);

        if (currentItem != null && !currentItem.equals(getResources().getString(R.string.AllCountersItem))) {
            mGroupsAdapter.restoreSelectedItem(currentItem);
        } else {
            /*set up all counters in the adapter when first open*/
            mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
        }

        mGroupsAdapter.getSelectedItem().observe(getViewLifecycleOwner(), selectedItem -> {
            if(selectedItem.equals(getResources().getString(R.string.AllCountersItem))){
                currentItem = getResources().getString(R.string.AllCountersItem);
                mToolbar.setTitle(currentItem);
                mViewModel.mCounters.removeObservers(getViewLifecycleOwner());
                mViewModel.mCounters.observe(getViewLifecycleOwner(), counters -> {
                    mCountersAdapter.setData(counters);
                });

            }else {
                mViewModel.mCounters.removeObservers(getViewLifecycleOwner());
                mToolbar.setTitle(selectedItem);
                mViewModel.getCountersByGroup(selectedItem).observe(getViewLifecycleOwner(), counters -> {
                    if (currentItem.equals(selectedItem) ){
                        mCountersAdapter.setData(counters);
                    }
                });
            }
            currentItem = selectedItem;
            mCountersAdapter.clearSelectedCounters();
            new Handler().postDelayed(()-> mDrawer.closeDrawer(GravityCompat.START), 200);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*set up listeners for selection mod*/
        mCountersAdapter.getSelectionMod().observe(getViewLifecycleOwner(), isSelectionMod ->{
                setUpToolbar(isSelectionMod);
                mCountersAdapter.getSelectedCountersCount().observe(getViewLifecycleOwner(), count -> {
                    mToolbar.setTitle("Выбрано: " + count);
                    if (count==0)
                        mToolbar.setTitle(currentItem);
                    mToolbar.getMenu().getItem(0).setVisible(count <= 1);
                });
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mAccessibility = new Accessibility(requireContext());
        mCounters_rv.setAdapter(mCountersAdapter);
        /*set up listeners on buttons witch appears when selection mod is active*/
        new FastCountButton(mDecAllSelectedCounters_bt, ()-> {
            mCountersAdapter.decSelectedCounters();
            mAccessibility.playDecSoundEffect();
            mAccessibility.playDecVibrationEffect(getView());
        });
        new FastCountButton(mIncAllSelectedCounters_bt, ()-> {
            mCountersAdapter.incSelectedCounters();
            mAccessibility.playIncSoundEffect();
            mAccessibility.playIncVibrationEffect(getView());
        });
    }

    private void setUpToolbar(boolean isSelectionMod) {
        if (isSelectionMod){
            mDecAllSelectedCounters_bt.setVisibility(View.VISIBLE);
            mIncAllSelectedCounters_bt.setVisibility(View.VISIBLE);
            mToolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close, null));
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_selection_mod);

            mToolbar.setNavigationOnClickListener(v -> {
                mCountersAdapter.clearSelectedCounters();
            });

            mToolbar.setOnMenuItemClickListener(menuItem->{
                switch (menuItem.getItemId()){
                    case R.id.editSelected:
                       Counter counter = mCountersAdapter.getSelectedCounter();
                        Navigation.findNavController(getView()).navigate(CountersFragmentDirections.
                                actionCountersFragmentToCreateEditCounterFragment().setCounterId(counter.id));
                        break;
                    case R.id.selectAllCounter:
                        mCountersAdapter.selectAllCounters();
                        break;
                    case R.id.resetSelected:
                        mCountersAdapter.resetSelectedCounters();
                        Snackbar.make(requireView(), "Counters reseated", BaseTransientBottomBar.LENGTH_LONG)
                                .setAction("UNDO", v1 -> {
                                    mCountersAdapter.undoReset();
                                }).show();
                        break;
                    case R.id.deleteSelected:
                        new DeleteCounterDialog(()->{
                            mCountersAdapter.deleteSelectedCounters();
                        }, mCountersAdapter.getSelectedCountersCount().getValue())
                                .show(getChildFragmentManager(), "DialogCounterDelete");
                        break;
                }
                return true;
            });

        }else {
            mDecAllSelectedCounters_bt.setVisibility(View.GONE);
            mIncAllSelectedCounters_bt.setVisibility(View.GONE);
            mToolbar.setNavigationIcon(mNavigationIcon);
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_counter_main_activity);
            mToolbar.setTitle(currentItem);

            mToolbar.setOnMenuItemClickListener(i->{
                if (i.getItemId() == R.id.counterAdd) {
                    new CreateCounterDialog().show(getParentFragmentManager(), "Add Counter");
                }
                return true;
            });

            mToolbar.setNavigationOnClickListener(v -> {
                mDrawer.open();
            });
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

}

