package com.yaroslavgorbachh.counter.database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yaroslavgorbachh.counter.database.Models.Counter;

import java.util.List;

@Dao
public interface CounterDao {

    @Insert
    void insert(Counter counter);

    @Update
    void update(Counter counter);

    @Delete
    void delete(Counter counter);

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

    @Query("SELECT * FROM counter_table WHERE widgetId = :widgetId")
    LiveData<Counter> getCounterWidgetLiveData(long widgetId);

}
