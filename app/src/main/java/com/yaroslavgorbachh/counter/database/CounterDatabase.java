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

import javax.inject.Inject;

@Database(entities = {Counter.class, CounterHistory.class, AppStyle.class},  version = 26)
@TypeConverters({Converters.class})
public abstract class CounterDatabase extends RoomDatabase {
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao counterHistoryDao();
    public abstract AppStyleDao appStyleDao();

}

