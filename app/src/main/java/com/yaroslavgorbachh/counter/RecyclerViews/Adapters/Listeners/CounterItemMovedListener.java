package com.yaroslavgorbachh.counter.RecyclerViews.Adapters.Listeners;

import com.yaroslavgorbachh.counter.Database.Models.Counter;

public interface CounterItemMovedListener {
    void onMove(Counter counterFrom, Counter counterTo);
}
