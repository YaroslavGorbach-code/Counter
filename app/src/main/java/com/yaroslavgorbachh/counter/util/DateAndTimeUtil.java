package com.yaroslavgorbachh.counter.util;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class DateAndTimeUtil {
    public static String convertDateToString(Date date){
        DateFormat dataFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());

        return  dataFormat.format(date);
    }

    public static String getCurrentDate(){
        DateFormat dataFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());
        return dataFormat.format(new Date());
    }

}
