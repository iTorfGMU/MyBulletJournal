package com.torfin.mybulletjournal.futurelog;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.VisibleForTesting;

import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class FutureLogPresenter implements FutureLogContract.Presenter, TasksProvider.TaskAdded {

    private FutureLogContract.View view;

    private TasksProvider provider;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM yyyy", Locale.getDefault());

    private int currentMonth = Calendar.getInstance().get(Calendar.MONTH);

    private List<Object> log;

    private Resubscribe callback;

    public static FutureLogPresenter newInstance(Context c, Resubscribe callback) {
        return new FutureLogPresenter(c, callback);
    }

    private FutureLogPresenter(Context c, Resubscribe callback) {
        provider = TasksProvider.getInstance(c, this);
        this.callback = callback;
    }

    @Override
    public void subscribe(FutureLogContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(FutureLogContract.View v) {
        if (v.equals(this.view)) {
            this.view = null;
        }
    }

    @Override
    public void configureRecyclerView() {
        this.view.showLoading();

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        new GetFutureTasks().execute(calendar.getTimeInMillis());
    }

    @Override
    public void onGetFutureTasksComplete(List<Task> list) {

        Collections.sort(list, new Comparator<Task>(){
            public int compare(Task o1, Task o2){
                return DateUtils.getDate(o1.taskDate).compareTo(DateUtils.getDate(o2.taskDate));
            }
        });

        List<Object> logList = new ArrayList<>();
        logList.add(DateUtils.formatDate(Calendar.getInstance().getTimeInMillis(), dateFormat));

        for (Task task : list) {
            if (DateUtils.getMonth(task.taskDate) != currentMonth) {
                currentMonth = DateUtils.getMonth(task.taskDate);

                logList.add(DateUtils.formatDate(task.taskDate, dateFormat));
                logList.add(task);
            } else {
                logList.add(task);
            }
        }

        setLogObjects(logList);

        view.hideLoading();
        view.setRecyclerView(logList);
    }

    @Override
    public void taskAdded() {

    }

    private class GetFutureTasks extends AsyncTask<Long, Void, List<Task>> {

        @Override
        protected List<Task> doInBackground(Long... params) {
            long startDate = params[0];
            return provider.getFutureTasks(startDate);
        }

        @Override
        protected void onPostExecute(List<Task> list) {
            super.onPostExecute(list);

            onGetFutureTasksComplete(list);
        }
    }

    private void checkView() {
        if (this.view == null) {
            callback.resubscribeView();
        }
    }

    public interface Resubscribe {
        void resubscribeView();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public FutureLogContract.View getView() {
        return this.view;
    }

    private void setLogObjects(List<Object> list) {
        this.log = list;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public List<Object> getLog() {
        return this.log;
    }
}
