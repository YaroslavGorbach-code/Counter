package com.yaroslavgorbachh.counter.util;

import android.content.Intent;

import com.yaroslavgorbachh.counter.data.Models.Counter;

import java.util.Collections;
import java.util.List;

public class CommonUtil {

    public static Intent getExportCSVIntent(List<Counter> list) {
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
