package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class CounterActivity extends AppCompatActivity {
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private ImageButton mRefreshButton;
    private LiveData<Counter> mCounter;
    private CounterViewModel mCounterViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);

        mValue_tv = findViewById(R.id.value);
        mIncButton = findViewById(R.id.inc_value);
        mDecButton = findViewById(R.id.dec_value);
        mRefreshButton = findViewById(R.id.refresh_value);

        mCounter = mCounterViewModel.getCounter(getIntent().getLongExtra(EXTRA_COUNTER_ID, 0));
        mCounter.observe(this, counter -> {

            mValue_tv.setText(String.valueOf(counter.value));

        });


        mIncButton.setOnClickListener(v->{

           int value = Objects.requireNonNull(mCounter.getValue()).value;
            value++;
           mCounterViewModel.setValue(mCounter.getValue(), value);

        });

        mDecButton.setOnClickListener(v->{

            int value = Objects.requireNonNull(mCounter.getValue()).value;
            value--;
            mCounterViewModel.setValue(mCounter.getValue(), value);

        });

        mRefreshButton.setOnClickListener(v->{

            int value = 0;
            mCounterViewModel.setValue(Objects.requireNonNull(mCounter.getValue()), value);

        });

    }


}
