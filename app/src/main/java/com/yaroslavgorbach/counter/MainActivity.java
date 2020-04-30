package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.yaroslavgorbach.counter.CounterList_rv.Listener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private  CounterList_rv mCountersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Counter> mTestDta = new ArrayList<>();
        mTestDta.add(new Counter(1,"test",1, 10, 22, 1, "test", getResources().getColor(R.color.blue)));
        mTestDta.add(new Counter(2,"test2",111, 10, 22, 1, "test", getResources().getColor(R.color.green)));
        mTestDta.add(new Counter(1,"test",1, 10, 22, 1, "test", getResources().getColor(R.color.blue)));
        mTestDta.add(new Counter(2,"test2",111, 10, 22, 1, "test", getResources().getColor(R.color.orange)));
        mTestDta.add(new Counter(1,"test",1, 10, 22, 1, "test", getResources().getColor(R.color.red)));
        mTestDta.add(new Counter(2,"test2",111, 10, 22, 1, "test", getResources().getColor(R.color.turquoise)));
        mTestDta.add(new Counter(1,"test",1, 10, 22, 1, "test", getResources().getColor(R.color.violet)));
        mTestDta.add(new Counter(2,"test2",111, 10, 22, 1, "test", getResources().getColor(R.color.yellow)));
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
        mCountersList.setCounters(mTestDta);

    }
}
