package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.yaroslavgorbach.counter.CounterList_rv.Listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AddCounterDialog.AddCounterListener {

    private  CounterList_rv mCountersList;
    private CounterViewModel mCounterViewModel;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*initialize fields*/
        mToolbar = findViewById(R.id.toolbar_mainActivity);
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        /*inflating menu and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_main_activity);
        mToolbar.setOnMenuItemClickListener(i->{

            switch (i.getItemId()){

                case R.id.counterAdd:

                    new AddCounterDialog().show(getSupportFragmentManager(), "Add Counter");

                    break;

            }


            return true;

        });


        /*initialize RecyclerView and it listener*/
        mCountersList = new CounterList_rv((RecyclerView) findViewById(R.id.countersList_rv), new Listener() {

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
        mCounterViewModel.getAllCounters().observe(this, counters -> {

            mCountersList.setCounters(counters);

        });

    }

    @Override
    public void onAddClick(String title) {

        Counter counter = new Counter(title, 0, 100, 100, 1, "null");
        mCounterViewModel.insert(counter);

    }
}
