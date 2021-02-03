package com.yaroslavgorbach.counter.Fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbach.counter.FastCountButton;
import com.yaroslavgorbach.counter.Fragments.Dialogs.CreateCounterDialog;
import com.yaroslavgorbach.counter.RecyclerViews.Adapters.CountersAdapter;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.GroupList_rv;
import com.yaroslavgorbach.counter.Utility;
import com.yaroslavgorbach.counter.ViewModels.CountersViewModel;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersFragment extends Fragment {
    private CountersViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private CountersAdapter mAdapter;
    private String mGroup;
    private MaterialToolbar mToolbar;
    private Drawable mNavigationIcon;
    private GroupList_rv mGroupsList;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_navigationItem;
    private NavigationView mNavigationDrawerView;
    private NavController mNavController;

    private TextView mIncAllSelectedCounters_bt;
    private TextView mDecAllSelectedCounters_bt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*callback for callback for handling back press button*/
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mAdapter.selectionMod.getValue()){
                    mAdapter.clearSelectedCounters();
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
        View view = inflater.inflate(R.layout.list_of_counters_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(CountersViewModel.class);
        mGroup = CountersFragmentArgs.fromBundle(requireArguments()).getGroup();
        mDecAllSelectedCounters_bt = view.findViewById(R.id.allSelectedDec);
        mIncAllSelectedCounters_bt = view.findViewById(R.id.allSelectedInc);
        mAllCounters_navigationItem = view.findViewById(R.id.AllCounters);
        mNavigationDrawerView = view.findViewById(R.id.navigationDrawerView);
        mToolbar = view.findViewById(R.id.toolbar_mainActivity);
        mDrawer = view.findViewById(R.id.drawer);
        mRecyclerView = view.findViewById(R.id.countersList_rv);
        mToolbar.setTitle(getResources().getString(R.string.AllCountersItem));


        /*navController set up*/
        mNavController = Navigation.findNavController(requireActivity(), R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setDrawerLayout(mDrawer)
                .build();
        NavigationUI.setupWithNavController(mToolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationDrawerView, mNavController);
        mNavigationIcon = mToolbar.getNavigationIcon();



        /*set up the fragment with all the counters*/
        mAllCounters_navigationItem.setOnClickListener(i->{
            NavDirections action = CountersFragmentDirections.actionCountersFragmentSelf();
            mNavController.navigate(action);
            mDrawer.closeDrawer(GravityCompat.START);
            mToolbar.setTitle(getResources().getString(R.string.AllCountersItem));
        });

        /*initialize RecyclerView and listener for groups*/
        mGroupsList = new GroupList_rv(view.findViewById(R.id.groupsList_rv), new GroupList_rv.Listener() {

            /*set up the fragment with all the counters which belong to a certain group*/
            @Override
            public void onOpen(String string) {
                NavDirections action = CountersFragmentDirections.actionCountersFragmentSelf().setGroup(string);
                mNavController.navigate(action);
//                mDrawer.closeDrawer(GravityCompat.START);
//                mToolbar.setTitle(string);
//                mTittle = string;
            }
        });

        mViewModel.getGroups().observe(getViewLifecycleOwner(), groups -> {
            mGroupsList.setGroups(Utility.deleteTheSameGroups(groups));
        });

        /*set up rv with counters*/
        mAdapter = new CountersAdapter(new CountersAdapter.CounterItemListeners() {
            @Override
            public void onPlusClick(Counter counter) {
                mViewModel.incCounter(counter);
            }

            @Override
            public void onMinusClick(Counter counter) {
                mViewModel.decCounter(counter);
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

        if (mGroup != null && !mGroup.equals("null")) {
            mViewModel.getCountersByGroup(mGroup).observe(getViewLifecycleOwner(), counters -> {
                mAdapter.setData(counters);
                mToolbar.setTitle(mGroup);
            });
        } else {
            mViewModel.mCounters.observe(getViewLifecycleOwner(), counters -> {
                mAdapter.setData(counters);
            });
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter.itemTouchHelper.attachToRecyclerView(mRecyclerView);
        mAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*set up listeners on buttons witch appears when selection mod is active*/
        new FastCountButton(mDecAllSelectedCounters_bt, ()-> mAdapter.decSelectedCounters());
        new FastCountButton(mIncAllSelectedCounters_bt, ()-> mAdapter.incSelectedCounters());

        /*set up listeners for selection mod*/
        mAdapter.selectionMod.observe(getViewLifecycleOwner(), isSelectionMod ->{
            if (isSelectionMod){
                mDecAllSelectedCounters_bt.setVisibility(View.VISIBLE);
                mIncAllSelectedCounters_bt.setVisibility(View.VISIBLE);
                mToolbar.setNavigationIcon(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_close, null));
                mToolbar.getMenu().clear();
                mToolbar.inflateMenu(R.menu.menu_selection_mod);

                mToolbar.setNavigationOnClickListener(v -> {
                    mAdapter.clearSelectedCounters();
                });

                mToolbar.setOnMenuItemClickListener(menuItem->{

                    switch (menuItem.getItemId()){
                        case R.id.selectAllCounter:
                            mAdapter.selectAllCounters();
                            break;
                        case R.id.resetSelected:
                            mAdapter.resetSelectedCounters();
                            Snackbar.make(view, "Counters reseated", BaseTransientBottomBar.LENGTH_LONG)
                                    .setAction("UNDO", v1 -> {
                                        mAdapter.undoReset();
                                    }).show();
                            break;
                        case R.id.deleteSelected:

                    }

                    return true;
                });
            }else {
                mDecAllSelectedCounters_bt.setVisibility(View.GONE);
                mIncAllSelectedCounters_bt.setVisibility(View.GONE);
                mToolbar.setNavigationIcon(mNavigationIcon);
                mToolbar.getMenu().clear();
                mToolbar.inflateMenu(R.menu.menu_counter_main_activity);

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
        });

    }
}

