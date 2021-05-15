package com.yaroslavgorbachh.counter.screen.counterSettings.animations;

import android.app.Activity;
import android.content.Intent;

import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.screen.counterSettings.SettingsActivity;

public class AnimateThemeChange {
    public static void animate(Activity activity){
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        activity.startActivity(new Intent(activity, SettingsActivity.class));
    }
}
