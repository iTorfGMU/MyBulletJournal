package com.torfin.mybulletjournal.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.utils.DateUtils;
import com.torfin.mybulletjournal.utils.WidgetTaskUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class TasksWidgetService extends RemoteViewsService {

    int appWidgetId;

    TasksProvider provider;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        if (isConnected) {
            //get tasks
            new GetTasks().execute();
        }

        return new ListTasksViewsFactory(this.getApplicationContext(), intent);
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(TasksWidgetProvider.DATA_FETCHED);
        widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        sendBroadcast(widgetUpdateIntent);
    }

    class GetTasks extends AsyncTask<Void, Void, List<Task>> implements TasksProvider.TaskAdded {

        @Override
        protected List<Task> doInBackground(Void... params) {
            provider = TasksProvider.getInstance(getApplicationContext(), this);

            Calendar calendar = Calendar.getInstance();

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            long dayOne = calendar.getTimeInMillis();

            long nextDay = DateUtils.addDays(calendar.getTime(), 1);
            calendar.setTimeInMillis(nextDay);
            long dayTwo = calendar.getTimeInMillis();

            return provider.getTasksWithDate(dayOne, dayTwo);
        }

        @Override
        protected void onPostExecute(List<Task> list) {
            super.onPostExecute(list);

            WidgetTaskUtils.setTasks(list);
            populateWidget();
        }

        @Override
        public void taskAdded() {

        }
    }
}

class ListTasksViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = ListTasksViewsFactory.class.getSimpleName();

    private Context context;

    private int appWidgetId;

    static List<Task> tasks = new ArrayList<>();

    public ListTasksViewsFactory(Context c, Intent intent) {
        this.context = c;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        if (WidgetTaskUtils.getTasks() != null) {
            tasks = WidgetTaskUtils.getTasks();
        }

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Task task = tasks.get(position);

        final RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.widget_list_row);

        remoteView.setImageViewResource(R.id.widget_task_icon, WidgetTaskUtils.getTaskImage(task));
        remoteView.setTextViewText(R.id.widget_task_name, task.taskName);

        return remoteView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
