package com.yaroslavgorbachh.counter.component.settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.lifecycle.LiveData;

import com.yaroslavgorbachh.counter.data.Domain.Counter;

import java.util.List;

public interface Settings {
    interface ResetCallback{
        void onReset(List<Counter> copy);
    }

    void backup(Intent data, Context context);
    void restore(Intent data, Context context);
    void deleteAll();
    void resetAll(ResetCallback callback);
    List<Counter> getAll();
    void changeNightMod(boolean b);
    void undoReset(List<Counter> copy);
}
