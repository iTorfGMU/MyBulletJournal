package com.torfin.mybulletjournal.utils;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class DateUtils {

    public static long addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days); //minus number would decrement the days
        return cal.getTimeInMillis();
    }

    public static String formatDate(Date date, SimpleDateFormat format) {
        return format.format(date);
    }

}
