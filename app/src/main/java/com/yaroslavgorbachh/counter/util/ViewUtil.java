package com.yaroslavgorbachh.counter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.AutoCompleteTextView;

import androidx.annotation.ColorInt;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Models.Counter;
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

    public static int getCounterTvSize(long value) {
        switch (String.valueOf(value).length()) {
            case 1:
            case 2:
                return 150;
            case 3:
                return 130;
            case 4:
                return 120;
            case 5:
                return 110;
            case 6:
                return 100;
            case 7:
                return 90;
            case 8:
                return 80;
            case 9:
                return 70;
            case 10:
            case 11:
                return 60;
            case 12:
            case 13:
                return 50;
            case 14:
            case 17:
            case 15:
            case 16:
            case 18:
            case 19:
                return 40;
        }
        return 0;
    }
    public static boolean valueFilter(TextInputEditText mValue_et) {
        /*if value is or - empty show error*/
        if (String.valueOf(mValue_et.getText()).trim().isEmpty() ||
                String.valueOf(mValue_et.getText()).matches("-") ) {
            mValue_et.setError(mValue_et.getContext().getString(R.string.valueError));
            mValue_et.setText(null);
            return false;
        } else {
            return true;
        }
    }

    public static boolean titleFilter(TextInputEditText mTitle_et) {
        /*if title is empty show error*/
        if (String.valueOf(mTitle_et.getText()).trim().isEmpty()) {
            mTitle_et.setError(mTitle_et.getContext().getString(R.string.titleError));
            return false;
        } else {
            return true;
        }
    }

    public static boolean stepFilter(TextInputEditText mStep_et) {
        /*if step is empty or 0 show error*/
        if (String.valueOf(mStep_et.getText()).trim().isEmpty()
                || String.valueOf(mStep_et.getText()).matches("0+")
                || String.valueOf(mStep_et.getText()).matches("-")) {
            mStep_et.setError(mStep_et.getContext().getString(R.string.stepError));
            mStep_et.setText(null);
            return false;
        } else {
            return true;
        }
    }

    public static String maxValueFilter(TextInputEditText mMaxValue_et) {
        /*if maxValue is empty set default value if is not set value from editText*/
        if (String.valueOf(mMaxValue_et.getText()).trim().isEmpty()
                || String.valueOf(mMaxValue_et.getText()).matches("-")) {
            return String.valueOf(Counter.MAX_VALUE);
        }else {
            return String.valueOf(mMaxValue_et.getText());
        }
    }

    public static String minValueFilter(TextInputEditText mMinValue_et) {
        /*if minValue is empty set default value if is not set value from editText*/
        if (String.valueOf(mMinValue_et.getText()).trim().isEmpty()
                || String.valueOf(mMinValue_et.getText()).matches("-")) {
            return String.valueOf(Counter.MIN_VALUE);
        } else {
            return String.valueOf(mMinValue_et.getText());
        }
    }

    public static String groupsFilter(AutoCompleteTextView mGroups_et) {
        /*if group is empty set no group*/
        if (mGroups_et.getText().toString().trim().isEmpty()) {
            return null;
        } else {
            return  mGroups_et.getText().toString();
        }
    }

}
