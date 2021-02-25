package com.yaroslavgorbachh.counter.Database.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.yaroslavgorbachh.counter.Database.Models.CounterHistory;

import java.util.List;

@Dao
public interface CounterHistoryDao {

    @Insert
    void insert(CounterHistory CounterHistory);

    @Query("DELETE FROM counterHistory_table WHERE counterId = :counterId")
    void delete(long counterId);

    @Query("SELECT * FROM counterHistory_table WHERE counterId = :counterId ORDER BY id DESC")
    LiveData<List<CounterHistory>> getCounterHistoryList(long counterId );

}
