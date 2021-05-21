package com.yaroslavgorbachh.counter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public static Date convertStringToDate(String stringDate){
        DateFormat dataFormat = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, Locale.getDefault());
        Date date = null;
        try {
            date = dataFormat.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

}
