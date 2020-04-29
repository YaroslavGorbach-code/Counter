package com.yaroslavgorbach.counter;

import android.graphics.Color;

class Counter {
    public long id;
    public String title;
    public int value;
    public int maxValue;
    public int minValue;
    public int step;
    public String grope;
    public int color;

    public Counter(long id, String title, int value, int maxValue, int minVlue, int step, String grope, int color) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.maxValue = maxValue;
        this.minValue = minVlue;
        this.step = step;
        this.grope = grope;
        this.color = color;
    }
}
