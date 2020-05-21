package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
    private long mCounterId;
    private Button mSaveToHistoryButton;
    private ImageView mAllInclusiveMin_iv;
    private ImageView mAllInclusiveMAx_iv;
    private TextView mMaxValue_tv;
    private TextView mMinValue_tv;

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
        mSaveToHistoryButton = findViewById(R.id.saveToHistoryButton);
        mAllInclusiveMAx_iv = findViewById(R.id.iconAllInclusiveMax);
        mAllInclusiveMin_iv = findViewById(R.id.iconAllInclusiveMin);
        mMaxValue_tv = findViewById(R.id.maxValue);
        mMinValue_tv = findViewById(R.id.minValue);
        mCounter = mCounterViewModel.getCounter(getIntent().getLongExtra(EXTRA_COUNTER_ID, -1));


        /*setting valueTextView size depending on value*/
        setTextViewSize();


        /*inflating menu, navigationIcon and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_activiry);
        mToolbar.setOnMenuItemClickListener(i->{

            switch (i.getItemId()){

                case R.id.counterDelete:

                    new DeleteCounterDialog().show(getSupportFragmentManager(), "DialogCounterDelete");

                    break;

                case R.id.counterEdit:

                    startActivity(new Intent(CounterActivity.this, CreateCounterDetailedEditCounterActivity.class)
                            .putExtra(CreateCounterDetailedEditCounterActivity.EXTRA_COUNTER_ID, mCounterId));

                    break;

                case R.id.counterHistory:

                    startActivity(new Intent(CounterActivity.this, CounterHistoryActivity.class).
                            putExtra(CounterHistoryActivity.EXTRA_COUNTER_ID, mCounterId));

                    break;

            }


            return true;
        });

        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i-> finish());



        /*each new counter value is set to textView*/
        mCounter.observe(this, counter -> {


            /*if counter == null that means it was deleted*/
        if (counter != null) {

            mCounterId = counter.id;
            mValue_tv.setText(String.valueOf(counter.value));
            mCounterTitle.setText(counter.title);

            if (counter.maxValue != Long.parseLong("9999999999999999")) {

                mAllInclusiveMAx_iv.setVisibility(View.GONE);
                mMaxValue_tv.setVisibility(View.VISIBLE);
                mMaxValue_tv.setText(String.valueOf(counter.maxValue));

            }


            if (counter.minValue != Long.parseLong("-9999999999999999")) {

                mAllInclusiveMin_iv.setVisibility(View.GONE);
                mMinValue_tv.setVisibility(View.VISIBLE);
                mMinValue_tv.setText(String.valueOf(counter.minValue));

            }

            setTextViewSize();


        }else {

            finish();

        }

        });


        /*saving counter value to history*/
        mSaveToHistoryButton.setOnClickListener(v -> {

            /*getting current data and time */
            Date currentDate = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss", Locale.getDefault());
            String date = dateFormat.format(currentDate);

                    mCounterViewModel.insert(new CounterHistory(Objects.requireNonNull(
                            mCounter.getValue()).value, date, mCounter.getValue().id));
            Toast.makeText(this, "Value " + mCounter.getValue().value + " saved to counter history", Toast.LENGTH_SHORT).show();
        });

        /*counter +*/
        mIncButton.setOnClickListener(v->{

            long maxValue;
            long incOn;
            long value = Objects.requireNonNull(mCounter.getValue()).value;
            incOn = mCounter.getValue().step;
            maxValue = mCounter.getValue().maxValue;
            value += incOn;

            if (value > maxValue){

                mCounterViewModel.setValue(mCounter.getValue(), maxValue);
                Toast.makeText(this, "This is maximum", Toast.LENGTH_SHORT).show();

            }else {

                mCounterViewModel.setValue(mCounter.getValue(), value);

            }




        });

        /*counter -*/
        mDecButton.setOnClickListener(v->{

            long minValue;
            long decOn;
            long value = Objects.requireNonNull(mCounter.getValue()).value;
            decOn = mCounter.getValue().step;
            minValue = mCounter.getValue().minValue;
            value -=decOn;

            if (value < minValue){

                mCounterViewModel.setValue(mCounter.getValue(), minValue);
                Toast.makeText(this, "This is minimum", Toast.LENGTH_SHORT).show();


            }else {

                mCounterViewModel.setValue(mCounter.getValue(), value);


            }


        });

        /*reset counter*/
        mResetButton.setOnClickListener(v->{

            long oldValue = Objects.requireNonNull(mCounter.getValue()).value;
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
        mCounterViewModel.delete(mCounterId);

    }

    /*method for changing the font size when changing the value of the counter*/
    private void setTextViewSize(){

        if (mValue_tv.getText().length() == 1){

            mValue_tv.setTextSize(150);

        }

        if (mValue_tv.getText().length() == 2){

            mValue_tv.setTextSize(150);

        }

        if (mValue_tv.getText().length() == 3){

            mValue_tv.setTextSize(130);

        }

        if (mValue_tv.getText().length() == 4){

            mValue_tv.setTextSize(120);

        }

        if (mValue_tv.getText().length() == 5){

            mValue_tv.setTextSize(110);

        }

        if (mValue_tv.getText().length() == 6){

            mValue_tv.setTextSize(100);

        }

        if (mValue_tv.getText().length() == 7){

            mValue_tv.setTextSize(90);

        }

        if (mValue_tv.getText().length() == 8){

            mValue_tv.setTextSize(80);

        }

        if (mValue_tv.getText().length() == 9){

            mValue_tv.setTextSize(70);

        }

        if (mValue_tv.getText().length() == 10){

            mValue_tv.setTextSize(60);

        }

        if (mValue_tv.getText().length() == 11){

            mValue_tv.setTextSize(60);

        }

        if (mValue_tv.getText().length() == 12){

            mValue_tv.setTextSize(50);

        }

        if (mValue_tv.getText().length() == 13){

            mValue_tv.setTextSize(50);

        }

        if (mValue_tv.getText().length() == 14){

            mValue_tv.setTextSize(40);

        }

        if (mValue_tv.getText().length() == 15){

            mValue_tv.setTextSize(40);

        }

        if (mValue_tv.getText().length() == 16){

            mValue_tv.setTextSize(40);

        }

        if (mValue_tv.getText().length() == 17){

            mValue_tv.setTextSize(40);

        }

        if (mValue_tv.getText().length() == 18){

            mValue_tv.setTextSize(40);

        }

    }


}
