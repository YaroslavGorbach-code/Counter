package com.yaroslavgorbach.counter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private ImageButton mRefreshButton;
    private int mValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mValue_tv = findViewById(R.id.value);
        mIncButton = findViewById(R.id.inc_value);
        mDecButton = findViewById(R.id.dec_value);
        mRefreshButton = findViewById(R.id.refresh_value);

        mValue = 0;



        mIncButton.setOnClickListener(v->{

            mValue++;
            setValue(mValue);

        });

        mDecButton.setOnClickListener(v->{

            mValue--;
            setValue(mValue);

        });

        mRefreshButton.setOnClickListener(v->{

            mValue = 0;
            setValue(mValue);

        });

    }

    private void setValue(int value){

        mValue_tv.setText(String.valueOf(value));

    }
}
