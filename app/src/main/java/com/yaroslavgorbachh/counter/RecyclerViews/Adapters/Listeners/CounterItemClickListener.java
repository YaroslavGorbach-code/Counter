package com.yaroslavgorbachh.counter.RecyclerViews.Adapters.Listeners;

import com.yaroslavgorbachh.counter.Database.Models.Counter;

public interface CounterItemClickListener {
        void onPlusClick(Counter counter);
        void onMinusClick(Counter counter);
        void onOpen(Counter counter);
}
