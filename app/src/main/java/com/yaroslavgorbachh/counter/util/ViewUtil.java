package com.yaroslavgorbachh.counter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.ColorInt;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;

public class ViewUtil {
    public static void animate(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }
    public static int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        @ColorInt int color = typedValue.data;
        return color;
    }

}
