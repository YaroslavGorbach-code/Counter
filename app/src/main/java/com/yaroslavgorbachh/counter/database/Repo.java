package com.yaroslavgorbachh.counter.database;

import android.appwidget.AppWidgetManager;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import com.yaroslavgorbachh.counter.counterWidget.CounterWidgetProvider;
import com.yaroslavgorbachh.counter.database.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterHistoryDao;
import com.yaroslavgorbachh.counter.database.Models.AppStyle;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;


@Singleton
public class Repo {

   private final CounterDao mCounterDao;
   private final CounterHistoryDao mCounterHistoryDao;
   private final AppStyleDao mAppStyleDao;

    @Inject
    public Repo(CounterDatabase database){
       mCounterHistoryDao = database.counterHistoryDao();
       mCounterDao = database.counterDao();
       mAppStyleDao = database.appStyleDao();
   }

   public AppStyle getCurrentStyle(){
       return mAppStyleDao.getCurrentColor();
   }

    public void insertCounter(Counter counter){
        Completable.create(emitter -> mCounterDao.insert(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounter(Counter counter){
        Completable.create(emitter ->
                mCounterDao.delete(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void updateCounter(Counter counter){
        Completable.create(emitter -> mCounterDao.update(counter))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounters(){
        Completable.create(emitter -> mCounterDao.deleteAllCounters())
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void insertCounterHistory(CounterHistory counterHistory){
        Completable.create(emitter -> mCounterHistoryDao.insert(counterHistory))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public void deleteCounterHistory(long counterId){
        Completable.create(emitter -> mCounterHistoryDao.delete(counterId))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }

    public Single<List<Counter>> getAllCountersNoLiveData(){
        return Single.create(emitter -> emitter.onSuccess(mCounterDao.getAllCountersNoLiveData()));
    }

    public LiveData<List<CounterHistory>> getCounterHistoryList(long counterId){
        return mCounterHistoryDao.getCounterHistoryList(counterId);
    }

    public LiveData<List<Counter>> getAllCounters(){
       return mCounterDao.getAllCounters();
    }

    public LiveData<Counter> getCounter(long id) {
        return mCounterDao.getCounter(id);
    }

    public Counter getCounterWidget(long widgetId) {
        return mCounterDao.getCounterWidget(widgetId);
    }

    public Counter getCounterNoLiveData(long id) {
        return mCounterDao.getCounterNoLiveData(id);
    }

    public LiveData<List<String>> getGroups(){
        return mCounterDao.getGroups();
    }

    public void changeTheme(AppStyle style) {
        Completable.create(emitter -> mAppStyleDao.update(style))
                .subscribeOn(Schedulers.io())
                .subscribe();
    }


}





















