package com.yaroslavgorbachh.counter.counterWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.yaroslavgorbachh.counter.MainActivity;
import com.yaroslavgorbachh.counter.MyApplication;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.counterSettings.themes.ThemeUtility;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Repo;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * Implementation of App Widget functionality.
 */
public class CounterWidgetProvider extends AppWidgetProvider {
    public static final String INC_CLICK = "INC_CLICK";
    public static final String DEC_CLICK = "DECK_CLICK";
    public static final String OPEN_CLICK = "OPEN_CLICK";
    public static final String START_MAIN_ACTIVITY_EXTRA = "START_MAIN_ACTIVITY_EXTRA";
    private final CompositeDisposable mDisposables = new CompositeDisposable();

    @Inject Repo repo;

    @Override
    public void onReceive(Context context, Intent intent) {
        MyApplication application = (MyApplication) context.getApplicationContext();
        application.appComponent.counterWidgetComponent().create().inject(this);

        long widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);


       Disposable disposable = repo.getCounterWidget(widgetId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(counter -> {
                    if (Objects.requireNonNull(intent.getAction()).equals(INC_CLICK) && counter != null) {
                        counter.inc(context, repo, null);
                        appWidgetManager.updateAppWidget(counter.widgetId,
                                getRemoteViews(counter, counter.widgetId, context, appWidgetManager, false));
                    }

                    if (Objects.requireNonNull(intent.getAction()).equals(DEC_CLICK) && counter != null) {
                        counter.dec(context, repo, null);
                        appWidgetManager.updateAppWidget(counter.widgetId,
                                getRemoteViews(counter, counter.widgetId, context, appWidgetManager, false));
                    }

                    if (Objects.requireNonNull(intent.getAction()).equals(OPEN_CLICK) && counter != null) {
                        Intent startMainActivityIntent = new Intent(context, MainActivity.class);
                        startMainActivityIntent.putExtra(START_MAIN_ACTIVITY_EXTRA, counter.id);
                        startMainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(startMainActivityIntent);
                    }
                },error->{});

       mDisposables.add(disposable);

        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_counter_widget);
        resizeWidgetViews(newOptions, views);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static void resizeWidgetViews(Bundle newOptions, RemoteViews views) {

        int minWidth = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);


        if (maxHeight > 100 || minWidth > 100) {
            views.setViewVisibility(R.id.dec, View.VISIBLE);
            views.setViewVisibility(R.id.fullScreen, View.VISIBLE);
            views.setTextViewTextSize(R.id.value, COMPLEX_UNIT_SP, 50);
            views.setTextViewTextSize(R.id.title, COMPLEX_UNIT_SP, 16);
        }

        if (maxHeight > 400 || minWidth > 400) {
            views.setTextViewTextSize(R.id.value, COMPLEX_UNIT_SP, 70);
        }

        if (maxHeight < 100 || minWidth < 100) {
            views.setViewVisibility(R.id.dec, View.GONE);
            views.setViewVisibility(R.id.fullScreen, View.GONE);
            views.setTextViewTextSize(R.id.value, COMPLEX_UNIT_SP, 25);
            views.setTextViewTextSize(R.id.title, COMPLEX_UNIT_SP, 12);
        }
    }


    public static RemoteViews getRemoteViews(Counter widgetCounter, int appWidgetId, Context context, AppWidgetManager appWidgetManager, boolean isFirsTimeCreated) {
        Bundle appWidgetOptions = appWidgetManager.getAppWidgetOptions(appWidgetId);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_counter_widget);

        Intent incIntent = new Intent(context, CounterWidgetProvider.class);
        incIntent.setAction(INC_CLICK);
        incIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // we need to embed the extras into the data so that the extras will not be ignored.
        incIntent.setData(Uri.parse(incIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent incPendingIntent = PendingIntent.getBroadcast(context, 0, incIntent, 0);
        views.setOnClickPendingIntent(R.id.value, incPendingIntent);

        Intent decIntent = new Intent(context, CounterWidgetProvider.class);
        decIntent.setAction(DEC_CLICK);
        decIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // we need to embed the extras into the data so that the extras will not be ignored.
        decIntent.setData(Uri.parse(incIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent decPendingIntent = PendingIntent.getBroadcast(context, 0, decIntent, 0);
        views.setOnClickPendingIntent(R.id.dec, decPendingIntent);

        Intent openIntent = new Intent(context, CounterWidgetProvider.class);
        openIntent.setAction(OPEN_CLICK);
        openIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        // we need to embed the extras into the data so that the extras will not be ignored.
        openIntent.setData(Uri.parse(incIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent openPendingIntent = PendingIntent.getBroadcast(context, 0, openIntent, 0);
        views.setOnClickPendingIntent(R.id.fullScreen, openPendingIntent);


        if (widgetCounter != null) {
            if (isFirsTimeCreated){
                views.setImageViewResource(R.id.toolbar_color, R.drawable.widget_toolbar_bg);
                views.setInt(R.id.toolbar_color, "setColorFilter", ThemeUtility.fetchAccentColor(context));
            }

            views.setTextViewText(R.id.value, String.valueOf(widgetCounter.value));
            views.setTextViewText(R.id.title, widgetCounter.title);
            resizeWidgetViews(appWidgetOptions, views);
        }

        return views;
    }

    public static Disposable updateWidgets(Context context, Repo repo) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        Disposable disposable = null;
        int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(context, CounterWidgetProvider.class));
        if (ids != null) {
            for (int id : ids) {
                disposable = repo.getCounterWidget(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(counter -> {
                            if (counter != null) {
                                appWidgetManager.updateAppWidget(counter.widgetId,
                                        getRemoteViews(counter, counter.widgetId, context, appWidgetManager, false));
                                setWidgetColor(context, counter.widgetId, appWidgetManager);

                            } else {
                                RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_counter_widget);
                                views.setTextViewText(R.id.value, context.getString(R.string.widget_deleted));
                                views.setTextViewText(R.id.title, context.getString(R.string.widget_deleted));
                                views.setInt(R.id.toolbar_color, "setBackgroundColor", Color.GRAY);
                                appWidgetManager.updateAppWidget(id, views);
                            }
                        }, error ->{});
            }
        }
    return disposable;
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

    public static void setWidgetColor(Context context, int widgetId, AppWidgetManager appWidgetManager) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.layout_counter_widget);
        views.setImageViewResource(R.id.toolbar_color, R.drawable.widget_toolbar_bg);
        views.setInt(R.id.toolbar_color, "setColorFilter", ThemeUtility.fetchAccentColor(context));
        appWidgetManager.updateAppWidget(widgetId, views);
        }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        mDisposables.dispose();
    }
}


