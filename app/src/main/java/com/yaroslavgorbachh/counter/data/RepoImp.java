package com.yaroslavgorbachh.counter.data;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.data.domain.History;
import com.yaroslavgorbachh.counter.data.local.SharedPrefStorage;
import com.yaroslavgorbachh.counter.data.local.room.RoomDb;
import com.yaroslavgorbachh.counter.feature.AboutCounterManager;
import com.yaroslavgorbachh.counter.feature.HistoryManager;
import com.yaroslavgorbachh.counter.feature.ad.AdManager;
import com.yaroslavgorbachh.counter.feature.roombackup.Backup;
import com.yaroslavgorbachh.counter.feature.roombackup.Restore;
import com.yaroslavgorbachh.counter.util.TimeAndDataUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepoImp implements Repo {
    private final RoomDb mDatabase;
    private final SharedPreferences mSharedPreferencesSettings;
    private final SharedPrefStorage mLocalSharedPref;

    public RepoImp(RoomDb database, SharedPreferences preferencesSettings, SharedPrefStorage localPref) {
        mDatabase = database;
        mSharedPreferencesSettings = preferencesSettings;
        mLocalSharedPref = localPref;
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
        List<Counter> counters = mDatabase.counterDao().getCounters().blockingFirst();
        mDatabase.counterDao().deleteAllCounters();
        for (Counter c : counters) {
            mDatabase.counterDao().insert(c);
        }
    }

    @Override
    public boolean getIsOrientationLock() {
       return mSharedPreferencesSettings.getBoolean("lockOrientation", true);
    }

    @Override
    public boolean getUseVolumeButtonsIsAllow() {
        return mSharedPreferencesSettings.getBoolean("useVolumeButtons", true);
    }

    @Override
    public boolean getKeepScreenOnIsAllow() {
        return mSharedPreferencesSettings.getBoolean("keepScreenOn", true);
    }

    @Override
    public boolean getClickVibrationIsAllow() {
        return mSharedPreferencesSettings.getBoolean("clickVibration", false);
    }

    @Override
    public boolean getClickSoundIsAllow() {
        return mSharedPreferencesSettings.getBoolean("clickSound", true);
    }

    @Override
    public boolean getClickSpeakIsAllow() {
        return mSharedPreferencesSettings.getBoolean("clickSpeak", false);
    }

    @Override
    public int getFastCountInterval() {
        return mSharedPreferencesSettings.getInt("fastCountSpeed", 200);
    }

    @Override
    public boolean getAdIsAllow() {
        return mLocalSharedPref.getAdIsAllow();
    }

    @Override
    public void setAdIsAllow(boolean isAllow) {
        mLocalSharedPref.setAdIsAllow(isAllow);
    }

    @Override
    public boolean getInterstitialAdIsAllow() {
        return mLocalSharedPref.getInterstitialAdCount() >= AdManager.INTERSTITIAL_SHOW_LIMIT;
    }

    @Override
    public void incInterstitialAdCount() {
        if (mLocalSharedPref.getInterstitialAdCount() < AdManager.INTERSTITIAL_SHOW_LIMIT) {
            mLocalSharedPref.setInterstitialAdCount(mLocalSharedPref.getInterstitialAdCount() + 1);
        } else {
            mLocalSharedPref.setInterstitialAdCount(0);
        }
    }

    @Override
    public boolean isAscAppReviewAllow() {
        Log.v("time", TimeAndDataUtil.getDaysBetween(
                new Date(mLocalSharedPref.getTimeLastReviewAsc()), new Date()) + "");
        return TimeAndDataUtil.getDaysBetween(
                new Date(mLocalSharedPref.getTimeLastReviewAsc()), new Date()) > 5;
    }

    @Override
    public void setDateLastReviewAsc(Date date) {
        mLocalSharedPref.setTimeLastReviewAsc(date.getTime());
    }

    @Override
    public boolean getFirstOpen() {
        return mLocalSharedPref.getFirstOpen();
    }

    @Override
    public void setFirstOpen(boolean firstOpen) {
        mLocalSharedPref.setFirstOpen(firstOpen);
    }


    @Override
    public boolean getIsNightMod() {
        return mLocalSharedPref.getNightMod();
    }

    @Override
    public void setIsNightMod(boolean b) {
        mLocalSharedPref.setNightMod(b);
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
    public Observable<List<Counter>> getCounters() {
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
    public void incCounter(long id, @Nullable ValueCallback callback) {
        Counter counter = getCounter(id).blockingFirst();
        if (counter.value + counter.step >= counter.maxValue){
            counter.value = counter.maxValue;
            if (callback != null) callback.onMax();
            updateCounter(counter);

        }else if(counter.value + counter.step <= counter.minValue){
            counter.value = counter.minValue;
            if (callback != null) callback.onMin();
            updateCounter(counter);
        }
        else {
            mDatabase.counterDao().inc(id);
        }
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(getCounter(id).blockingFirst().value,
                                TimeAndDataUtil.convertDateToString(new Date()), id)));
        updateCounter(AboutCounterManager.updateMinCounterValue(getCounter(id).blockingFirst()));
    }

    @Override
    public void decCounter(long id, @Nullable ValueCallback callback) {
        Counter counter = getCounter(id).blockingFirst();
        if (counter.value - counter.step <= counter.minValue){
            counter.value = counter.minValue;
            if (callback != null) callback.onMin();
            updateCounter(counter);
        }else if (counter.value - counter.step >= counter.maxValue){
            counter.value = counter.maxValue;
            if (callback != null) callback.onMax();
            updateCounter(counter);
        } else {
            mDatabase.counterDao().dec(id);
        }
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(getCounter(id).blockingFirst().value,
                                TimeAndDataUtil.convertDateToString(new Date()), id)));
        updateCounter(AboutCounterManager.updateMinCounterValue(getCounter(id).blockingFirst()));
    }

    @Override
    public void resetCounter(long id) {
        updateCounter(AboutCounterManager.updateValueBeforeReset(getCounter(id).blockingFirst()));
        mDatabase.counterDao().reset(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(getCounter(id).blockingFirst().value,
                                TimeAndDataUtil.convertDateToString(new Date()), id)));
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















