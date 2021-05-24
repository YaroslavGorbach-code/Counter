package com.yaroslavgorbachh.counter.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Domain.History;
import com.yaroslavgorbachh.counter.data.local.Db;
import com.yaroslavgorbachh.counter.feature.AboutCounterManager;
import com.yaroslavgorbachh.counter.feature.Accessibility;
import com.yaroslavgorbachh.counter.feature.HistoryManager;
import com.yaroslavgorbachh.counter.feature.roombackup.Backup;
import com.yaroslavgorbachh.counter.feature.roombackup.Restore;
import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepoImp implements Repo {
    private final Db mDatabase;
    private final SharedPreferences mSharedPreferences;
    private final Accessibility mAccessibility;

    public RepoImp(Db database, SharedPreferences preferences, Accessibility accessibility) {
        mDatabase = database;
        mSharedPreferences = preferences;
        mAccessibility = accessibility;
    }

    public void createCounter(Counter counter) {
        mDatabase.counterDao().insert(counter);
    }

    @Override
    public void backup(Intent data, Context context) {
        new Backup.Init()
                .database(mDatabase)
                .setContext(context)
                .uri(data.getData())
                .OnCompleteListener((success, message) -> {
                    if (success) {
                        Toast.makeText(context, context.getResources().getString(R.string.successCreatedToast,
                                data.getData().getPath()), Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context,
                                context.getResources().getString(R.string.failCreatedToast, message),
                                Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    @Override
    public void restore(Intent data, Context context) {
        new Restore.Init()
                .database(mDatabase)
                .uri(data.getData())
                .setContext(context)
                .OnCompleteListener((success, message) -> {
                    if (success) {
                        Toast.makeText(context, context.getResources().getString(R.string.successRestore), Toast.LENGTH_LONG).show();
                        new Handler().postDelayed(() -> Runtime.getRuntime().exit(0), 3000);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.failRestore), Toast.LENGTH_LONG).show();
                    }
                }).execute();
    }

    @Override
    public void triggerCountersLiveData() {
        List<Counter> counters = mDatabase.counterDao().getAllCountersNoLiveData();
        mDatabase.counterDao().deleteAllCounters();
        for (Counter c : counters) {
            mDatabase.counterDao().insert(c);
        }
    }

    @Override
    public boolean getIsOrientationLock() {
       return mSharedPreferences.getBoolean("lockOrientation", true);
    }

    @Override
    public boolean getUseVolumeButtonsIsAllow() {
        return mSharedPreferences.getBoolean("useVolumeButtons", true);
    }

    @Override
    public boolean getKeepScreenOnIsAllow() {
        return mSharedPreferences.getBoolean("keepScreenOn", true);
    }

    @Override
    public void updateCounter(Counter counter) {
        mDatabase.counterDao().update(counter);
    }

    @Override
    public void deleteCounters() {
        mDatabase.counterDao().deleteAllCounters();
    }

    @Override
    public void addHistory(History history) {
        mDatabase.counterHistoryDao().insert(history);
    }

    @Override
    public void removeCounterHistory(long counterId) {
        mDatabase.counterHistoryDao().deleteCounterHistory(counterId);
    }

    @Override
    public void removeHistoryItem(long id) {
        mDatabase.counterHistoryDao().delete(id);
    }

    @Override
    public LiveData<List<History>> getHistoryList(long counterId) {
        return mDatabase.counterHistoryDao().getHistoryList(counterId);
    }

    @Override
    public LiveData<List<Counter>> getCounters() {
        return mDatabase.counterDao().getCounters();
    }

    @Override
    public Observable<Counter> getCounter(long id) {
        return mDatabase.counterDao().getCounter(id);
    }

    @Override
    public Counter getCounterWidget(long widgetId) {
        return mDatabase.counterDao().getCounterWidget(widgetId);
    }

    @Override
    public LiveData<List<String>> getGroups() {
        return mDatabase.counterDao().getGroups();
    }

    @Override
    public void incCounter(long id) {
        mDatabase.counterDao().inc(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(
                                getCounter(id).blockingFirst().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
        updateCounter(AboutCounterManager.updateMaxCounterValue(getCounter(id).blockingFirst()));

        if (mSharedPreferences.getBoolean("clickVibration", false)){
            mAccessibility.playIncVibrationEffect();
        }
        if (mSharedPreferences.getBoolean("clickSound", true)){
            mAccessibility.playIncSoundEffect();
        }
        if (mSharedPreferences.getBoolean("clickSpeak", false)){
            mAccessibility.speechOutput(String.valueOf(getCounter(id).blockingFirst().value));
        }
    }

    @Override
    public void decCounter(long id) {
        mDatabase.counterDao().dec(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(
                                getCounter(id).blockingFirst().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
        updateCounter(AboutCounterManager.updateMinCounterValue(getCounter(id).blockingFirst()));
        if (mSharedPreferences.getBoolean("clickVibration", false)){
            mAccessibility.playDecVibrationEffect();
        }
        if (mSharedPreferences.getBoolean("clickSound", true)){
            mAccessibility.playDecSoundEffect();
        }
        if (mSharedPreferences.getBoolean("clickSpeak", false)){
            mAccessibility.speechOutput(String.valueOf(getCounter(id).blockingFirst().value));
        }
    }

    @Override
    public void resetCounter(long id) {
        updateCounter(AboutCounterManager.updateValueBeforeReset(getCounter(id).blockingFirst()));
        mDatabase.counterDao().reset(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(
                                getCounter(id).blockingFirst().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
        updateCounter(AboutCounterManager.updateLastResetDate(getCounter(id).blockingFirst()));
    }

    @Override
    public void deleteCounter(long mId) {
        Completable.create(emitter ->
                mDatabase.counterDao().delete(mId))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}















