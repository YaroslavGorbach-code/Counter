package com.yaroslavgorbachh.counter.data.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.yaroslavgorbachh.counter.data.Models.History;

import java.util.List;

@Dao
public interface CounterHistoryDao {

    @Insert
    void insert(History History);

    @Query("DELETE FROM counterHistory_table WHERE counterId = :counterId")
    void delete(long counterId);

    @Delete
    void delete(History history);

    @Query("SELECT * FROM counterHistory_table WHERE counterId = :counterId ORDER BY id DESC")
    LiveData<List<History>> getCounterHistoryList(long counterId );

}
