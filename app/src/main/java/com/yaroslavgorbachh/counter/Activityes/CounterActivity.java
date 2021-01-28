package com.yaroslavgorbachh.counter.Activityes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.yaroslavgorbachh.counter.FastCountButton;
import com.yaroslavgorbachh.counter.Fragments.Dialogs.DeleteCounterDialog;
import com.yaroslavgorbachh.counter.ViewModels.CounterViewModel;
import com.yaroslavgorbachh.counter.ViewModels.Factories.CounterViewModelFactory;
import com.yaroslavgorbachh.counter.R;

public class CounterActivity extends AppCompatActivity implements DeleteCounterDialog.DeleteDialogListener {
    public static final String EXTRA_COUNTER_ID = "EXTRA_COUNTER_ID";
    private TextView mValue_tv;
    private TextView mIncButton;
    private TextView mDecButton;
    private ImageButton mResetButton;
    private CounterViewModel mViewModel;
    private Toolbar mToolbar;
    private TextView mCounterTitle;
    private View mLayout;
    private long mCounterId;
    private Button mSaveToHistoryButton;
    private ImageView mAllInclusiveMin_iv;
    private ImageView mAllInclusiveMAx_iv;
    private TextView mMaxValue_tv;
    private TextView mMinValue_tv;
    private TextView mGroupTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        /*initialize fields*/
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
        mGroupTitle = findViewById(R.id.groupTitle);
        mCounterId = getIntent().getLongExtra(EXTRA_COUNTER_ID, -1);
        mViewModel = new ViewModelProvider(this, new CounterViewModelFactory(getApplication(),
                mCounterId)).get(CounterViewModel.class);

        /*inflating menu, navigationIcon and set listeners*/
        mToolbar.inflateMenu(R.menu.menu_counter_activiry);
        mToolbar.setOnMenuItemClickListener(i -> {
            switch (i.getItemId()) {
                case R.id.counterDelete:
                    new DeleteCounterDialog().show(getSupportFragmentManager(), "DialogCounterDelete");
                    break;
                case R.id.counterEdit:
                    startActivity(new Intent(CounterActivity.this, CreateEditCounterActivity.class)
                            .putExtra(CreateEditCounterActivity.EXTRA_COUNTER_ID, mCounterId));
                    break;
                case R.id.counterHistory:
                    startActivity(new Intent(CounterActivity.this, CounterHistoryActivity.class).
                            putExtra(CounterHistoryActivity.EXTRA_COUNTER_ID, mCounterId));
                    break;
            }
            return true;
        });
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        mToolbar.setNavigationOnClickListener(i -> finish());

        /*each new counter value is setting to textView*/
        mViewModel.mCounter.observe(this, counter -> {
            /*if counter == null that means it was deleted*/
            if (counter != null) {
                mValue_tv.setText(String.valueOf(counter.value));
                mCounterTitle.setText(counter.title);
                mGroupTitle.setText(counter.grope);

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
            } else {
                finish();
            }
        });

        /*saving counter value to history*/
        mSaveToHistoryButton.setOnClickListener(v -> {
            mViewModel.saveValueToHistory();
        });

        /*counter +*/
        new FastCountButton(mIncButton, () -> {
            mViewModel.incCounter();
        });

        /*counter -*/
        new FastCountButton(mDecButton, () -> {
            mViewModel.decCounter();
        });

        /*reset counter*/
        mResetButton.setOnClickListener(v -> {
            mViewModel.resetCounter();
            Snackbar.make(mLayout, "Counter reset", BaseTransientBottomBar.LENGTH_LONG)
                    .setAction("UNDO", v1 -> {
                        mViewModel.restoreValue();
                    }).show();
        });
    }

    /*delete counter*/
    @Override
    public void onDialogDeleteClick() {
        mViewModel.deleteCounter();
    }


    /*method for changing the font size when changing the value of the counter*/
    private void setTextViewSize() {
        switch (mValue_tv.getText().length()) {
            case 1:
            case 2:
                mValue_tv.setTextSize(150);
                break;
            case 3:
                mValue_tv.setTextSize(130);
                break;
            case 4:
                mValue_tv.setTextSize(120);
                break;
            case 5:
                mValue_tv.setTextSize(110);
                break;
            case 6:
                mValue_tv.setTextSize(100);
                break;
            case 7:
                mValue_tv.setTextSize(90);
                break;
            case 8:
                mValue_tv.setTextSize(80);
                break;
            case 9:
                mValue_tv.setTextSize(70);
                break;
            case 10:
            case 11:
                mValue_tv.setTextSize(60);
                break;
            case 12:
            case 13:
                mValue_tv.setTextSize(50);
                break;
            case 14:
            case 17:
            case 15:
            case 16:
            case 18:
                mValue_tv.setTextSize(40);
                break;

        }
    }
}
