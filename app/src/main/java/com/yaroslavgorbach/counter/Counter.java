package com.yaroslavgorbach.counter;

import android.graphics.Color;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "counter_table")
public class Counter {

    @PrimaryKey(autoGenerate = true)
    public long id;

    public String title;
    public int value;
    public int maxValue;
    public int minValue;
    public int step;
    public String grope;


    public Counter( String title, int value, int maxValue, int minValue, int step, String grope) {
        this.title = title;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.step = step;
        this.grope = grope;
    }



    public void setId(long id) {

        this.id = id;
    }
}
