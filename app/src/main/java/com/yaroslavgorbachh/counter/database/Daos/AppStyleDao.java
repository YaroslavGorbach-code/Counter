package com.yaroslavgorbachh.counter.database.Daos;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.yaroslavgorbachh.counter.database.Models.AppStyle;

@Dao
public interface AppStyleDao {
        @Insert
        void insert(AppStyle style);

        @Update
        void update(AppStyle style);

        @Query("SELECT * FROM app_style")
        AppStyle getCurrentColor();

}
