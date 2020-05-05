package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class CounterActivity extends AppCompatActivity implements DeleteCounterDialog.DeleteDialogListener {
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private ImageButton mResetButton;
    private LiveData<Counter> mCounter;
    private CounterViewModel mCounterViewModel;
    private Toolbar mToolbar;
    private TextView mCounterTitle;
    private LinearLayout mLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /*initialize fields*/
        mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        mValue_tv = findViewById(R.id.value);
        mIncButton = findViewById(R.id.inc_value);
        mDecButton = findViewById(R.id.dec_value);
        mResetButton = findViewById(R.id.reset_value);
        mToolbar = findViewById(R.id.counterActivity_toolbar);
        mCounterTitle = findViewById(R.id.counterTitle);
        mLayout = findViewById(R.id.counterLayout);
        mCounter = mCounterViewModel.getCounter(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1));


        /*inflating menu, navigationIcon and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_activiry);
        mToolbar.setOnMenuItemClickListener(i->{

            switch (i.getItemId()){

                case R.id.counterDelete:

                    new DeleteCounterDialog().show(getSupportFragmentManager(), "DialogCounterDelete");

                    break;

            }


            return true;
        });
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i-> finish());


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

        /*reset counter*/
        mResetButton.setOnClickListener(v->{

            int oldValue = Objects.requireNonNull(mCounter.getValue()).value;
            int value = 0;
            mCounterViewModel.setValue(Objects.requireNonNull(mCounter.getValue()), value);
            Snackbar.make(mLayout,"Counter reset", BaseTransientBottomBar.LENGTH_LONG)
                    .setAction("UNDO", v1 ->
                            mCounterViewModel.setValue(Objects.requireNonNull(mCounter.getValue()), oldValue)).show();

        });

    }

    /*delete counter*/
    @Override
    public void onDialogDeleteClick() {

        mCounterViewModel.delete(mCounter.getValue());

    }

}
