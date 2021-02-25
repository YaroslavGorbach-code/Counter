package com.yaroslavgorbachh.counter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatDelegate;
import com.yaroslavgorbachh.counter.Database.Models.Counter;
import com.yaroslavgorbachh.counter.Database.Repo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public class Utility {
    /*delete the same groups*/
    static public List<String> deleteTheSameGroups(List<String> strings) {
        Set<String> set = new HashSet<>(strings);
        List<String> mList = Arrays.asList(set.toArray(new String[0]));
        Collections.sort(mList);
        return mList;
    }

    public void setTheme(SharedPreferences sharedPreferences, Activity activity) {
        new Thread(() -> {
            Repo repo = new Repo(activity.getApplication());
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

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String convertDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy HH:mm:ss", Locale.getDefault());
        return  dateFormat.format(date);
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy", Locale.getDefault());
        return dateFormat.format(new Date());
    }


    /*method for changing the font size depending on the counter value*/
    public static int getValueTvSize(Counter counter) {
        switch (String.valueOf(counter.value).length()) {
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

    public static Intent getShareCountersInCSVIntent(List<Counter> list){
        StringBuilder textToSend = new StringBuilder();
        Collections.reverse(list);
        for (Counter counter : list) {
            textToSend.append(counter.title).append(": ").append(counter.value).append("\n");
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend.toString());
        sendIntent.setType("text/plain");
        return Intent.createChooser(sendIntent, null);
    }

    public static int fetchAccentColor(Context context) {
        TypedValue typedValue = new TypedValue();
        TypedArray a = context.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorAccent });
        int color = a.getColor(0, 0);
        a.recycle();
        return color;
    }

}
