package com.yaroslavgorbachh.counter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yaroslavgorbachh.counter.Database.Models.Counter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class Utility {
    /*delete the same groups*/
    static public List<String> deleteTheSameGroups(List<String> strings) {
        Set<String> set = new HashSet<>(strings);
        List<String> mList = Arrays.asList(set.toArray(new String[0]));
        Collections.sort(mList);
        return mList;
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

    public static String formatDateToString(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.YY HH:mm:ss", Locale.getDefault());
        return  dateFormat.format(date);
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
        for (Counter counter : list) {
            textToSend.append(counter.title).append(": ").append(counter.value).append("\n");
        }
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToSend.toString());
        sendIntent.setType("text/plain");
        return Intent.createChooser(sendIntent, null);
    }

}
