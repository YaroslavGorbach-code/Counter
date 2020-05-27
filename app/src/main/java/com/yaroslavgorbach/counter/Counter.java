package com.yaroslavgorbach.counter;

import android.graphics.Color;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "counter_table")
public class Counter {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public long value;
    public long maxValue;
    public long minValue;
    public long step;
    public String grope;
    public String createData;



    public Counter( String title, long value, long maxValue, long minValue, long step, String grope, String createData) {
        this.title = title;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.step = step;
        this.grope = grope;
        this.createData = createData;
    }



    public void setId(long id) {
        this.id = id;
    }

}
