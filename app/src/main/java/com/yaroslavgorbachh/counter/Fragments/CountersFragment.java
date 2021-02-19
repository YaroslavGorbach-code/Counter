package com.yaroslavgorbachh.counter.Fragments;

import android.content.BroadcastReceiver;
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
import android.widget.TextView;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.Activityes.SettingsActivity;
import com.yaroslavgorbachh.counter.FastCountButton;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.CreateCounterDialog;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.RecyclerViews.Adapters.CountersAdapter;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.RecyclerViews.Adapters.GroupsAdapter;
import com.yaroslavgorbachh.counter.Utility;
import com.yaroslavgorbachh.counter.ViewModels.CountersViewModel;
import com.yaroslavgorbachh.counter.Activityes.MainActivity;

import java.util.stream.Collectors;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersFragment extends Fragment  {
    private static final String CURRENT_GROUP = "CURRENT_GROUP";

    private CountersViewModel mViewModel;
    private RecyclerView mCounters_rv;
    private CountersAdapter mCountersAdapter;
    private Toolbar mToolbar;
    private Drawable mNavigationIcon;
    private GroupsAdapter mGroupsAdapter;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_drawerItem;
    private TextView mIncAllSelectedCounters_bt;
    private TextView mDecAllSelectedCounters_bt;
    private String currentItem;
    private Accessibility mAccessibility;
    private AudioManager mAudioManager;
    private BroadcastReceiver mMessageReceiver;
    private ConstraintLayout mIconAndTextThereAreNoCounters;
    private ConstraintLayout mThereAreNoGroupsTextAndIcon;


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
        mToolbar = view.findViewById(R.id.toolbar_mainActivity);
        mDrawer = view.findViewById(R.id.drawer);
        mCounters_rv = view.findViewById(R.id.counters_list);
        mIconAndTextThereAreNoCounters = view.findViewById(R.id.iconAndTextThereAreNoCounters);
        mThereAreNoGroupsTextAndIcon = view.findViewById(R.id.thereAreNoGroupsTextAndIcon);
        RecyclerView groups_rv = view.findViewById(R.id.groupsList_rv);
        mAudioManager = (AudioManager) requireContext().getSystemService(Context.AUDIO_SERVICE);
        NavigationView navigationDrawerView = view.findViewById(R.id.navigationDrawerView);

        /*navController set up*/
        NavController mNavController = Navigation.findNavController(requireActivity(), R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setDrawerLayout(mDrawer)
                .build();
        NavigationUI.setupWithNavController(mToolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(navigationDrawerView, mNavController);
        mNavigationIcon = mToolbar.getNavigationIcon();

        /*when click set up the adapter with all the counters*/
        mAllCounters_drawerItem.setOnClickListener(i->{
            mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
            new Handler().postDelayed(()-> mDrawer.closeDrawer(GravityCompat.START), 200);
        });

        view.findViewById(R.id.settings).setOnClickListener(i->{
            Intent startSettingsActivity = new Intent(getContext(), SettingsActivity.class);
            startActivity(startSettingsActivity);
        });

        view.findViewById(R.id.iconThereAreNoCounters).setOnClickListener(v -> {
            new CreateCounterDialog().show(getParentFragmentManager(), "Add Counter");
        });

        /*initialize RecyclerView and listener for groups*/
        mGroupsAdapter = new GroupsAdapter();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(requireContext());
        groups_rv.setLayoutManager(mLayoutManager);
        groups_rv.setAdapter(mGroupsAdapter);
        groups_rv.setHasFixedSize(true);

        mViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
            if (groups.size() > 0){
                groups_rv.setVisibility(View.VISIBLE);
                mThereAreNoGroupsTextAndIcon.setVisibility(View.GONE);
            }else {
                if (!mCountersAdapter.getSelectionMod().getValue()){
                    groups_rv.setVisibility(View.GONE);
                    mThereAreNoGroupsTextAndIcon.setVisibility(View.VISIBLE);
                }
            }
                mGroupsAdapter.setGroups(Utility.deleteTheSameGroups(groups));
        });

        /*set up rv with counters*/
        mCountersAdapter = new CountersAdapter(new CountersAdapter.CounterItemListeners() {
            @Override
            public void onPlusClick(Counter counter) {
                mViewModel.incCounter(counter);
                mAccessibility.playIncFeedback(getView(),String.valueOf(counter.value));
            }

            @Override
            public void onMinusClick(Counter counter) {
                mViewModel.decCounter(counter);
                mAccessibility.playDecFeedback(getView(),String.valueOf(counter.value));
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
                switch (intent.getIntExtra(MainActivity.KEYCODE_EXTRA,-1)){
                    case MainActivity.KEYCODE_VOLUME_DOWN:
                        if (mCountersAdapter.getSelectionMod().getValue()){
                            decSelectedCounters();
                        }else {
                            mAudioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                        }
                        break;
                    case MainActivity.KEYCODE_VOLUME_UP:
                        if (mCountersAdapter.getSelectionMod().getValue()){
                            incSelectedCounters();
                        }else {
                          mAudioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
                        }
                        break;
                }
            }
        };

        /*Register to receive messages.*/
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(MainActivity.ON_KEY_DOWN_BROADCAST));

        LinearLayoutManager layoutManager = new LinearLayoutManager(mCounters_rv.getContext());
        mCounters_rv.setLayoutManager(layoutManager);
        mCounters_rv.setHasFixedSize(true);
        mCounters_rv.getItemAnimator().setAddDuration(0);
        mCounters_rv.getItemAnimator().setChangeDuration(0);
        mCounters_rv.getItemAnimator().setRemoveDuration(0);
        mCountersAdapter.itemTouchHelper.attachToRecyclerView(mCounters_rv);
        mCountersAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);

        if (currentItem != null && !currentItem.equals(getResources().getString(R.string.allCountersItem))) {
            mGroupsAdapter.restoreSelectedItem(currentItem);
        } else {
            /*set up all counters in the adapter when first open*/
            mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
        }

        /*filter the list depending on the selected group */
        mGroupsAdapter.getSelectedItem().observe(getViewLifecycleOwner(), selectedItem -> {
            mViewModel.mCounters.removeObservers(getViewLifecycleOwner());
            mViewModel.mCounters.observe(getViewLifecycleOwner(), counters -> {
                        if (!selectedItem.equals(getResources().getString(R.string.allCountersItem))){
                            mCountersAdapter.setData(counters.stream()
                                    .filter(counter -> counter.grope!=null && counter.grope.equals(selectedItem)).collect(Collectors.toList()));

                            /*when all counters of particular group was deleted */
                            if (mCountersAdapter.getItemCount()==0)
                                mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);

                        }else {
                            mCountersAdapter.setData(counters);
                        }

                        if (counters.size()<=0){
                            mIconAndTextThereAreNoCounters.setVisibility(View.VISIBLE);
                        }else {
                            mIconAndTextThereAreNoCounters.setVisibility(View.GONE);
                        }
                });
            currentItem = selectedItem;
            mToolbar.setTitle(currentItem);
            mCountersAdapter.clearSelectedCounters();
            new Handler().postDelayed(()-> mDrawer.closeDrawer(GravityCompat.START), 200);

        });
        mCounters_rv.setAdapter(mCountersAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*set up listeners for selection mod*/
        mCountersAdapter.getSelectionMod().observe(getViewLifecycleOwner(), isSelectionMod ->{
                setUpToolbarConfiguration(isSelectionMod);
                mCountersAdapter.getSelectedCountersCount().observe(getViewLifecycleOwner(), count -> {
                    mToolbar.setTitle(getString(R.string.selectionModTitle, String.valueOf(count)));
                    if (count==0)
                        mToolbar.setTitle(currentItem);
                    mToolbar.getMenu().getItem(0).setVisible(count <= 1);
                });
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        /*we initialise all this methods in on start because their behavior can change depends on pref*/
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if (sharedPreferences.getBoolean("leftHandMod", false ) && !mCountersAdapter.leftHandMod){
            getActivity().recreate();
        }
        if (!sharedPreferences.getBoolean("leftHandMod", false ) && mCountersAdapter.leftHandMod){
            getActivity().recreate();
        }

        if (sharedPreferences.getBoolean("lockOrientation", true ) ){
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }else {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }

        if (sharedPreferences.getBoolean("leftHandMod", false )){
            mDecAllSelectedCounters_bt.setText("+");
            mIncAllSelectedCounters_bt.setText("−");
            new FastCountButton(mDecAllSelectedCounters_bt, this::incSelectedCounters);
            new FastCountButton(mIncAllSelectedCounters_bt, this::decSelectedCounters);
        }else {
            mIncAllSelectedCounters_bt.setText("+");
            mDecAllSelectedCounters_bt.setText("−");
            new FastCountButton(mDecAllSelectedCounters_bt, this::decSelectedCounters);
            new FastCountButton(mIncAllSelectedCounters_bt, this::incSelectedCounters);
        }
        mAccessibility = new Accessibility(requireContext());
    }

    private void incSelectedCounters() {
        mCountersAdapter.incSelectedCounters();
        mAccessibility.playIncFeedback(getView(), null);
    }

    private void decSelectedCounters() {
        mCountersAdapter.decSelectedCounters();
        mAccessibility.playDecFeedback(getView(), null);
    }

    /*set up toolbar configuration depending on selection mod*/
    private void setUpToolbarConfiguration(boolean isSelectionMod) {
        if (isSelectionMod){
            mDecAllSelectedCounters_bt.setVisibility(View.VISIBLE);
            mIncAllSelectedCounters_bt.setVisibility(View.VISIBLE);
            mToolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close, getActivity().getTheme()));
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
                        Snackbar.make(requireView(), getResources().getString(R.string
                        .counterReset), BaseTransientBottomBar.LENGTH_LONG)
                                .setAction(getResources().getString(R.string.counterResetUndo), v1 -> {
                                    mCountersAdapter.undoReset();
                                }).show();
                        break;
                    case R.id.exportSelected:{
                        startActivity(Utility.getShareCountersInCSVIntent(mCountersAdapter.getSelectedCounters()));
                        break;
                    }
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
            mToolbar.inflateMenu(R.menu.menu_counter_main_fragment);
            mToolbar.setTitle(currentItem);

            mToolbar.setOnMenuItemClickListener(i->{
                if (i.getItemId() == R.id.counterAdd) {
                    if (currentItem.equals(getResources().getString(R.string.allCountersItem))){
                        CreateCounterDialog.newInstance(null).show(getParentFragmentManager(),"addCounter");
                    }else {
                        CreateCounterDialog.newInstance(currentItem).show(getParentFragmentManager(),"addCounter");
                    }
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

