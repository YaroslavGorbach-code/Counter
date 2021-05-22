package com.yaroslavgorbachh.counter.data;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.History;
import com.yaroslavgorbachh.counter.feature.AboutCounterManager;
import com.yaroslavgorbachh.counter.feature.HistoryManager;
import com.yaroslavgorbachh.counter.feature.roombackup.Backup;
import com.yaroslavgorbachh.counter.feature.roombackup.Restore;
import com.yaroslavgorbachh.counter.util.DateAndTimeUtil;

import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepoImp implements Repo {
    private final Db mDatabase;
    public RepoImp(Db database) {
        mDatabase = database;
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

    public void editCounter(Counter counter) {
        mDatabase.counterDao().update(counter);
    }

    public void deleteCounters() {
        mDatabase.counterDao().deleteAllCounters();
    }

    public void addHistory(History history) {
        mDatabase.counterHistoryDao().insert(history);
    }

    public void removeCounterHistory(long counterId) {
        mDatabase.counterHistoryDao().deleteCounterHistory(counterId);
    }

    @Override
    public void removeHistoryItem(long id) {
        mDatabase.counterHistoryDao().delete(id);
    }

    public LiveData<List<History>> getHistoryList(long counterId) {
        return mDatabase.counterHistoryDao().getHistoryList(counterId);
    }

    public LiveData<List<Counter>> getCounters() {
        return mDatabase.counterDao().getCounters();
    }

    public Observable<Counter> getCounter(long id) {
        return mDatabase.counterDao().getCounter(id);
    }

    public Single<Counter> getCounterWidget(long widgetId) {
        return Single.create(emitter -> emitter.onSuccess(mDatabase.counterDao().getCounterWidget(widgetId)));
    }

    public LiveData<List<String>> getGroups() {
        return mDatabase.counterDao().getGroups();
    }

    public void changeTheme(AppStyle style) {
        Completable.create(emitter -> mDatabase.appStyleDao().update(style))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void incCounter(long id) {
        mDatabase.counterDao().inc(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(
                                getCounter(id).blockingFirst().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
        editCounter(AboutCounterManager.updateMaxCounterValue(getCounter(id).blockingFirst()));

    }

    public void decCounter(long id) {
        mDatabase.counterDao().dec(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(
                                getCounter(id).blockingFirst().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
        editCounter(AboutCounterManager.updateMinCounterValue(getCounter(id).blockingFirst()));
    }

    public void resetCounter(long id) {
        editCounter(AboutCounterManager.updateValueBeforeReset(getCounter(id).blockingFirst()));
        mDatabase.counterDao().reset(id);
        HistoryManager.getInstance()
                .saveValueWitDelay(id, getCounter(id).blockingFirst().value, () ->
                        addHistory(new History(
                                getCounter(id).blockingFirst().value,
                                DateAndTimeUtil.convertDateToString(new Date()), id)));
        editCounter(AboutCounterManager.updateLastResetDate(getCounter(id).blockingFirst()));
    }

    public void deleteCounter(long mId) {
        Completable.create(emitter ->
                mDatabase.counterDao().delete(mId))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }
}





















