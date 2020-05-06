package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CreateCounterDetailedActivity extends AppCompatActivity {

    private TextInputEditText mTitle_et;
    private TextInputEditText mValue_et;
    private TextInputEditText mStep_et;
    private TextInputEditText mMaxValue_et;
    private TextInputEditText mMinValue_et;
    private AutoCompleteTextView mGroups_et;
    private CounterViewModel mViewModel;
    private Toolbar mToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_counter_detailed);

        /*initialize fields*/
        mTitle_et = findViewById(R.id.counterTitle_addCounter_detailed);
        mValue_et = findViewById(R.id.counterValue_addCounter_detailed);
        mStep_et = findViewById(R.id.counterStep_addCounter_detailed);
        mMaxValue_et = findViewById(R.id.counterMaxValue_addCounter_detailed);
        mMinValue_et = findViewById(R.id.counterMinValue_addCounter_detailed);
        mToolbar = findViewById(R.id.toolbar_counterCreateActivity);
        mViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        mGroups_et = findViewById(R.id.filled_exposed_dropdown);

        /*set navigationIcon, inflate menu, and set listeners*/
        mToolbar.setNavigationIcon(R.drawable.ic_close);
        mToolbar.setNavigationOnClickListener(i-> finish());
        mToolbar.inflateMenu(R.menu.menu_counter_create_activity);

        /*create new counter*/
        mToolbar.setOnMenuItemClickListener(i->{
            String title = "";
            int value = 0;
            int step = 0;
            String group = "";
            int maxValue;
            int minValue;


            /*if title is empty show error*/
            if(mTitle_et.getText().toString().trim().isEmpty()){

                mTitle_et.setError("This field cannot be empty");

            }else{

                title = mTitle_et.getText().toString();
            }


            /*if value is empty show error*/
            if (String.valueOf(mValue_et.getText()).equals("")) {

                mValue_et.setError("This field cannot be empty");

            }else {

                value = Integer.valueOf(String.valueOf(mValue_et.getText()));
            }

            /*if step is empty show error*/
            if (String.valueOf(mStep_et.getText()).equals("")){

                mStep_et.setError("This field cannot be empty");

            }else {

                step = Integer.valueOf(String.valueOf(mStep_et.getText()));
            }

            /*if maxValue is empty show error*/
            if (String.valueOf(mMaxValue_et.getText()).equals("")){

                maxValue = 999999999;

            }else {

                maxValue = Integer.valueOf(String.valueOf(mMaxValue_et.getText()));

            }

            /*if minValue is empty show error*/
            if (String.valueOf(mMinValue_et.getText()).equals("")){

                minValue = -999999999;

            }else {

                minValue = Integer.valueOf(String.valueOf(mMinValue_et.getText()));

            }

            /*if group is empty show error*/
            if(mGroups_et.getText().toString().trim().isEmpty()){

                mGroups_et.setError("This field cannot be empty");

            }else{

                group = mGroups_et.getText().toString();
            }


            /*if all fields are filled create counter*/
            if ( !(String.valueOf(mValue_et.getText()).equals("")) &&!(title.trim().isEmpty()) && step!=0 && !(group.trim().isEmpty())){

                Counter counter = new Counter(title, value, maxValue, minValue, step, group);
                mViewModel.insert(counter);
                finish();

            }




            // TODO: 06.05.2020 save counter

            return true;
        });

        /*each new group sets into dropdown_menu*/
         mViewModel.getGroups().observe(this, strings -> {

              /*delete the same groups*/
          Set<String> set = new HashSet<>(Arrays.asList(strings));
          String[] result = set.toArray(new String[set.size()]);

          ArrayAdapter<String> adapter =
                  new ArrayAdapter<>(
                          CreateCounterDetailedActivity.this,
                          R.layout.dropdown_menu_popup_item,
                          result);
          mGroups_et.setAdapter(adapter);

      });




    }
}
