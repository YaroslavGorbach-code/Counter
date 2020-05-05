package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class CreateCounterDetailedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_counter_detailed);

        /*initialize fields*/
        CounterViewModel mCounterViewModel = new ViewModelProvider(this).get(CounterViewModel.class);
        AutoCompleteTextView editTextFilledExposedDropdown = findViewById(R.id.filled_exposed_dropdown);


        /*each new group sets into dropdown_menu*/
          mCounterViewModel.getGroups().observe(this, strings -> {

              /*delete the same groups*/
          Set<String> set = new HashSet<>(Arrays.asList(strings));
          String[] result = set.toArray(new String[set.size()]);

          ArrayAdapter<String> adapter =
                  new ArrayAdapter<>(
                          CreateCounterDetailedActivity.this,
                          R.layout.dropdown_menu_popup_item,
                          result);
          editTextFilledExposedDropdown.setAdapter(adapter);

      });




    }
}
