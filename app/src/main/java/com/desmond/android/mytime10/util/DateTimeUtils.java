package com.desmond.android.mytime10.util;

import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gk on 15/9/28.
 */
public class DateTimeUtils {

    public static int getWeekOfYear(DatePicker datePicker) {
        Calendar c = new GregorianCalendar(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());

        return c.get(Calendar.WEEK_OF_YEAR);
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
        return new SimpleDateFormat("yyyy-M-d").format(new Date());
    }

    public static String getCurrentMonthStr() {
        return new SimpleDateFormat("yyyy-M").format(new Date());
    }

    public static String getCurrentYearStr() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

}
