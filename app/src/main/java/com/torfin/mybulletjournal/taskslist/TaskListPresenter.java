package com.torfin.mybulletjournal.taskslist;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskListPresenter implements TaskListContract.Presenter, TasksProvider.TaskAdded {

    public static final String TAG = TaskListPresenter.class.getSimpleName();

    private TasksProvider provider;

    private TaskListContract.View view;

    private SimpleDateFormat dateFormat;

    private SimpleDateFormat calendarDateFormat;

    private SimpleDateFormat timeFormat;

    private int numOfDaysDifference = 0;

    static TaskListPresenter newInstance(Context context) {
        return new TaskListPresenter(context);
    }

    private TaskListPresenter(Context context) {
        provider = TasksProvider.getInstance(context, this);
        dateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY", Locale.getDefault());
        calendarDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    }

    @Override
    public void getTasks() {
        new RetrieveTasks().execute();
    }

    @Override
    public void getTasksWithDate() {
        this.view.showLoading();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long time = DateUtils.addDays(calendar.getTime(), numOfDaysDifference);
        calendar.setTimeInMillis(time);
        long dayOne = calendar.getTimeInMillis();

        Log.d(TAG, "DATE ONE: " + dateFormat.format(calendar.getTime()) + " @ " + timeFormat.format(calendar.getTime()));
        Log.d(TAG, "DATE ONE: " + calendar.getTime());

        long nextDay = DateUtils.addDays(calendar.getTime(), 1);
        calendar.setTimeInMillis(nextDay);
        long dayTwo = calendar.getTimeInMillis();

        Log.d(TAG, "DATE TWO: " + dateFormat.format(calendar.getTime()) + " @ " + timeFormat.format(calendar.getTime()));
        Log.d(TAG, "DATE TWO: " + calendar.getTime());

        new RetrieveTasksWithDate().execute(dayOne, dayTwo);
    }

    @Override
    public void subscribe(TaskListContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(TaskListContract.View v) {
        if (v == this.view) {
            this.view = null;
        }
    }

    @Override
    public void setDate() {
        long time = DateUtils.addDays(Calendar.getInstance().getTime(), numOfDaysDifference);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        this.view.setDate(DateUtils.formatDate(calendar.getTime(), dateFormat));
    }

    @Override
    public void onDateButtonPressed(int counter) {
        numOfDaysDifference += counter;
        setDate();
    }

    @Override
    public void updateTasksList() {
        this.view.updateTasks();
    }

    @Override
    public ArrayList<Task> getListOfTasks(HashMap<String, Task> tasks) {
        return provider.convertMapToList(tasks);
    }

    @Override
    public String getDate(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return calendarDateFormat.format(calendar.getTime());
    }

    @Override
    public String getTime(long date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return timeFormat.format(calendar.getTime());
    }

    @Override
    public void taskAdded() {
        updateTasksList();
    }

    private class RetrieveTasks extends AsyncTask<Void, Void, HashMap<String, Task>> {

        @Override
        protected HashMap<String, Task> doInBackground(Void... params) {
            return provider.getTasks();
        }

        @Override
        protected void onPostExecute(HashMap<String, Task> tasks) {
            super.onPostExecute(tasks);

            view.hideLoading();

            if (tasks == null || tasks.size() == 0) {
                view.showNoTasks();
            } else {
                view.showTasksList(tasks);
            }
        }
    }

    private class RetrieveTasksWithDate extends AsyncTask<Long, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Long... params) {
            Long dateOne = params[0];
            Long dateTwo = params[1];
            return provider.getTasksWithDate(dateOne, dateTwo);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);

            view.hideLoading();

            if (tasks == null || tasks.size() == 0) {
                view.showNoTasks();
            } else {
                view.showTasksList(provider.convertListToMap(tasks));
            }
        }
    }
}
