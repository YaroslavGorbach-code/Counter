package com.yaroslavgorbachh.counter.data.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.List;

@Dao
public interface CounterDao {

    @Insert
    void insert(Counter counter);

    @Update
    void update(Counter counter);

    @Query("DELETE FROM counter_table WHERE id =:id")
    void delete(long id);

    @Query("SELECT * FROM counter_table ORDER BY createDataSort DESC")
    LiveData<List<Counter>> getAllCounters();

    @Query("SELECT * FROM counter_table ORDER BY createDataSort DESC")
    List<Counter> getAllCountersNoLiveData();

    @Query("DELETE FROM counter_table")
    void deleteAllCounters();

    @Query("SELECT * FROM counter_table WHERE id = :id")
    LiveData<Counter> getCounter(long id);

    @Query("SELECT grope FROM counter_table WHERE grope<>'' ORDER BY grope ASC")
    LiveData<List<String>> getGroups();

    @Query("SELECT * FROM counter_table WHERE widgetId = :widgetId")
    Counter getCounterWidget(long widgetId);

    @Query("SELECT * FROM counter_table WHERE id = :id")
    Counter getCounterNoLiveData(long id);

    @Query("UPDATE counter_table SET value = value + 1 WHERE id = :id")
    void inc(long id);

    @Query("UPDATE counter_table SET value = value - 1 WHERE id = :id")
    void dec(long id);

    @Query("UPDATE counter_table SET value = 0 WHERE id = :id")
    void reset(long id);
}
