package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CounterHistoryActivity extends AppCompatActivity {

    private CounterHistoryList_rv mHistoryList;
    private CounterViewModel mCounterViewModel;
    private String mDate;
    private Toolbar mToolbar;
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_history);


        /*initialize fields*/
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        mHistoryList = new CounterHistoryList_rv(findViewById(R.id.counterHistory_rv));
        mToolbar = findViewById(R.id.toolbar_history);

        /*initialize navigation listener*/
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i-> finish());

        /*getting current date*/
        Date currentDate = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY", Locale.getDefault());
        mDate = dateFormat.format(currentDate);

        /*if the history with today's date is already in the database then update if not then create*/
        mCounterViewModel.getCounter(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1))
                .observe(this, counter -> {


                    mCounterViewModel.getCounterHistory(counter.id, mDate).observe(this, counterHistory -> {

                        if (counterHistory == null){

                            mCounterViewModel.insert(new CounterHistory(counter.value, mDate, counter.id));

                        }else {

                            CounterHistory updateHistory = new CounterHistory( counter.value, mDate, counter.id );
                            updateHistory.setId(counterHistory.id);

                            mCounterViewModel.update(updateHistory);

                        }

                    });

                });

        /*update list of history*/
        mCounterViewModel.getCounterHistoryList(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1))
                .observe(this, counterHistories -> {

                    mHistoryList.setHistory(counterHistories);

                });

    }
}
