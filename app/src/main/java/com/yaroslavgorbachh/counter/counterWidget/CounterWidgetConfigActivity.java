package com.yaroslavgorbachh.counter.counterWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider.INC_CLICK;

public class CounterWidgetConfigActivity extends AppCompatActivity {
    @Inject Repo repo;
    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private RecyclerView mRecyclerView;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyApplication application = (MyApplication) getApplication();
        application.appComponent.counterWidgetComponent().create().inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_configuration);
        mRecyclerView = findViewById(R.id.widget_counters_rv);

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

        WidgetCountersAdapter adapter = new WidgetCountersAdapter(counter -> {
            Counter widgetCounter = repo.getCounterNoLiveData(counter.id);
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

            if (widgetCounter.widgetId!=null && CounterWidgetProvider.checkWidgetIfExists(widgetCounter.widgetId, this)){
                // TODO: 4/1/2021 translate
                Toast.makeText(this, "The widget for this counter already exists", Toast.LENGTH_LONG).show();
            }else {
                appWidgetManager.updateAppWidget(appWidgetId,
                        CounterWidgetProvider.getRemoteViews(widgetCounter, appWidgetId, this, appWidgetManager));
                widgetCounter.widgetId = appWidgetId;
                repo.updateCounter(widgetCounter);

                Intent resultIntent = new Intent();
                resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Disposable disposable = repo.getAllCountersNoLiveData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(counters -> {
                    adapter.setData(counters);
                    mRecyclerView.setAdapter(adapter);
                });
        disposables.add(disposable);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }
}
