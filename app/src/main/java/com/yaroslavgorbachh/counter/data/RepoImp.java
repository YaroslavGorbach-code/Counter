package com.yaroslavgorbachh.counter.data;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.data.Daos.CounterDao;
import com.yaroslavgorbachh.counter.data.Daos.CounterHistoryDao;
import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RepoImp implements Repo{
    private final CounterDao mCounterDao;
    private final CounterHistoryDao mCounterHistoryDao;
    private final AppStyleDao mAppStyleDao;

    public RepoImp(CounterDatabase database) {
        mCounterHistoryDao = database.counterHistoryDao();
        mCounterDao = database.counterDao();
        mAppStyleDao = database.appStyleDao();
    }

    public AppStyle getCurrentStyle() {
        return mAppStyleDao.getCurrentColor();
    }

    public void insertCounter(Counter counter) {
        Completable.create(emitter -> mCounterDao.insert(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounter(Counter counter) {
        Completable.create(emitter ->
                mCounterDao.delete(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateCounter(Counter counter) {
        Completable.create(emitter -> mCounterDao.update(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounters() {
        Completable.create(emitter -> mCounterDao.deleteAllCounters())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void insertCounterHistory(CounterHistory counterHistory) {
        Completable.create(emitter -> mCounterHistoryDao.insert(counterHistory))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounterHistory(long counterId) {
        Completable.create(emitter -> mCounterHistoryDao.delete(counterId))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Single<List<Counter>> getAllCountersNoLiveData() {
        return Single.create(emitter -> emitter.onSuccess(mCounterDao.getAllCountersNoLiveData()));
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId) {
        return mCounterHistoryDao.getCounterHistoryList(counterId);
    }

    public LiveData<List<Counter>> getAllCounters() {
        return mCounterDao.getAllCounters();
    }

    public LiveData<Counter> getCounter(long id) {
        return mCounterDao.getCounter(id);
    }

    public Single<Counter> getCounterWidget(long widgetId) {
        return Single.create(emitter -> emitter.onSuccess(mCounterDao.getCounterWidget(widgetId)));
    }

    public Single<Counter> getCounterNoLiveData(long id) {
        return Single.create(emitter -> emitter.onSuccess(mCounterDao.getCounterNoLiveData(id)));
    }

    public LiveData<List<String>> getGroups() {
        return mCounterDao.getGroups();
    }

    public void changeTheme(AppStyle style) {
        Completable.create(emitter -> mAppStyleDao.update(style))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteHistoryItem(CounterHistory counterHistory) {
        Completable.create(emitter ->
                mCounterHistoryDao.delete(counterHistory))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void incCounter(long mId) {
        mCounterDao.inc(mId);
    }

    public void decCounter(long mId) {
        mCounterDao.dec(mId);
    }

    public void resetCounter(long mId) {
        mCounterDao.reset(mId);
    }

    public void deleteCounter(long mId) {

    }
}





















