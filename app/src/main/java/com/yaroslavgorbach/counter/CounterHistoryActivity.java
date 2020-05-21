package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CounterHistoryActivity extends AppCompatActivity {

    private CounterHistoryList_rv mHistoryList;
    private Spinner mSpinner;
    private Toolbar mToolbar;
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";
    private HistoryViewModel mHistoryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_history);


        /*initialize fields*/
        mHistoryViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        mHistoryList = new CounterHistoryList_rv(findViewById(R.id.counterHistory_rv));
        mToolbar = findViewById(R.id.toolbar_history);
        mSpinner = findViewById(R.id.spinner);

        /*initialize navigation listener*/
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i-> finish());

        /*set toolTipText*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSpinner.setTooltipText("Chose sort");
        }

        /*creating adapter for spinner*/
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.history_sort_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            mSpinner.setAdapter(adapter);

        /*setting listener for selected item in spinner*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String[] choose = getResources().getStringArray(R.array.history_sort_items);

                if(choose[position].equals("Sort by time")){

                    /*update list of history sort by time*/
                    mHistoryViewModel.getCounterHistoryList(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1))
                            .observe(CounterHistoryActivity.this, counterHistories -> {

                                mHistoryList.setHistory(counterHistories);

                            });

                }else {

                    /*update list of history sort by value*/
                    mHistoryViewModel.getCounterHistoryListSortByValue(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1))
                            .observe(CounterHistoryActivity.this, counterHistories -> {

                                mHistoryList.setHistory(counterHistories);

                            });

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}
