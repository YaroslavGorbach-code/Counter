package com.yaroslavgorbachh.counter.Activityes;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.Database.ViewModels.CounterViewModel;
import com.yaroslavgorbachh.counter.Fragments.CountersFragment;
import com.yaroslavgorbachh.counter.Fragments.CreateCounterDialog;
import com.yaroslavgorbachh.counter.Models.Counter;
import com.yaroslavgorbachh.counter.RecyclerViews.GroupList_rv;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements CreateCounterDialog.AddCounterListener {

    private GroupList_rv mGroupsList;
    private CounterViewModel mCounterViewModel;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    private LinearLayout mAllCounters_navigationItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*initialize fields*/
        mToolbar = findViewById(R.id.toolbar_mainActivity);
        mDrawer = findViewById(R.id.drawer);
        mAllCounters_navigationItem = findViewById(R.id.AllCounters);
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        /*inflating menu and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_main_activity);
        mToolbar.setOnMenuItemClickListener(i->{

            if (i.getItemId() == R.id.counterAdd) {
                new CreateCounterDialog().show(getSupportFragmentManager(), "Add Counter");
            }
            return true;

        });

        /*adding toggle*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        /*setting the fragment with all the counters at the first time*/
        CountersFragment fragment = new CountersFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_list_counters, fragment).commit();

        /*setting the fragment with all the counters*/
        mAllCounters_navigationItem.setOnClickListener(i->{
            getSupportFragmentManager().beginTransaction().replace(R.id.container_list_counters, fragment).commit();
            mToolbar.setTitle(R.string.AllCountersItem);
            mDrawer.closeDrawer(GravityCompat.START);
        });

        /*initialize RecyclerView and it listener for groups*/
        mGroupsList = new GroupList_rv(findViewById(R.id.groupsList_rv), new GroupList_rv.Listener() {

            /*setting the fragment with all the counters which belong to a certain group*/
            @Override
            public void onOpen(String string) {
                Bundle arg = new Bundle();
                arg.putString("group_title", string);
                CountersFragment fragment = new CountersFragment();
                fragment.setArguments(arg);
                getSupportFragmentManager().beginTransaction().replace(R.id.container_list_counters, fragment).commit();
                mToolbar.setTitle(string);
                mDrawer.closeDrawer(GravityCompat.START);
            }

        });

        mCounterViewModel.getGroups().observe(this, strings -> {
            /*delete the same groups*/
             Set<String> set = new HashSet<>(strings);
             String[] result = set.toArray(new String[0]);
             mGroupsList.setGroups(Arrays.asList(result));
        });
    }

    /*create new counter*/
    @Override
    public void onAddClick(String title, String group) {

        Date currentDate = new Date();
        currentDate.getTime();
//        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss", Locale.getDefault());
//        String date = dateFormat.format(currentDate);

        Counter counter = new Counter(title, 0, Long.parseLong("9999999999999999"),
                Long.parseLong("-9999999999999999"), 1, group, currentDate);
        mCounterViewModel.insert(counter);

    }

    /*launch exact creation counter*/
    @Override
    public void onLaunchDetailedClick() {

        startActivity(new Intent(MainActivity.this, CreateEditCounterActivity.class ));

    }
}
