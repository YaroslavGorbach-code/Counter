package com.yaroslavgorbachh.counter.di;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.preference.PreferenceManager;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.database.CounterDatabase;
import com.yaroslavgorbachh.counter.database.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.database.Daos.CounterDao;
import com.yaroslavgorbachh.counter.database.Models.AppStyle;
import com.yaroslavgorbachh.counter.database.Models.Counter;


import java.util.Date;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class RoomModule {

    @Provides
    public CounterDatabase provideDatabase(Context context){
        return Room.databaseBuilder(context.getApplicationContext(),CounterDatabase.class, "counter.db")
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
//                                super.onCreate(db);
//                                ContentValues cv = new ContentValues();
//                                cv.put("title", "New counter");
//                                cv.put("value", 0);
//                                cv.put("maxValue", Counter.MAX_VALUE);
//                                cv.put("minValue", Counter.MIN_VALUE);
//                                cv.put("step", 1);
//                                cv.put("grope", (String) null);
//                                cv.put("createData", new Date().getTime());
//                                cv.put("createDataSort", new Date().getTime());
//                                cv.put("lastResetData", (String) null);
//                                cv.put("lastResetValue", 0);
//                                cv.put("counterMaxValue", 0);
//                                cv.put("counterMinValue", 0);
//                                db.insert("counter_table", SQLiteDatabase.CONFLICT_REPLACE, cv);
                            }
                        })
                        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                        .addMigrations(MIGRATION_24_25)
                        .addMigrations(MIGRATION_25_26)
                        .build();
            }



    private static final Migration MIGRATION_24_25 = new Migration(24, 25) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE counter_table ADD COLUMN createDataSort INTEGER DEFAULT null ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetData INTEGER DEFAULT null");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN lastResetValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMaxValue INTEGER NOT NULL DEFAULT 0 ");
            database.execSQL("ALTER TABLE counter_table ADD COLUMN counterMinValue INTEGER NOT NULL DEFAULT 0 ");
        }
    };

    private static final Migration MIGRATION_25_26 = new Migration(25, 26) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE app_style (" +
                    "id INTEGER PRIMARY KEY NOT NULL," +
                    "style INTEGER NOT NULL DEFAULT 0) ");
            database.execSQL("INSERT INTO app_style (id, style) VALUES(1, 0)");
        }
    };


//    public static final RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
//        public void onCreate(SupportSQLiteDatabase db) {
//            new Thread(() -> {
//
//                Date currentDate = new Date();
//                currentDate.getTime();
//
//                counterDao.insert(new Counter("New counter",0, Counter.MAX_VALUE,
//                        Counter.MIN_VALUE,
//                        1, null, currentDate, currentDate, null,
//                        0, 0, 0 ));
//                appStyleDao.insert(new AppStyle(1, R.style.AppTheme));
//            }).start();
//        }
//
//        public void onOpen(SupportSQLiteDatabase db) {
//            // do something every time database is open
//        }
//    };


}
