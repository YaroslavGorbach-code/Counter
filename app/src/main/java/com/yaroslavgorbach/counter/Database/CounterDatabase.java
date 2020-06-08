package com.yaroslavgorbach.counter.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yaroslavgorbach.counter.Database.Daos.CounterDao;
import com.yaroslavgorbach.counter.Database.Daos.CounterHistoryDao;
import com.yaroslavgorbach.counter.Models.Counter;
import com.yaroslavgorbach.counter.Models.CounterHistory;
import com.yaroslavgorbach.counter.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Database(entities = {Counter.class, CounterHistory.class },  version = 24)
@TypeConverters({Converters.class})
public abstract class CounterDatabase extends RoomDatabase {

    private static CounterDatabase sInstance;

    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao CounterHistoryDao();


    public static synchronized CounterDatabase getInstance(Context context){

        if (sInstance == null){

            sInstance = Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(rdc)
                    .build();
        }

        return sInstance;

    }


    private static RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            new PopulateAsyncTask(sInstance).execute();
        }

        public void onOpen(SupportSQLiteDatabase db) {
            // do something every time database is open
        }
    };


    private static class PopulateAsyncTask extends AsyncTask<Void, Void, Void>{
        private CounterDao mDao;
        PopulateAsyncTask(CounterDatabase db){
            mDao = db.counterDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Date currentDate = new Date();
            currentDate.getTime();

            mDao.insert(new Counter("New counter",0, Long.parseLong("9999999999999999"), Long.parseLong("-9999999999999999"),
                    1, null, currentDate ));
            return null;
        }
    }

}
