package com.yaroslavgorbach.counter.Activityes;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.yaroslavgorbach.counter.Fragments.CountersFragment;
import com.yaroslavgorbach.counter.Fragments.CountersFragmentDirections;
import com.yaroslavgorbach.counter.Fragments.Dialogs.CreateCounterDialog;
import com.yaroslavgorbach.counter.R;
import com.yaroslavgorbach.counter.RecyclerViews.GroupList_rv;
import com.yaroslavgorbach.counter.Utility;
import com.yaroslavgorbach.counter.ViewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {

    private GroupList_rv mGroupsList;
    private MainActivityViewModel mViewModel;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_navigationItem;
    private NavigationView mNavigationDrawerView;
    private NavController mNavController;
    private MaterialToolbar mMaterialToolbar;
    private String mTittle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*initialize fields*/
        mAllCounters_navigationItem = findViewById(R.id.AllCounters);
        mNavigationDrawerView = findViewById(R.id.navigationDrawerView);
        mDrawer = findViewById(R.id.drawer);
        mMaterialToolbar = findViewById(R.id.toolbar_mainActivity);
        mMaterialToolbar.setTitle(getResources().getString(R.string.AllCountersItem));
        mNavController = Navigation.findNavController(this, R.id.hostFragment);
        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        setUpNavControllers();


        /*setting the fragment with all the counters*/
        mAllCounters_navigationItem.setOnClickListener(i->{
            NavDirections action = CountersFragmentDirections.actionCountersFragmentSelf();
            mNavController.navigate(action);
            mDrawer.closeDrawer(GravityCompat.START);
            mMaterialToolbar.setTitle(getResources().getString(R.string.AllCountersItem));
        });

        /*initialize RecyclerView and it listener for groups*/
        mGroupsList = new GroupList_rv(findViewById(R.id.groupsList_rv), new GroupList_rv.Listener() {

            /*setting the fragment with all the counters which belong to a certain group*/
            @Override
            public void onOpen(String string) {
                NavDirections action = CountersFragmentDirections.actionCountersFragmentSelf().setGroup(string);
                mNavController.navigate(action);
                mDrawer.closeDrawer(GravityCompat.START);
                mMaterialToolbar.setTitle(string);
                mTittle = string;
            }
        });

        mViewModel.getGroups().observe(this, groups -> {
            mGroupsList.setGroups(Utility.deleteTheSameGroups(groups));
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setUpNavControllers() {
        AppBarConfiguration appBarConfiguration;
        mMaterialToolbar.inflateMenu(R.menu.menu_counter_main_activity);
        mMaterialToolbar.setOnMenuItemClickListener(i->{
            if (i.getItemId() == R.id.counterAdd) {
                new CreateCounterDialog().show(getSupportFragmentManager(), "Add Counter");
            }
            return true;
        });


        appBarConfiguration = new AppBarConfiguration.Builder(mNavController.getGraph())
                .setDrawerLayout(mDrawer)
                .build();
        NavigationUI.setupWithNavController(mMaterialToolbar, mNavController, appBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationDrawerView, mNavController);


        mNavController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            switch (destination.getId()) {
                case R.id.counterFragment:
                    mMaterialToolbar.setVisibility(View.GONE);
                    break;
                case R.id.createEditCounterFragment:
                    mMaterialToolbar.setVisibility(View.GONE);
                    break;
                case R.id.countersFragment:
                    mMaterialToolbar.setVisibility(View.VISIBLE);
                    if (mTittle==null){
                        mTittle = getResources().getString(R.string.AllCountersItem);
                    }
                    mMaterialToolbar.setTitle(mTittle);
                    break;
                case R.id.counterHistoryFragment:
                    mMaterialToolbar.setVisibility(View.GONE);
                    break;
            }
        });
    }

}
