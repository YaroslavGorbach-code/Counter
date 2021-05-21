package com.yaroslavgorbachh.counter.data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.BackupAndRestore.MyBackup;
import com.yaroslavgorbachh.counter.data.BackupAndRestore.MyRestore;
import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;
import com.yaroslavgorbachh.counter.feature.HistoryManager;
import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepoImp implements Repo {

    private final CounterDatabase mDatabase;

    public RepoImp(CounterDatabase database) {
        mDatabase = database;
    }

    public AppStyle getCurrentStyle() {
        return mDatabase.appStyleDao().getCurrentColor();
    }

    public void insertCounter(Counter counter) {
        Completable.create(emitter -> mDatabase.counterDao().insert(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    @Override
    public void backup(Intent data, Context context) {
        new MyBackup.Init()
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
        new MyRestore.Init()
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
    public LiveData<List<Counter>> getCounters(String mGroup) {
        return mDatabase.counterDao().getCounters(mGroup);
    }

    @Override
    public void triggerCountersLiveData() {
        List<Counter> counters = mDatabase.counterDao().getAllCountersNoLiveData();
        mDatabase.counterDao().deleteAllCounters();
        for (Counter c : counters) {
            mDatabase.counterDao().insert(c);
        }
    }


    public void updateCounter(Counter counter) {
        Completable.create(emitter -> mDatabase.counterDao().update(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounters() {
        Completable.create(emitter -> mDatabase.counterDao().deleteAllCounters())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void insertCounterHistory(CounterHistory counterHistory) {
        mDatabase.counterHistoryDao().insert(counterHistory);
    }

    public void deleteCounterHistory(long counterId) {
        Completable.create(emitter -> mDatabase.counterHistoryDao().delete(counterId))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Single<List<Counter>> getAllCountersNoLiveData() {
        return Single.create(emitter -> emitter.onSuccess(mDatabase.counterDao().getAllCountersNoLiveData()));
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId) {
        return mDatabase.counterHistoryDao().getCounterHistoryList(counterId);
    }

    public LiveData<List<Counter>> getCounters() {
        return mDatabase.counterDao().getCounters();
    }

    public LiveData<Counter> getCounter(long id) {
        return mDatabase.counterDao().getCounter(id);
    }

    public Single<Counter> getCounterWidget(long widgetId) {
        return Single.create(emitter -> emitter.onSuccess(mDatabase.counterDao().getCounterWidget(widgetId)));
    }

    public Single<Counter> getCounterNoLiveData(long id) {
        return mDatabase.counterDao().getCounterNoLiveData(id);
    }

    public LiveData<List<String>> getGroups() {
        return mDatabase.counterDao().getGroups();
    }

    public void changeTheme(AppStyle style) {
        Completable.create(emitter -> mDatabase.appStyleDao().update(style))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteHistoryItem(CounterHistory counterHistory) {
        Completable.create(emitter ->
                mDatabase.counterHistoryDao().delete(counterHistory))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void incCounter(long id) {
        mDatabase.counterDao().inc(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounterNoLiveData(id).blockingGet().value, () ->
                        insertCounterHistory(new CounterHistory(
                                getCounterNoLiveData(id).blockingGet().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));

    }

    public void decCounter(long id) {
        mDatabase.counterDao().dec(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounterNoLiveData(id).blockingGet().value, () ->
                        insertCounterHistory(new CounterHistory(
                                getCounterNoLiveData(id).blockingGet().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
    }

    public void resetCounter(long id) {
        mDatabase.counterDao().reset(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounterNoLiveData(id).blockingGet().value, () ->
                        insertCounterHistory(new CounterHistory(
                                getCounterNoLiveData(id).blockingGet().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
    }

    public void deleteCounter(long mId) {
        Completable.create(emitter ->
                mDatabase.counterDao().delete(mId))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}





















