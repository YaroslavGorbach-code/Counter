package com.yaroslavgorbach.counter.RecyclerViews.Adapters.Listeners;

import com.yaroslavgorbach.counter.Database.Models.Counter;

public interface CounterItemMovedListener {
    void onMove(Counter counterFrom, Counter counterTo);
}
