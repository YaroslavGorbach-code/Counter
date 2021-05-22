package com.yaroslavgorbachh.counter.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.data.Domain.History;
import com.yaroslavgorbachh.counter.data.local.dao.CounterDao;
import com.yaroslavgorbachh.counter.data.local.dao.CounterHistoryDao;


import java.util.Date;

@Database(entities = {Counter.class, History.class},  version = 28)
@TypeConverters({Db.Converters.class})
public abstract class Db extends RoomDatabase {
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao counterHistoryDao();

    public static class Converters {
        @TypeConverter
        public static Date fromTimestamp(Long value) {
            return value == null ? null : new Date(value);
        }

        @TypeConverter
        public static Long dateToTimestamp(Date date) {
            return date == null ? null : date.getTime();
        }
    }
}

