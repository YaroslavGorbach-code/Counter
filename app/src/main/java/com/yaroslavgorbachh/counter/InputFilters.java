package com.yaroslavgorbachh.counter;

import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputEditText;
import com.yaroslavgorbachh.counter.data.Models.Counter;

public class InputFilters {

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

    public static long maxValueFilter(TextInputEditText mMaxValue_et) {
        /*if maxValue is empty set default value if is not set value from editText*/
        if (String.valueOf(mMaxValue_et.getText()).trim().isEmpty()
                || String.valueOf(mMaxValue_et.getText()).matches("-")) {
            return Counter.MAX_VALUE;
        }else {
            return Long.parseLong(String.valueOf(mMaxValue_et.getText()));
        }
    }

    public static long minValue(TextInputEditText mMinValue_et) {
        /*if minValue is empty set default value if is not set value from editText*/
        if (String.valueOf(mMinValue_et.getText()).trim().isEmpty()
                || String.valueOf(mMinValue_et.getText()).matches("-")) {
            return Counter.MIN_VALUE;
        } else {
            return Long.parseLong(String.valueOf(mMinValue_et.getText()));
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
