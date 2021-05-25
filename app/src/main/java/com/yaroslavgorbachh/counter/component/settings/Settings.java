package com.yaroslavgorbachh.counter.component.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import java.util.List;

public interface Settings {
    void backup(Intent data, Context context);
    void restore(Intent data, Context context);
    void deleteAll();
    void resetAll();
    LiveData<List<Counter>> getAll();
}
