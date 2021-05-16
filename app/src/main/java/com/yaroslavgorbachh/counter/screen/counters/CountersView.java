package com.yaroslavgorbachh.counter.screen.counters;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.screen.counters.DragAndDrop.MultiSelection.CounterMultiSelection;
import com.yaroslavgorbachh.counter.screen.counters.DragAndDrop.MultiSelection.MultiCount;
import com.yaroslavgorbachh.counter.screen.counters.DragAndDrop.MultiSelection.MultiSelection;
import com.yaroslavgorbachh.counter.screen.counters.navigationDrawer.CounterDrawerMenuItemSelector;
import com.yaroslavgorbachh.counter.screen.counters.navigationDrawer.DrawerItemSelector;
import com.yaroslavgorbachh.counter.utill.Utility;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersView {
    private final Drawable mNavigationIcon;
    private final DrawerItemSelector drawerItemSelector;
    private final FragmentCountersBinding mBinding;
    private MultiCount mCounterMultiCount;
    private GroupsAdapter mGroupsAdapter;
    private CountersAdapter mCountersAdapter;
    public interface Callback{
        void onSettings();
        void onNoCounters();
        void onInc(Counter counter);
        void onDec(Counter counter);
        void onOpen(Counter counter);
        void onMoved(Counter counterFrom, Counter counterTo);
    }
    CountersView(FragmentCountersBinding binding, Activity activity, Callback callback){
        drawerItemSelector = new CounterDrawerMenuItemSelector();
        mCounterMultiCount = new CounterMultiSelection(null, activity, null);

        mBinding = binding;
        /*navController set up*/
        NavController mNavController = Navigation.findNavController(activity, R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setOpenableLayout(binding.openableLayout)
                .build();
        NavigationUI.setupWithNavController(binding.toolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.drawer.navigationDrawer, mNavController);
        mNavigationIcon = binding.toolbar.getNavigationIcon();

        /*when click set up the adapter with all the counters*/
        binding.drawer.allCounters.setOnClickListener(i -> {
            //mGroupsAdapter.allCountersItemSelected(mAllCounters_drawerItem);
            binding.openableLayout.close();
        });

        binding.drawer.settings.setOnClickListener(i -> callback.onSettings());
        binding.noCounters.setOnClickListener(v -> callback.onNoCounters());

        mGroupsAdapter = new GroupsAdapter(drawerItemSelector);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        binding.drawer.groupsList.setLayoutManager(mLayoutManager);
        binding.drawer.groupsList.setAdapter(mGroupsAdapter);
        binding.drawer.groupsList.setHasFixedSize(true);

        /*set up rv with counters*/
         mCountersAdapter = new CountersAdapter(new CountersAdapter.CounterItemClickListener() {
            @Override
            public void onPlusClick(Counter counter) {
                callback.onInc(counter);
            }

            @Override
            public void onMinusClick(Counter counter) {
                callback.onDec(counter);
            }

            @Override
            public void onOpen(Counter counter) {
                callback.onOpen(counter);
            }

            @Override
            public void onMoved(Counter counterFrom, Counter counterTo) {
                callback.onMoved(counterFrom, counterTo);
            }
        }, null, null);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mBinding.getRoot().getContext());
        mBinding.list.setLayoutManager(layoutManager);
        mBinding.list.setHasFixedSize(true);
        mBinding.list.setAdapter(mCountersAdapter);
        mCountersAdapter.itemTouchHelper.attachToRecyclerView(mBinding.list);
        mCountersAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);
    }

    public void setGroups(List<String> groups) {

        if (groups.size() > 0) {
            mBinding.drawer.groupsList.setVisibility(View.VISIBLE);
            mBinding.drawer.noGroups.setVisibility(View.GONE);
        } else {
            if (!mCounterMultiCount.getSelectionModState().getValue()) {
                mBinding.drawer.groupsList.setVisibility(View.GONE);
                mBinding.drawer.noGroups.setVisibility(View.VISIBLE);
            }
        }
        mGroupsAdapter.setGroups(Utility.deleteTheSameGroups(groups));
    }

    public void setCounters(List<Counter> data) {
        mCountersAdapter.setData(data);
    }


    public MultiSelection getMultiSelection() {
        return mCounterMultiCount;
    }
}
