package com.yaroslavgorbach.counter;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.yaroslavgorbach.counter.CounterList_rv.Listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements CreateCounterDialog.AddCounterListener {

    private CounterList_rv mCountersList;
    private GroupList_rv mGroupsList;
    private CounterViewModel mCounterViewModel;

    private Toolbar mToolbar;
    private DrawerLayout mDrawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*initialize fields*/
        mToolbar = findViewById(R.id.toolbar_mainActivity);
        mDrawer = findViewById(R.id.drawer);
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        /*inflating menu and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_main_activity);
        mToolbar.setOnMenuItemClickListener(i->{

            switch (i.getItemId()){

                case R.id.counterAdd:

                    new CreateCounterDialog().show(getSupportFragmentManager(), "Add Counter");

                    break;
            }

            return true;

        });

        /*adding toggle*/
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, mToolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        /*initialize RecyclerView and it listener*/
        mCountersList = new CounterList_rv(findViewById(R.id.countersList_rv), new Listener() {

            /*counter +*/
            @Override
            public void onPlusClick(Counter counter) {

                int value = counter.value;
                value++;
                mCounterViewModel.setValue(counter, value);

            }

            /*counter -*/
            @Override
            public void onMinusClick(Counter counter) {

                int value = counter.value;
                value--;
                mCounterViewModel.setValue(counter, value);
            }

            /*open counterActivity*/
            @Override
            public void onOpen(Counter counter) {

                startActivity(new Intent(MainActivity.this, CounterActivity.class).
                        putExtra(CounterActivity.EXTRA_COUNTER_ID, counter.id));
            }
        });

        /*updates the list of counters if something changes in the counter_table*/
        mCounterViewModel.getCountersByGroup("All counters").observe(this, new Observer<List<Counter>>() {

            @Override
            public void onChanged(List<Counter> counters) {
                mCountersList.setCounters(counters);
            }

        });


        /*initialize RecyclerView and it listener*/
        mGroupsList = new GroupList_rv(findViewById(R.id.groupsList_rv), new GroupList_rv.Listener() {

            @Override
            public void onOpen(String string) {

                   startActivity(new Intent(MainActivity.this, CountersByGroupActivity.class )
                   .putExtra(CountersByGroupActivity.EXTRA_GROUP_TITLE, string));
                    finish();

            }


        });

        mCounterViewModel.getGroups().observe(this, strings -> {

            /*delete the same groups*/
             Set<String> set = new HashSet<>(strings);
             String[] result = set.toArray(new String[set.size()]);
             mGroupsList.setGroups(Arrays.asList(result));
        });
    }

    /*create new counter*/
    @Override
    public void onAddClick(String title) {

        Counter counter = new Counter(title, 0, 999999999, -999999999, 1, "All Counters");
        mCounterViewModel.insert(counter);

    }

    /*launch exact creation counter*/
    @Override
    public void onLaunchDetailedClick() {

        startActivity(new Intent(MainActivity.this, CreateCounterDetailed_AND_EditCounterActivity.class ));


    }
}
