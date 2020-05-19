package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CreateCounterDetailed_AND_EditCounterActivity extends AppCompatActivity {

    private String mTitle;
    private long mValue;
    private long mStep;
    private String mGroup;
    private long mMaxValue;
    private long mMinValue;

    private TextInputEditText mTitle_et;
    private TextInputEditText mValue_et;
    private TextInputEditText mStep_et;
    private TextInputEditText mMaxValue_et;
    private TextInputEditText mMinValue_et;
    private AutoCompleteTextView mGroups_et;
    private CounterViewModel mViewModel;
    private Toolbar mToolbar;
    private LiveData<Counter> mCounter;
    private long mCounterId;
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_edit_counter_detailed);

        /*initialize fields*/
        mTitle_et = findViewById(R.id.counterTitle_addCounter_detailed);
        mValue_et = findViewById(R.id.counterValue_addCounter_detailed);
        mStep_et = findViewById(R.id.counterStep_addCounter_detailed);
        mMaxValue_et = findViewById(R.id.counterMaxValue_addCounter_detailed);
        mMinValue_et = findViewById(R.id.counterMinValue_addCounter_detailed);
        mToolbar = findViewById(R.id.toolbar_counterCreateActivity);
        mViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        mGroups_et = findViewById(R.id.filled_exposed_dropdown);
        mCounterId = (getIntent().getLongExtra(EXTRA_COUNTER_ID, -1));
        mCounter = mViewModel.getCounter(mCounterId);

        /*if mCounter == null that means that counter will be created*/
        if (mCounterId<0){

            mTitle = "";
            mValue = 0;
            mStep = 0;
            mGroup = "";

            /*if mCounter != null that means counter will
             be updated and we ned to get old counter values*/
        }else{

            mCounter.observe(this, counter->{

                mTitle = counter.title;
                mValue = counter.value;
                mStep = counter.step;
                mGroup = counter.grope;
                mMaxValue = counter.maxValue;
                mMinValue = counter.minValue;

                mTitle_et.setText(counter.title);
                mValue_et.setText(String.valueOf(counter.value));
                mStep_et.setText(String.valueOf(counter.step));
                mGroups_et.setText(counter.grope);
                mMaxValue_et.setText(String.valueOf(counter.maxValue));
                mMinValue_et.setText(String.valueOf(counter.minValue));

            });

        }

        /*set navigationIcon, inflate menu, and set listeners*/
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setNavigationOnClickListener(i-> finish());
        mToolbar.inflateMenu(R.menu.menu_counter_create_activity);



        /*create new counter*/
        mToolbar.setOnMenuItemClickListener(i->{


            /*if title is empty show error*/
            if(mTitle_et.getText().toString().trim().isEmpty()){

                mTitle_et.setError("This field cannot be empty");

            }else{

                mTitle = mTitle_et.getText().toString();
            }


            /*if value is empty show error*/
            if (String.valueOf(mValue_et.getText()).trim().isEmpty()) {

                mValue_et.setError("This field cannot be empty");

            }else {

                mValue = Long.parseLong(String.valueOf(mValue_et.getText()));
            }

            /*if step is empty show error*/
            if (String.valueOf(mStep_et.getText()).trim().isEmpty()){

                mStep_et.setError("This field cannot be empty");

            }else {

                mStep = Long.parseLong(String.valueOf(mStep_et.getText()));
            }

            /*if maxValue is empty show error*/
            if (String.valueOf(mMaxValue_et.getText()).trim().isEmpty()){

                mMaxValue = 999999999;

            }else {

                mMaxValue = Long.parseLong(String.valueOf(mMaxValue_et.getText()));

            }

            /*if minValue is empty show error*/
            if (String.valueOf(mMinValue_et.getText()).trim().isEmpty()){

                mMinValue = -999999999;

            }else {

                mMinValue = Long.parseLong(String.valueOf(mMinValue_et.getText()));

            }

            /*if group is empty show error*/
            if(mGroups_et.getText().toString().trim().isEmpty()){

                mGroup = null;

            }else{

                mGroup = mGroups_et.getText().toString();
            }


            /*if all fields are filled create counter*/
            if ( !(String.valueOf(mValue_et.getText()).trim().isEmpty())
                    &&!(String.valueOf(mTitle_et.getText()).trim().isEmpty())
                    && !(String.valueOf(mStep_et.getText()).trim().isEmpty())){


                /*if mCounter == null insert counter*/
                if (mCounterId<0){
                    Counter counter = new Counter(mTitle, mValue, mMaxValue, mMinValue, mStep, mGroup);
                    mViewModel.insert(counter);
                    finish();

                    /*if mCounter != null update counter*/
                }else{
                    Counter counter = new Counter(mTitle, mValue, mMaxValue, mMinValue, mStep, mGroup);
                    counter.setId(mCounterId);
                    mViewModel.update(counter);
                    finish();
                }
            }

            return true;
        });

        /*each new group sets into dropdown_menu*/
         mViewModel.getGroups().observe(this, strings -> {

              /*delete the same groups*/
          Set<String> set = new HashSet<>(strings);
          String[] result = set.toArray(new String[set.size()]);

          ArrayAdapter<String> adapter =
                  new ArrayAdapter<>(
                          CreateCounterDetailed_AND_EditCounterActivity.this,
                          R.layout.dropdown_menu_popup_item,
                          result);
          mGroups_et.setAdapter(adapter);

      });

    }
}
