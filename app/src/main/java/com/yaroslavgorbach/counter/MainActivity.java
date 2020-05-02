package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.yaroslavgorbach.counter.CounterList_rv.Listener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private  CounterList_rv mCountersList;
    private CounterViewModel mCounterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        mCountersList = new CounterList_rv((RecyclerView) findViewById(R.id.countersList_rv), new Listener() {

            @Override
            public void onPlusClick(Counter counter) {

            }

            @Override
            public void onMinusClick(Counter counter) {


            }

            @Override
            public void onOpen(Counter counter) {

                startActivity(new Intent(MainActivity.this, CounterActivity.class));
            }
        });

        mCounterViewModel.getAllCounters().observe(this, counters -> {

            mCountersList.setCounters(counters);

        });

    }
}
