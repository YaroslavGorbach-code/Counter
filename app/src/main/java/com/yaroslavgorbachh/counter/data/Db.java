package com.yaroslavgorbachh.counter.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.yaroslavgorbachh.counter.data.Daos.AppStyleDao;
import com.yaroslavgorbachh.counter.data.Daos.CounterDao;
import com.yaroslavgorbachh.counter.data.Daos.CounterHistoryDao;
import com.yaroslavgorbachh.counter.data.Models.AppStyle;
import com.yaroslavgorbachh.counter.data.Models.Counter;
import com.yaroslavgorbachh.counter.data.Models.History;

import java.util.Date;

@Database(entities = {Counter.class, History.class, AppStyle.class},  version = 27)
@TypeConverters({Db.Converters.class})
public abstract class Db extends RoomDatabase {
    public abstract CounterDao counterDao();
    public abstract CounterHistoryDao counterHistoryDao();
    public abstract AppStyleDao appStyleDao();

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

