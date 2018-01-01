package com.torfin.mybulletjournal;

import com.torfin.mybulletjournal.utils.DateUtils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by torftorf1 on 1/1/18.
 */

@RunWith(JUnit4.class)
public class DateUtilsUnitTest {

    private SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

    @Test
    public void test_addDays() {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.DATE, 1);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        assertNotNull(DateUtils.addDays(Calendar.getInstance().getTime(), 1));
        assertEquals(DateUtils.addDays(calendar1.getTime(), 1), calendar.getTimeInMillis());
    }

    @Test
    public void test_FormatDate() {
        String formattedDate = DateUtils.formatDate(Calendar.getInstance().getTime(), format);
        assertNotNull(formattedDate);
        assertEquals(format.format(Calendar.getInstance().getTime()), formattedDate);
    }

    @Test
    public void test_FormatDate_Long() {
        String formattedDate = DateUtils.formatDate(Calendar.getInstance().getTimeInMillis(), format);
        assertNotNull(formattedDate);
        assertEquals(format.format(Calendar.getInstance().getTimeInMillis()), formattedDate);
    }

    @Test
    public void test_FormatDate_Default() {
        String formattedDate = DateUtils.formatDate(Calendar.getInstance().getTimeInMillis());
        assertNotNull(formattedDate);
        assertEquals(format.format(Calendar.getInstance().getTime()), formattedDate);
    }

    @Test
    public void test_GetMonth() {
        int month = DateUtils.getMonth(Calendar.getInstance().getTimeInMillis());
        assertNotNull(month);
        assertEquals(Calendar.getInstance().get(Calendar.MONTH), month);
    }

    @Test
    public void test_GetDate() {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date date = DateUtils.getDate(calendar.getTimeInMillis());
        assertNotNull(date);
        assertEquals(calendar.getTime(), date);
    }
}
