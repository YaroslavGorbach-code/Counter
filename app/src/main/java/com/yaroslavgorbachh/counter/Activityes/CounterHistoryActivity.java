package com.yaroslavgorbachh.counter.Activityes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.yaroslavgorbachh.counter.Models.CounterHistory;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.RecyclerViews.CounterHistoryList_rv;
import com.yaroslavgorbachh.counter.Database.ViewModels.HistoryViewModel;

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
        mToolbar = findViewById(R.id.toolbar_history);
        mSpinner = findViewById(R.id.spinner);

        /*initialize navigation listener*/
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i-> finish());

        setAdapterForHistoryItems();

        /*setting listener for selected item in spinner*/
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortList(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void sortList(int position) {
        String[] choose = getResources().getStringArray(R.array.history_sort_items);

        if(choose[position].equals("Sort by time") || choose[position].equals("Сортировка по дате")){
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

    private void setAdapterForHistoryItems() {
        mHistoryList = new CounterHistoryList_rv(findViewById(R.id.counterHistory_rv),
                new CounterHistoryList_rv.HistoryItemClickListener() {
                    @Override
                    public void onDelete(CounterHistory counterHistory) {
                        mHistoryViewModel.delete(counterHistory);
                    }
                });

        /*set toolTipText*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mSpinner.setTooltipText("Chose sort");
        }

        /*creating adapter for spinner*/
        ArrayAdapter<?> adapter =
                ArrayAdapter.createFromResource(this, R.array.history_sort_items, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(adapter);
    }
}
