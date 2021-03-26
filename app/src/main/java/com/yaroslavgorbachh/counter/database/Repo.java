package com.yaroslavgorbachh.counter.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.database.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterHistoryDao;
import com.yaroslavgorbachh.counter.database.Models.AppStyle;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Single;

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
        //TODO: 2/19/2021 make in background
       return mAppStyleDao.getCurrentColor();
   }

    public void insertCounter(Counter counter){
        new Thread(() -> mCounterDao.insert(counter)).start();
    }

    public void deleteCounter(Counter counter){
        new Thread(() -> mCounterDao.delete(counter)).start();
    }

    public void updateCounter(Counter counter){
        new Thread(() -> mCounterDao.update(counter)).start();
    }

    public void deleteCounters(){
        new Thread(mCounterDao::deleteAllCounters).start();
    }

    public void insertCounterHistory(CounterHistory counterHistory){
        new Thread(() -> mCounterHistoryDao.insert(counterHistory)).start();
    }

    public void deleteCounterHistory(long counterId){
        new Thread(() -> mCounterHistoryDao.delete(counterId)).start();
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

    public LiveData<List<String>> getGroups(){
        return mCounterDao.getGroups();
    }

    public void changeTheme(AppStyle style) {
        new Thread(() -> mAppStyleDao.update(style)).start();
    }
}





















