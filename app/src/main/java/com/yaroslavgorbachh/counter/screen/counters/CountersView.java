package com.yaroslavgorbachh.counter.screen.counters;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Explode;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.transition.MaterialFade;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver;
import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.databinding.FragmentCountersBinding;
import com.yaroslavgorbachh.counter.feature.FastCountButton;
import com.yaroslavgorbachh.counter.feature.ad.AdManager;
import com.yaroslavgorbachh.counter.screen.counters.drawer.GroupsAdapter;
import com.yaroslavgorbachh.counter.screen.counters.multyselection.CounterMultiSelection;
import com.yaroslavgorbachh.counter.util.CommonUtil;

import java.util.List;

import jp.wasabeef.recyclerview.animators.ScaleInBottomAnimator;

import static androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY;
import static com.yaroslavgorbachh.counter.VolumeButtonBroadcastReceiver.ON_KEY_DOWN_BROADCAST;

public class CountersView {


    public interface Callback {
        void onSettings();
        void onInc(Counter counter);
        void onDec(Counter counter);
        void onOpen(long counterId);
        void onMoved(Counter counterFrom, Counter counterTo);
        void onEdit(Counter counter);
        void onReset(List<Counter> counters);
        void onExport(Intent intent);
        void onRemove(List<Counter> counters);
        void onShowCreateDialog();
        void onDecSelected(List<Counter> selected);
        void onIncSelected(List<Counter> selected);
        void onGroupItemSelected(String group);
        void onAllCountersItemSelected();
        void onLoverVolume();
        void onRaiseVolume();
        void onRemoveAd();
    }

    private final FragmentCountersBinding mBinding;
    private final GroupsAdapter mGroupsAdapter;
    private final CountersAdapter mCountersAdapter;
    private final Drawable mNavigationIcon;
    private final VolumeButtonBroadcastReceiver mMessageReceiver;
    private final Callback mCallback;
    private String mGroupTitle;

    CountersView(FragmentCountersBinding binding,
                 int fastCountInterval,
                 FragmentActivity activity,
                 LifecycleOwner lifecycleOwner,
                 AdManager adManager,
                 Callback callback) {
        mBinding = binding;
        mGroupTitle = binding.getRoot().getContext().getString(R.string.allCountersItem);
        mCallback = callback;
        OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mCountersAdapter.getSelected().size() > 0) {
                    mCountersAdapter.unselectAll();
                } else {
                    activity.finish();
                }
            }
        };
        activity.getOnBackPressedDispatcher().addCallback(lifecycleOwner, backPressedCallback);
        adManager.showBanner(activity, mBinding.bannerContainer);

        mMessageReceiver = new VolumeButtonBroadcastReceiver(new VolumeButtonBroadcastReceiver.VolumeKeyDownResponse() {
            @Override
            public void decCounters() {
                callback.onDecSelected(mCountersAdapter.getSelected());
            }

            @Override
            public void incCounters() {
                callback.onIncSelected(mCountersAdapter.getSelected());
            }

            @Override
            public void lowerVolume() {
                callback.onLoverVolume();
            }

            @Override
            public void raiseVolume() {
                callback.onRaiseVolume();
            }
        });
        /*Register to receive messages.*/
        LocalBroadcastManager.getInstance(binding.getRoot().getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(ON_KEY_DOWN_BROADCAST));

        NavController mNavController = Navigation.findNavController(activity, R.id.hostFragment);
        AppBarConfiguration appBarConfiguration;
        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setOpenableLayout(binding.openableLayout)
                .build();
        NavigationUI.setupWithNavController(binding.toolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.drawer.navigationDrawer, mNavController);
        mNavigationIcon = binding.toolbar.getNavigationIcon();

        binding.drawer.settings.setOnClickListener(i -> callback.onSettings());
        binding.drawer.removeAd.setOnClickListener(i -> callback.onRemoveAd());

        binding.noCountersLayout.iconNoCounters.setOnClickListener(v -> callback.onShowCreateDialog());

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

        mCountersAdapter = new CountersAdapter(new CounterMultiSelection(), fastCountInterval, new CountersAdapter.Callback() {
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
                callback.onOpen(counter.id);
            }

            @Override
            public void onMoved(Counter counterFrom, Counter counterTo) {
                callback.onMoved(counterFrom, counterTo);
            }

            @Override
            public void onMultiSelectionStateChange(boolean isActive) {
                mMessageReceiver.setSelectionMod(isActive);
                if (isActive) {
                    showButtons(true);
                    mBinding.toolbar.setNavigationIcon(ResourcesCompat.getDrawable(
                            binding.getRoot().getContext().getResources(), R.drawable.ic_close, activity.getTheme()));
                    binding.toolbar.getMenu().clear();
                    binding.toolbar.inflateMenu(R.menu.menu_selection_mod);

                } else {
                    showButtons(false);
                    mBinding.toolbar.setNavigationIcon(mNavigationIcon);
                    binding.toolbar.getMenu().clear();
                    binding.toolbar.inflateMenu(R.menu.menu_counters);
                }
            }

            @Override
            public void onSelect(int count) {
                if (mCountersAdapter != null && mCountersAdapter.getSelected().size() > 0) {
                    binding.toolbar.setTitle(String.valueOf(mCountersAdapter.getSelected().size()));
                    mBinding.toolbar.getMenu().getItem(0).setVisible(mCountersAdapter.getSelected().size() <= 1);
                } else {
                    binding.toolbar.setTitle(mGroupTitle);
                }
            }
        }, activity);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mBinding.getRoot().getContext());
        mBinding.list.setLayoutManager(layoutManager);
        mBinding.list.setHasFixedSize(true);
        mBinding.list.setAdapter(mCountersAdapter);
        binding.list.setItemAnimator(new ScaleInBottomAnimator());
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
                callback.onExport(CommonUtil.getExportCSVIntent(mCountersAdapter.getSelected()));
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
            if (mCountersAdapter.getSelected().size() > 0) {
                mCountersAdapter.unselectAll();
            } else {
                mBinding.openableLayout.open();
            }
        });
        new FastCountButton(binding.decSelected, () ->
                callback.onDecSelected(mCountersAdapter.getSelected()), fastCountInterval);

        new FastCountButton(binding.incSelected, () ->
                callback.onIncSelected(mCountersAdapter.getSelected()), fastCountInterval);
    }

    public void setGroups(List<String> groups) {
        if (groups.size() > 0) {
            mBinding.drawer.groupsList.setVisibility(View.VISIBLE);
            mBinding.drawer.noGroups.setVisibility(View.GONE);
        }
        mGroupsAdapter.setGroups(groups);
    }

    public void setCounters(List<Counter> data) {
        showNoCountersIcon(data.isEmpty());
        mCountersAdapter.setData(data);
    }

    public void setGroup(String currentGroup) {
        if (currentGroup == null) {
            mGroupTitle = mBinding.getRoot().getContext().getString(R.string.allCountersItem);
            mBinding.toolbar.setTitle(mGroupTitle);
        } else {
            mBinding.toolbar.setTitle(currentGroup);
            mGroupTitle = currentGroup;
            mGroupsAdapter.select(currentGroup);
            mBinding.drawer.allCounters.setBackgroundResource(R.drawable.i_group);
        }
    }

    public void unregisterReceiver(Context context) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(mMessageReceiver);
    }

    private void showButtons(boolean show) {
        Transition transition = new Explode();
        transition.setDuration(400);
        transition.addTarget(R.id.inc_selected);
        transition.addTarget(R.id.dec_selected);

        TransitionManager.beginDelayedTransition(mBinding.getRoot(), transition);
        if (show) {
            mBinding.incSelected.setVisibility(View.VISIBLE);
            mBinding.decSelected.setVisibility(View.VISIBLE);
        } else {
            mBinding.incSelected.setVisibility(View.GONE);
            mBinding.decSelected.setVisibility(View.GONE);
        }

    }

    public void setCounterWidgetId(long id) {
        if(id!=0){
            mCallback.onOpen(id);
        }
    }

    public void sesRemoveAdVisibility(boolean adIsAllow) {
        if (!adIsAllow)
        mBinding.drawer.removeAd.setVisibility(View.GONE);
    }


    private void showNoCountersIcon(boolean show) {
        Transition transition = new MaterialFade();
        transition.setDuration(400);
        transition.addTarget(R.id.no_counters);

        TransitionManager.beginDelayedTransition(mBinding.getRoot(), transition);
        if (show) {
            mBinding.noCountersLayout.noCounters.setVisibility(View.VISIBLE);
        } else {
            mBinding.noCountersLayout.noCounters.setVisibility(View.GONE);
        }
    }

}
