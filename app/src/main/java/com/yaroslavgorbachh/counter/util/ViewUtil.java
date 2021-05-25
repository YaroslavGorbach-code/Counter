package com.yaroslavgorbachh.counter.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;

import androidx.annotation.ColorInt;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yaroslavgorbachh.counter.R;
import com.yaroslavgorbachh.counter.data.Domain.Counter;
import com.yaroslavgorbachh.counter.screen.settings.SettingsActivity;

public class ViewUtil {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

    public static boolean valueFilter(TextInputEditText value, TextInputEditText min, TextInputEditText max) {
        /*if value is or - empty show error*/
        if (String.valueOf(value.getText()).trim().isEmpty() ||
                String.valueOf(value.getText()).matches("-")) {
            value.setError(value.getContext().getString(R.string.valueError));
            value.setText(null);
            return false;
        } else if (max.getText()!=null
                && Long.valueOf(max.getText().toString()) < Long.valueOf(value.getText().toString())){
            value.setError(value.getContext().getString(R.string.ValueBiggerMaxError));

            return false;

        }else if (min.getText()!=null
                && Long.valueOf(min.getText().toString()) > Long.valueOf(value.getText().toString())){
            value.setError(value.getContext().getString(R.string.ValueLeesMin));

            return false;
        }
        return true;
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
        } else {
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
            return mGroups_et.getText().toString();
        }
    }

    public static boolean maxAndMinValueFilter(TextInputEditText max, TextInputEditText min) {
        if (max.getText()!=null
                && min.getText()!=null
                && !max.getText().toString().trim().isEmpty()
                && !min.getText().toString().trim().isEmpty()){
            if (Long.valueOf(min.getText().toString()) > Long.valueOf(max.getText().toString())){
                min.setError(min.getContext().getString(R.string.minValueError));
                return false;
            }else if (Long.valueOf(max.getText().toString()) < Long.valueOf(min.getText().toString())){
                max.setError(min.getContext().getString(R.string.maxValueError));
                return false;
            }
        }
       return true;
    }
}
