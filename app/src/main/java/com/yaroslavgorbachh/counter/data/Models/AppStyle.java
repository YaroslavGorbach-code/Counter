package com.yaroslavgorbachh.counter.data.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "app_style")
public class AppStyle {

    @PrimaryKey
    public long id;

    @ColumnInfo(defaultValue = "0")
    public int style;

    public AppStyle(long id, int style) {
        this.style = style;
        this.id = id;
    }


}
