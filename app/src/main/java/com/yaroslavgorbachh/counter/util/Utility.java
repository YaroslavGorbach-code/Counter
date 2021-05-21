package com.yaroslavgorbachh.counter.util;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utility {

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
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

}
