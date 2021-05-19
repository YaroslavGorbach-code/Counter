package com.yaroslavgorbachh.counter.screen.counters;

import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.feature.Animations;
import com.yaroslavgorbachh.counter.feature.multyselection.CounterMultiSelection;
import com.yaroslavgorbachh.counter.screen.counters.drawer.GroupsAdapter;
import com.yaroslavgorbachh.counter.utill.Utility;

import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;

public class CountersView {

    public interface Callback {
        void onSettings();
        void onInc(Counter counter);
        void onDec(Counter counter);
        void onOpen(Counter counter);
        void onMoved(Counter counterFrom, Counter counterTo);
        void onEdit(Counter counter);
        void onReset(List<Counter> counters);
        void onExport(List<Counter> counters);
        void onRemove(List<Counter> counters);
        void onShowCreateDialog();
        void onDecSelected(List<Counter> selected);
        void onIncSelected(List<Counter> selected);
        void onGroupItemSelected(String group);
        void onAllCountersItemSelected();
    }

    private final FragmentCountersBinding mBinding;
    private final GroupsAdapter mGroupsAdapter;
    private final CountersAdapter mCountersAdapter;
    private final Drawable mNavigationIcon;
    private String mGroupTitle;

    CountersView(FragmentCountersBinding binding, FragmentActivity activity, LifecycleOwner lifecycleOwner, Callback callback) {
        mBinding = binding;
        mGroupTitle = binding.getRoot().getContext().getString(R.string.allCountersItem);
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mCountersAdapter.getSelected().size()>0) {
                    mCountersAdapter.unselectAll();
                } else {
                    activity.finish();
                }
            }
        };

        activity.getOnBackPressedDispatcher().addCallback(lifecycleOwner, backPressedCallback);
        NavController mNavController = Navigation.findNavController(activity, R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setOpenableLayout(binding.openableLayout)
                .build();
        NavigationUI.setupWithNavController(binding.toolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.drawer.navigationDrawer, mNavController);
        mNavigationIcon = binding.toolbar.getNavigationIcon();

        binding.drawer.settings.setOnClickListener(i -> callback.onSettings());
        binding.noCounters.setOnClickListener(v -> callback.onShowCreateDialog());

        mGroupsAdapter = new GroupsAdapter(group -> {
            callback.onGroupItemSelected(group);
            mBinding.openableLayout.close();
            binding.drawer.allCounters.setBackgroundResource(R.drawable.i_group);
        });

        mBinding.drawer.allCounters.setOnClickListener(v -> {
            callback.onAllCountersItemSelected();
            mBinding.openableLayout.close();
            binding.drawer.allCounters.setBackgroundResource(R.drawable.i_group_selected_bg);
            mGroupsAdapter.unselect();
            mGroupTitle = mBinding.getRoot().getContext().getString(R.string.allCountersItem);
            mBinding.toolbar.setTitle(mGroupTitle);
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(binding.getRoot().getContext());
        binding.drawer.groupsList.setLayoutManager(mLayoutManager);
        binding.drawer.groupsList.setAdapter(mGroupsAdapter);
        binding.drawer.groupsList.setHasFixedSize(true);

        mCountersAdapter = new CountersAdapter(new CounterMultiSelection(), new CountersAdapter.Callback() {
            @Override
            public void onInc(Counter counter) {
                callback.onInc(counter);
            }

            @Override
            public void onDec(Counter counter) {
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

            @Override
            public void onMultiSelectionStateChange(boolean isActive) {
                if (isActive) {
                    Animations.showButtonsMultiSelection(binding.decSelected, binding.incSelected);
                    mBinding.toolbar.setNavigationIcon(ResourcesCompat.getDrawable(
                            binding.getRoot().getContext().getResources(), R.drawable.ic_close, activity.getTheme()));
                    binding.toolbar.getMenu().clear();
                    binding.toolbar.inflateMenu(R.menu.menu_selection_mod);

                } else {
                    Animations.hideButtonsMultiSelection(binding.decSelected, binding.incSelected);
                    mBinding.toolbar.setNavigationIcon(mNavigationIcon);
                    binding.toolbar.getMenu().clear();
                    binding.toolbar.inflateMenu(R.menu.menu_counters);
                }
            }

            @Override
            public void onSelect(int count) {
                if (mCountersAdapter!=null && mCountersAdapter.getSelected().size() > 0){
                    binding.toolbar.setTitle(String.valueOf(mCountersAdapter.getSelected().size()));
                }else{
                    binding.toolbar.setTitle(mGroupTitle);
                }
            }
        }, activity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mBinding.getRoot().getContext());
        mBinding.list.setLayoutManager(layoutManager);
        mBinding.list.setHasFixedSize(true);
        mBinding.list.setAdapter(mCountersAdapter);
        mCountersAdapter.itemTouchHelper.attachToRecyclerView(mBinding.list);
        mCountersAdapter.setStateRestorationPolicy(PREVENT_WHEN_EMPTY);

        mBinding.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.editSelected) {
                callback.onEdit(mCountersAdapter.getFirsSelected());
            }
            if (item.getItemId() == R.id.selectAllCounter) {
                mCountersAdapter.selectAllCounters();
            }
            if (item.getItemId() == R.id.resetSelected) {
                callback.onReset(mCountersAdapter.getSelected());
            }
            if (item.getItemId() == R.id.exportSelected) {
                callback.onExport(mCountersAdapter.getSelected());
            }
            if (item.getItemId() == R.id.deleteSelected) {
                String title;
                if (mCountersAdapter.getSelected().size() > 1) {
                    title = mBinding.getRoot().getContext().getString(R.string.deleteCountersDeleteDialog);
                } else {
                    title = mBinding.getRoot().getContext().getString(R.string.deleteCounterDeleteDialog);
                }
                new MaterialAlertDialogBuilder(mBinding.getRoot().getContext())
                        .setTitle(title)
                        .setMessage(R.string.deleteCounterDialogText)
                        .setPositiveButton(R.string.deleteCounterDialogPositiveButton, (dialog, which) -> {
                            callback.onRemove(mCountersAdapter.getSelected());
                            mCountersAdapter.unselectAll();
                        })
                        .setNegativeButton(R.string.deleteCounterDialogNegativeButton, null)
                        .show();
            }
            if (item.getItemId() == R.id.counterCreate) {
                callback.onShowCreateDialog();
            }
            return true;
        });

        mBinding.toolbar.setNavigationOnClickListener(v -> {
            if (mCountersAdapter.getSelected().size()>0) {
                mCountersAdapter.unselectAll();
            } else {
                mBinding.openableLayout.open();
            }
        });
        binding.decSelected.setOnClickListener(v -> callback.onDecSelected(mCountersAdapter.getSelected()));
        binding.incSelected.setOnClickListener(v -> callback.onIncSelected(mCountersAdapter.getSelected()));
    }

    public void setGroups(List<String> groups) {
        if (groups.size() > 0) {
            mBinding.drawer.groupsList.setVisibility(View.VISIBLE);
            mBinding.drawer.noGroups.setVisibility(View.GONE);
        }
        mGroupsAdapter.setGroups(groups);
    }

    public void setCounters(List<Counter> data) {
        mCountersAdapter.setData(data);
    }

    public void setGroup(String currentGroup) {
        if (currentGroup == null){
            mGroupTitle = mBinding.getRoot().getContext().getString(R.string.allCountersItem);
            mBinding.toolbar.setTitle(mGroupTitle);
        }else {
            mBinding.toolbar.setTitle(currentGroup);
            mGroupTitle = currentGroup;
            mGroupsAdapter.select(currentGroup);
            mBinding.drawer.allCounters.setBackgroundResource(R.drawable.i_group);
        }
    }
}
