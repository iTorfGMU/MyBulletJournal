package com.torfin.mybulletjournal.monthlylog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.View;
import android.widget.CalendarView;

import java.util.Calendar;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class MonthlyLogPresenter implements MonthlyLogContract.Presenter, CalendarView.OnDateChangeListener, View.OnClickListener {

    private Context context;

    private MonthlyLogContract.View view;

    private long selectedDate;

    private Resubscribe callback;

    public static MonthlyLogPresenter newInstance(Context c, Resubscribe callback) {
        return new MonthlyLogPresenter(c, callback);
    }

    private MonthlyLogPresenter(Context c, Resubscribe callback) {
        this.context = c;

        this.callback = callback;

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //set default value to today
        selectedDate = calendar.getTimeInMillis();
    }

    @Override
    public void subscribe(MonthlyLogContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(MonthlyLogContract.View v) {
        if (v.equals(this.view)) {
            this.view = null;
        }
    }

    @Override
    public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.set(year, month, dayOfMonth);

        selectedDate = calendar.getTimeInMillis();
    }

    @Override
    public void onClick(View v) {
        checkView();

        this.view.showTaskListActivity(selectedDate);
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public void setSelectedDate(long day) {
        selectedDate = day;
    }

    private void checkView() {
        if (this.view == null) {
            callback.resubscribeView();
        }
    }

    public interface Resubscribe {
        void resubscribeView();
    }
}
