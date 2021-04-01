package com.yaroslavgorbachh.counter.counterWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;

import com.yaroslavgorbachh.counter.Accessibility;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Objects;

import javax.inject.Inject;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;

/**
 * Implementation of App Widget functionality.
 */
public class CounterWidgetProvider extends AppWidgetProvider {
    public static String INC_CLICK = "INC_CLICK";
    @Inject
    Repo mRepo;

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        application.appComponent.counterWidgetComponent().create().inject(this);

        long widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Counter counterWidget = mRepo.getCounterWidget(widgetId);

        if (Objects.requireNonNull(intent.getAction()).equals(INC_CLICK) && counterWidget != null) {
            counterWidget.inc(context, mRepo, null);
            counterWidget = mRepo.getCounterWidget(widgetId);
            appWidgetManager.updateAppWidget(counterWidget.widgetId,
                    getRemoteViews(counterWidget, counterWidget.widgetId, context));
        }
        super.onReceive(context, intent);
    }

    public static RemoteViews getRemoteViews(Counter widgetCounter, int appWidgetId, Context context) {
        Intent intent = new Intent(context, CounterWidgetProvider.class);
        intent.setAction(INC_CLICK);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // we need to embed the extras into the data so that the extras will not be ignored.
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
        views.setOnClickPendingIntent(R.id.widget, pendingIntent);

        if (widgetCounter != null) {
            views.setTextViewText(R.id.widget_value, String.valueOf(widgetCounter.value));
            views.setTextViewText(R.id.appwidget_text, widgetCounter.title);
        }

        return views;
    }

    public static void updateWidgets(Context context, Repo repo) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, CounterWidgetProvider.class));
        if (ids != null) {
            for (int id : ids) {
                Counter counter = repo.getCounterWidget(id);
                if (counter != null) {
                    appWidgetManager.updateAppWidget(counter.widgetId,
                            getRemoteViews(counter, counter.widgetId, context));
                } else {
                    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.counter_widget);
                    // TODO: 4/1/2021 translate
                    views.setTextViewText(R.id.widget_value, "DELETED");
                    views.setTextViewText(R.id.appwidget_text, "DELETED");
                    appWidgetManager.updateAppWidget(id, views);
                }
            }
        }
    }

    public static boolean checkWidgetIfExists(int widgetId, Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, CounterWidgetProvider.class));
        if (ids != null) {
            for (int id : ids) {
                if (id == widgetId)
                    return true;
            }
        }
        return false;
    }
}

