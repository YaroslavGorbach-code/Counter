package com.yaroslavgorbachh.counter.data.local.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.yaroslavgorbachh.counter.data.domain.Counter;
import com.yaroslavgorbachh.counter.data.domain.History;


import java.util.Date;

@Database(entities = {Counter.class, History.class},  version = 29)
@TypeConverters({RoomDb.Converters.class})
public abstract class RoomDb extends RoomDatabase {
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

