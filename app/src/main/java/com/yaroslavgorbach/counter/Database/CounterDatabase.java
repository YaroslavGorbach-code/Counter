package com.yaroslavgorbach.counter.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yaroslavgorbach.counter.Database.Daos.CounterDao;
import com.yaroslavgorbach.counter.Database.Daos.CounterHistoryDao;
import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.Database.Models.CounterHistory;

import java.util.Date;

@Database(entities = {Counter.class, CounterHistory.class },  version = 24)
@TypeConverters({Converters.class})
public abstract class CounterDatabase extends RoomDatabase {
    private static CounterDatabase sInstance;
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao CounterHistoryDao();

    public static synchronized CounterDatabase getInstance(Context context){
        if (sInstance == null){
            sInstance = Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                    .addCallback(rdc)
                    .build();
        }
        return sInstance;
    }

    private static final RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            CounterDao mDao;
            Date currentDate = new Date();
            mDao = sInstance.counterDao();
            currentDate.getTime();
            new Thread(() -> {
                mDao.insert(new Counter("New counter",0, Long.parseLong("9999999999999999"),
                        Long.parseLong("-9999999999999999"),
                        1, null, currentDate ));
            }).start();
        }

        public void onOpen(SupportSQLiteDatabase db) {
            // do something every time database is open
        }
    };

}
