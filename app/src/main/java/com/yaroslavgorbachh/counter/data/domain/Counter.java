package com.yaroslavgorbachh.counter.data.domain;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "counter_table")
public class Counter {
    public final static long MAX_VALUE = 999999999999999999L;
    public final static long MIN_VALUE = -999999999999999999L;

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String title;
    public long value;
    public long maxValue;
    public long minValue;
    public long step;
    public Integer colorId;
    public String grope;
    @ColumnInfo(name = "createData")
    public Date createDate;
    @ColumnInfo(name = "createDataSort")
    public Date createDateSort;
    @ColumnInfo(name = "lastResetData")
    public Date lastResetDate;
    @ColumnInfo(defaultValue = "0")
    public long lastResetValue;
    @ColumnInfo(defaultValue = "0")
    public long counterMaxValue;
    @ColumnInfo(defaultValue = "0")
    public long counterMinValue;
    public Integer widgetId;

    public Counter(String title,
                   long value,
                   long maxValue,
                   long minValue,
                   long step,
                   int colorId,
                   String grope,
                   Date createDate,
                   Date createDateSort,
                   Date lastResetDate,
                   long lastResetValue,
                   long counterMaxValue,
                   long counterMinValue,
                   Integer widgetId) {
        this.title = title;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.step = step;
        this.colorId = colorId;
        this.grope = grope;
        this.createDate = createDate;
        this.createDateSort = createDateSort;
        this.lastResetDate = lastResetDate;
        this.lastResetValue = lastResetValue;
        this.counterMaxValue = counterMaxValue;
        this.counterMinValue = counterMinValue;
        this.widgetId = widgetId;
    }

    public void setId(long id) {
        this.id = id;
    }

}
