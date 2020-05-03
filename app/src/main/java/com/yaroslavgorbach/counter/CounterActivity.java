package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Objects;

public class CounterActivity extends AppCompatActivity implements DeleteCounterDialog.DeleteDialogListener {
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private ImageButton mRefreshButton;
    private LiveData<Counter> mCounter;
    private CounterViewModel mCounterViewModel;
    private Toolbar mToolbar;
    private TextView mCounterTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /*initialize fields*/
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        mValue_tv = findViewById(R.id.value);
        mIncButton = findViewById(R.id.inc_value);
        mDecButton = findViewById(R.id.dec_value);
        mRefreshButton = findViewById(R.id.refresh_value);
        mToolbar = findViewById(R.id.counterActivity_toolbar);
        mCounterTitle = findViewById(R.id.counterTitle);
        mCounter = mCounterViewModel.getCounter(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1));


        /*inflating menu and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_activiry);
        mToolbar.setOnMenuItemClickListener(i->{

            switch (i.getItemId()){

                case R.id.counterDelete:

                    new DeleteCounterDialog().show(getSupportFragmentManager(), "DialogCounterDelete");

                    break;

            }


            return true;
        });


        /*each new counter value is set to textView*/
        mCounter.observe(this, counter -> {

        if (counter != null) {

            mValue_tv.setText(String.valueOf(counter.value));
            // TODO: 03.05.2020 установку текста нужно делать в другом месте но пока пусть будет тут
            mCounterTitle.setText(counter.title);

        }else {

            finish();
            
        }


        });



        /*counter +*/
        mIncButton.setOnClickListener(v->{

           int value = Objects.requireNonNull(mCounter.getValue()).value;
            value++;
           mCounterViewModel.setValue(mCounter.getValue(), value);

        });

        /*counter -*/
        mDecButton.setOnClickListener(v->{

            int value = Objects.requireNonNull(mCounter.getValue()).value;
            value--;
            mCounterViewModel.setValue(mCounter.getValue(), value);

        });

        /*set counter value 0*/
        mRefreshButton.setOnClickListener(v->{

            int value = 0;
            mCounterViewModel.setValue(Objects.requireNonNull(mCounter.getValue()), value);

        });

    }

    @Override
    public void onDialogDeleteClick() {

        mCounterViewModel.delete(mCounter.getValue());

    }

}
