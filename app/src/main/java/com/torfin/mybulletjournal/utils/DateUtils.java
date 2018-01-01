package com.torfin.mybulletjournal.utils;


import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class DateUtils {

    private static SimpleDateFormat defaultFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    public static long addDays(@NonNull Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTimeInMillis();
    }

    public static String formatDate(@NonNull Date date, @NonNull  SimpleDateFormat format) {
        return format.format(date);
    }

    public static String formatDate(long date, @NonNull SimpleDateFormat format) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        return format.format(cal.getTime());
    }

    public static String formatDate(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        return defaultFormat.format(cal.getTime());
    }

    public static int getMonth(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        return cal.get(Calendar.MONTH);
    }

    public static Date getDate(long date) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date);

        return cal.getTime();
    }

}
