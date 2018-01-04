package com.torfin.mybulletjournal.taskslist;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;
import android.support.v4.widget.SwipeRefreshLayout;

import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskListPresenter implements TaskListContract.Presenter, TasksProvider.TaskAdded, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG = TaskListPresenter.class.getSimpleName();

    private TasksProvider provider;

    private TaskListContract.View view;

    private SimpleDateFormat dateFormat;

    private SimpleDateFormat calendarDateFormat;

    private SimpleDateFormat timeFormat;

    private int numOfDaysDifference = 0;

    private boolean allTasks;

    private long dateToDisplay = -1;

    private Resubscribe callback;

    public static final String DISPLAY_ALL_TASKS = "all_tasks";

    public static final String DISPLAY_DATE_KEY = "date_to_display";

    public static TaskListPresenter newInstance(Context context) {
        return newInstance(context, null);
    }

    public static TaskListPresenter newInstance(Context context, Resubscribe callback) {
        return new TaskListPresenter(context, callback);
    }

    private TaskListPresenter(Context context, Resubscribe callback) {
        this.callback = callback;
        provider = TasksProvider.getInstance(context, this);
        dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        calendarDateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    }

    @Override
    public void getTasks() {
        checkView();

        allTasks = true;

        this.view.showLoading();
        new RetrieveTasks().execute();
    }

    @Override
    public void getTasksWithDate() {

        Calendar calendar = Calendar.getInstance();

        long time = DateUtils.addDays(calendar.getTime(), numOfDaysDifference);

        getTasksWithDate(time);
    }

    @Override
    public void getTasksWithDate(long date) {
        checkView();

        allTasks = false;

        this.view.showLoading();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        calendar.setTimeInMillis(date);

        this.view.setDate(DateUtils.formatDate(calendar.getTime(), dateFormat));

        long dayOne = calendar.getTimeInMillis();

        dateToDisplay = dayOne;

        long nextDay = DateUtils.addDays(calendar.getTime(), 1);
        calendar.setTimeInMillis(nextDay);
        long dayTwo = calendar.getTimeInMillis();


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
        checkView();

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
        checkView();

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
    public long getCurrentlyDisplayedDate() {
        return dateToDisplay;
    }

    @Override
    public boolean showAllTasks() {
        return allTasks;
    }

    @Override
    public void onGetTasksComplete(HashMap<String, Task> tasks) {
        checkView();

        view.setAdapter(tasks);

        view.hideLoading();

        if (tasks == null || tasks.size() == 0) {
            view.showNoTasks();
        } else {
            view.showTasksList(tasks);
        }
    }

    @Override
    public void onGetTasksByDateComplete(List<Task> list) {
        checkView();

        HashMap<String, Task> map = provider.convertListToMap(list);

        view.setAdapter(map);
        view.hideLoading();

        if (list == null || list.size() == 0) {
            view.showNoTasks();
        } else {
            view.showTasksList(map);
        }
    }

    @Override
    public void taskAdded() {
        updateTasksList();
    }

    @Override
    public void onRefresh() {
        checkView();

        this.view.refreshAdapter(false);
        this.view.showLoading();

        if (allTasks) {
            getTasks();
        } else {
            getTasksWithDate();
        }
    }

    public class RetrieveTasks extends AsyncTask<Void, Void, HashMap<String, Task>> {

        @Override
        protected HashMap<String, Task> doInBackground(Void... params) {
            return provider.getTasks();
        }

        @Override
        protected void onPostExecute(HashMap<String, Task> tasks) {
            super.onPostExecute(tasks);

            onGetTasksComplete(tasks);
        }
    }

    public class RetrieveTasksWithDate extends AsyncTask<Long, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Long... params) {
            Long dateOne = params[0];
            Long dateTwo = params[1];
            return provider.getTasksWithDate(dateOne, dateTwo);
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            super.onPostExecute(tasks);

            onGetTasksByDateComplete(tasks);
        }
    }

    private void checkView() {
        if (this.view == null && callback != null) {
            callback.resubscribeView();
        }
    }

    public interface Resubscribe {
        void resubscribeView();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public TasksProvider getProvider() {
        return this.provider;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public TaskListContract.View getView() {
        return this.view;
    }

}
