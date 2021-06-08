package com.yaroslavgorbachh.counter.data.local.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.yaroslavgorbachh.counter.data.domain.History;

import java.util.List;

@Dao
public interface CounterHistoryDao {

    @Insert
    void insert(History History);

    @Query("DELETE FROM counterHistory_table WHERE counterId = :counterId")
    void deleteCounterHistory(long counterId);

    @Query("DELETE FROM counterHistory_table WHERE id = :historyId")
    void delete(long historyId);


    @Query("SELECT * FROM counterHistory_table WHERE counterId = :counterId ORDER BY id DESC")
    LiveData<List<History>> getHistoryList(long counterId );

}
