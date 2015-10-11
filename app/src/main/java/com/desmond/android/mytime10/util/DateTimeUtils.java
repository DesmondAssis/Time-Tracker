package com.desmond.android.mytime10.util;

import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gk on 15/9/28.
 */
public class DateTimeUtils {
    private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");

    public static int getWeekOfYear(DatePicker datePicker) {
        Calendar c = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getWeekOfYear(String currentDateStr, boolean isChangeTodos) {
        Calendar c = new GregorianCalendar();
        if(isChangeTodos) {
            try {
               Date date = sdf.parse(currentDateStr);
                c.setTime(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return c.get(Calendar.YEAR) + "-" + c.get(Calendar.WEEK_OF_YEAR);
    }

    public static String getWeekOfYear() {
        Calendar c = new GregorianCalendar();
        return c.get(Calendar.YEAR) + "-" + c.get(Calendar.WEEK_OF_YEAR);
    }

    public static int getWeekOfYear(int i) {
        Calendar c = new GregorianCalendar();

        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * yyyy-M-d
     * @return
     */
    public static String getCurrentDateStr() {
        return sdf.format(new Date());
    }

    public static String getCurrentDateStr(String currentDateStr, boolean isChangeTods) {
        Date date = new Date();
        if(isChangeTods) {
            try {
                date = sdf.parse(currentDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return sdf.format(date);
    }

    public static String getPreOrNextDateStr(String currentDateStr, boolean isPre) {
        try {
            Date date = sdf.parse(currentDateStr);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            int count = isPre ? -1 : 1;
            c.add(Calendar.DAY_OF_MONTH, count);

            return sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return currentDateStr;
    }

    public static String getCurrentMonthStr(String currentDateStr, boolean isChangeTods) {
        Date date = new Date();
        if(isChangeTods) {
            try {
                date = sdf.parse(currentDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return new SimpleDateFormat("yyyy-M").format(date);
    }

    public static String getCurrentMonthStr() {
        return new SimpleDateFormat("yyyy-M").format(new Date());
    }

    public static String getCurrentYearStr() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH~mm").format(new Date());
    }
}
