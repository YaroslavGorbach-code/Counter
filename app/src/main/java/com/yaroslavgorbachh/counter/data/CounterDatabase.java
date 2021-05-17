package com.yaroslavgorbachh.counter.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.yaroslavgorbachh.counter.data.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.data.Daos.CounterDao;
import com.yaroslavgorbachh.counter.data.Daos.CounterHistoryDao;
import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.CounterHistory;

@Database(entities = {Counter.class, CounterHistory.class, AppStyle.class},  version = 27)
@TypeConverters({Converters.class})
public abstract class CounterDatabase extends RoomDatabase {
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao counterHistoryDao();
    public abstract AppStyleDao appStyleDao();
}

