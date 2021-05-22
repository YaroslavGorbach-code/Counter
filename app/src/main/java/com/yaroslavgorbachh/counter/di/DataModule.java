package com.yaroslavgorbachh.counter.di;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.local.Db;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Repo;
import com.yaroslavgorbachh.counter.data.RepoImp;


import java.util.Date;

import dagger.Module;
import dagger.Provides;

import static com.yaroslavgorbachh.counter.data.local.Migrations.MIGRATION_24_25;
import static com.yaroslavgorbachh.counter.data.local.Migrations.MIGRATION_25_26;
import static com.yaroslavgorbachh.counter.data.local.Migrations.MIGRATION_26_27;
import static com.yaroslavgorbachh.counter.data.local.Migrations.MIGRATION_27_28;

@Module
public class DataModule {

    @Provides
    public Db provideDatabase(Context context){
        return Room.databaseBuilder(context.getApplicationContext(), Db.class, "counter.db")
                        .addCallback(new RoomDatabase.Callback() {
                            @Override
                            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                ContentValues cv = new ContentValues();
                                cv.put("title", "New counter");
                                cv.put("value", 0);
                                cv.put("maxValue", Counter.MAX_VALUE);
                                cv.put("minValue", Counter.MIN_VALUE);
                                cv.put("step", 1);
                                cv.put("grope", (String) null);
                                cv.put("createData", new Date().getTime());
                                cv.put("createDataSort", new Date().getTime());
                                cv.put("lastResetData", (String) null);
                                cv.put("lastResetValue", 0);
                                cv.put("counterMaxValue", 0);
                                cv.put("counterMinValue", 0);
                                db.insert("counter_table", SQLiteDatabase.CONFLICT_REPLACE, cv);
                                cv.clear();

                                cv.put("id", 1);
                                cv.put("style", R.style.AppTheme);
                                db.insert("app_style", SQLiteDatabase.CONFLICT_REPLACE, cv);

                            }
                        })
                        .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
                        .allowMainThreadQueries()
                        .addMigrations(MIGRATION_24_25, MIGRATION_25_26, MIGRATION_26_27, MIGRATION_27_28)
                        .build();
            }

    @Provides
    public Repo provideRepo(Db database){
       return new RepoImp(database);
    }
}
