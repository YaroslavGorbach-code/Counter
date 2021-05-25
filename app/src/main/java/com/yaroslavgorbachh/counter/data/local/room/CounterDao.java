package com.yaroslavgorbachh.counter.data.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;

@Dao
public interface CounterDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Counter counter);

    @Update
    void update(Counter counter);

    @Query("DELETE FROM counter_table WHERE id =:id")
    void delete(long id);

    @Query("SELECT * FROM counter_table ORDER BY createDataSort DESC")
    Observable<List<Counter>> getCounters();

    @Query("DELETE FROM counter_table")
    void deleteAllCounters();

    @Query("SELECT * FROM counter_table WHERE id = :id")
    Observable<Counter> getCounter(long id);

    @Query("SELECT DISTINCT grope FROM counter_table WHERE grope<>'' ORDER BY grope ASC")
    LiveData<List<String>> getGroups();

    @Query("SELECT * FROM counter_table WHERE widgetId = :widgetId")
    Counter getCounterWidget(long widgetId);

    @Query("UPDATE counter_table SET value = value + 1 WHERE id = :id")
    void inc(long id);

    @Query("UPDATE counter_table SET value = value - 1 WHERE id = :id")
    void dec(long id);

    @Query("UPDATE counter_table SET value = 0 WHERE id = :id")
    void reset(long id);
}
