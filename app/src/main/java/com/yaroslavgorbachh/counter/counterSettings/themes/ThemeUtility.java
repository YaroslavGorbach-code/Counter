package com.yaroslavgorbachh.counter.counterSettings.themes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatDelegate;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.database.Repo;

public class ThemeUtility {
    public static int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

    public static void setTheme(SharedPreferences sharedPreferences, Activity activity, Repo repo) {
        new Thread(() -> {
            if (repo.getCurrentStyle() != null){
                activity.setTheme(repo.getCurrentStyle().style);
            }
        }).start();
        if (sharedPreferences.getBoolean("nightMod", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

}
