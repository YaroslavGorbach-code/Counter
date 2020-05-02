package com.yaroslavgorbach.counter;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CounterDao {

    @Insert
    void insert(Counter counter);

    @Update
    void update(Counter counter);

    @Delete
    void delete(Counter counter);

    @Query("SELECT * FROM counter_table ORDER BY 'DESC'")
    LiveData<List<Counter>> getAllCounters();

    @Query("SELECT * FROM counter_table WHERE id = :id")
    LiveData<Counter> getCounter(long id);
}
