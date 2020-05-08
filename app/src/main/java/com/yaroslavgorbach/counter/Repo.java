package com.yaroslavgorbach.counter;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Repo {

   private CounterDao mCounterDao;

   private LiveData<List<Counter>> mAll_counters;
   private LiveData<List<String>> mGroups;



    public Repo(Application application){

       CounterDatabase database = CounterDatabase.getInstance(application);
        mCounterDao = database.counterDao();
        mAll_counters = mCounterDao.getAllCounters();
        mGroups = mCounterDao.getGroups();


   }

    public void insert(Counter counter){

     new InsertCounterAsyncTask(mCounterDao).execute(counter);
    }

    public void delete(Counter counter){

       new DeleteCounterAsyncTask(mCounterDao).execute(counter);
    }

    public void update(Counter counter){

       new UpdateCounterAsyncTask(mCounterDao).execute(counter);
    }

    public LiveData<List<Counter>> getAllCounters(){

       return mAll_counters;
    }

    public LiveData<Counter> getCounter(long id) {

        return mCounterDao.getCounter(id);

    }

    public LiveData<List<String>> getGroups(){

        return mGroups;
    }




    public static class InsertCounterAsyncTask extends AsyncTask<Counter, Void, Void>{

        private CounterDao counterDao;

        private InsertCounterAsyncTask(CounterDao counterDao) {

            this.counterDao = counterDao;

        }

        @Override
        protected Void doInBackground(Counter... counters) {

            counterDao.insert(counters[0]);
            return null;
        }
    }

    public static class DeleteCounterAsyncTask extends AsyncTask<Counter, Void, Void>{

        private CounterDao counterDao;

        private DeleteCounterAsyncTask(CounterDao counterDao) {

            this.counterDao = counterDao;

        }

        @Override
        protected Void doInBackground(Counter... counters) {

            counterDao.delete(counters[0]);
            return null;
        }
    }

    public static class UpdateCounterAsyncTask extends AsyncTask<Counter, Void, Void>{

        private CounterDao counterDao;

        private UpdateCounterAsyncTask(CounterDao counterDao) {

            this.counterDao = counterDao;

        }

        @Override
        protected Void doInBackground(Counter... counters) {

            counterDao.update(counters[0]);
            return null;
        }
    }






}





















