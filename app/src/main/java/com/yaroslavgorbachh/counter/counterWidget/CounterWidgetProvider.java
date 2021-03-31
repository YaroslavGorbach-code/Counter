package com.yaroslavgorbachh.counter.counterWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import javax.inject.Inject;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;

/**
 * Implementation of App Widget functionality.
 */
public class CounterWidgetProvider extends AppWidgetProvider {
    public static String YOUR_AWESOME_ACTION = "YourAwesomeAction";

    @Inject
    Repo mRepo;
    private Counter mCounter;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        Intent intent = new Intent(context, CounterWidgetProvider.class);
        intent.setAction(YOUR_AWESOME_ACTION);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // we need to embed the extras into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);
        if (mCounter != null) {
            views.setTextViewText(R.id.widget_value, String.valueOf(mCounter.value));
            views.setTextViewText(R.id.appwidget_text, mCounter.title);
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        application.appComponent.counterWidgetComponent().create().inject(this);
        long widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        mCounter = mRepo.getCounterWidget(widgetId);


        if (intent.getAction().equals(YOUR_AWESOME_ACTION) && mCounter != null) {
            mCounter.inc(context, mRepo, null);
            mCounter = mRepo.getCounterWidget(widgetId);

            updateAppWidget(context, appWidgetManager, Math.toIntExact(mCounter.widgetId));
        }

        if (intent.getAction().equals(ACTION_APPWIDGET_UPDATE)) {
            int[] ids = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            if (ids != null) {
                for (int id : ids) {
                    Log.v("up", "widget id = " + id);

                    Counter counter = mRepo.getCounterWidget(id);
                    mCounter = counter;
                    if (counter != null) {
                        Log.v("up", " counter widget id = " + counter.widgetId);
                        updateAppWidget(context, appWidgetManager, Math.toIntExact(counter.widgetId));
                    }
                }

            }
            mCounter = null;
        }


        super.onReceive(context, intent);

    }

}
