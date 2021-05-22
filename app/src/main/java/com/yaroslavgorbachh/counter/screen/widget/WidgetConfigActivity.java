package com.yaroslavgorbachh.counter.screen.widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.yaroslavgorbachh.counter.App;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.databinding.ActivityWidgetConfigurationBinding;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class WidgetConfigActivity extends AppCompatActivity {
    @Inject Repo repo;
    @Inject SharedPreferences sharedPreferences;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        App application = (App) getApplication();
        application.appComponent.inject(this);
        super.onCreate(savedInstanceState);
        ActivityWidgetConfigurationBinding binding =
                ActivityWidgetConfigurationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        Intent configIntent = getIntent();
        Bundle extras = configIntent.getExtras();
        if (extras != null) {
            appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultValue);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        WidgetConfigView view = new WidgetConfigView(binding, new WidgetConfigView.Callback() {
            @Override
            public void onBack() { finish(); }

            @Override
            public void onWidget(Counter counter) {
                repo.getCounter(counter.id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(widgetCounter -> {
                            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(WidgetConfigActivity.this);
                            if (widgetCounter.widgetId != null && WidgetProvider.checkWidgetIfExists(widgetCounter.widgetId, WidgetConfigActivity.this)) {
                                Toast.makeText(WidgetConfigActivity.this, getString(R.string.widget_exists), Toast.LENGTH_LONG).show();
                            } else {
                                appWidgetManager.updateAppWidget(appWidgetId,
                                        WidgetProvider.getRemoteViews(widgetCounter, counter.widgetId,WidgetConfigActivity.this, appWidgetManager, true));
                                widgetCounter.widgetId = appWidgetId;
                                repo.updateCounter(widgetCounter);

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            }
                        }, error -> {
                        });
            }
        });
        repo.getCounters().observe(this, view::setCounters);
    }
}
