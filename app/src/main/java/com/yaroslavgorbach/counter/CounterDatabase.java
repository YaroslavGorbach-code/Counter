package com.yaroslavgorbach.counter;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = Counter.class, version = 1)
public abstract class CounterDatabase extends RoomDatabase {

    private static CounterDatabase sInstance;
    public abstract CounterDao counterDao();

    public static synchronized CounterDatabase getInstance(Context context){

        if (sInstance == null){

            sInstance = Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }

        return sInstance;

    }


    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {

            super.onCreate(db);
            new PopulateDbAsyncTask(sInstance).execute();
        }

    };

    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private CounterDao counterDao;

        private PopulateDbAsyncTask(CounterDatabase db) {

            this.counterDao = db.counterDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            counterDao.insert(new Counter("test",1, 10, 22, 1, "test"));
            counterDao.insert(new Counter("test",1, 10, 22, 1, "test"));
            counterDao.insert(new Counter("test",1, 10, 22, 1, "test"));
            counterDao.insert(new Counter("test",1, 10, 22, 1, "test"));
            counterDao.insert(new Counter("test",1, 10, 22, 1, "test"));

            return null;
        }
    }
}
