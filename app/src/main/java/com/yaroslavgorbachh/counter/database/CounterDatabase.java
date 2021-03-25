package com.yaroslavgorbachh.counter.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yaroslavgorbachh.counter.database.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterHistoryDao;
import com.yaroslavgorbachh.counter.database.Models.AppStyle;
import com.yaroslavgorbachh.counter.database.Models.Counter;
import com.yaroslavgorbachh.counter.database.Models.CounterHistory;
import com.yaroslavgorbachh.counter.R;

import java.util.Date;

@Database(entities = {Counter.class, CounterHistory.class, AppStyle.class},  version = 26)
@TypeConverters({Converters.class})
public abstract class CounterDatabase extends RoomDatabase {
    private static CounterDatabase sInstance;
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao counterHistoryDao();
    public abstract AppStyleDao appStyleDao();

    public static synchronized CounterDatabase getInstance(Context context){

        if (sInstance == null){
            sInstance = Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                    .addCallback(rdc)
                    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                    .addMigrations(MIGRATION_24_25)
                    .addMigrations(MIGRATION_25_26)
                    .build();
        }
        return sInstance;

    }


    static final Migration MIGRATION_24_25 = new Migration(24, 25) {

        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counter_table ADD COLUMN createDataSort INTEGER DEFAULT null ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetData INTEGER DEFAULT null");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMaxValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMinValue INTEGER NOT NULL DEFAULT 0 ");
        }
    };

    static final Migration MIGRATION_25_26 = new Migration(25, 26) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE app_style (" +
                    "id INTEGER PRIMARY KEY NOT NULL," +
                    "style INTEGER NOT NULL DEFAULT 0) ");
            database.execSQL("INSERT INTO app_style (id, style) VALUES(1, 0)");
        }
    };


    private static final RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
        public void onCreate(SupportSQLiteDatabase db) {
            new Thread(() -> {
                CounterDao counterDao;
                AppStyleDao appStyleDao;
                counterDao = sInstance.counterDao();
                appStyleDao = sInstance.appStyleDao();
                Date currentDate = new Date();
                currentDate.getTime();

                counterDao.insert(new Counter("New counter",0, Counter.MAX_VALUE,
                        Counter.MIN_VALUE,
                        1, null, currentDate, currentDate, null,
                        0, 0, 0 ));
                appStyleDao.insert(new AppStyle(1, R.style.AppTheme));
            }).start();
        }

        public void onOpen(SupportSQLiteDatabase db) {
            // do something every time database is open
        }
    };

}
