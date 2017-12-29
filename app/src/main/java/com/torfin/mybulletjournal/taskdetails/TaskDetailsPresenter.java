package com.torfin.mybulletjournal.taskdetails;

import android.content.Context;
import android.os.AsyncTask;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskDetailsPresenter implements TaskDetailsContract.Presenter, TasksProvider.TaskAdded {

    private Context context;

    private TasksProvider provider;

    private TaskDetailsContract.View view;

    private Task selectedTask;

    private SimpleDateFormat dateFormat;


    public static TaskDetailsPresenter newInstance(Context c) {
        return new TaskDetailsPresenter(c);
    }

    private TaskDetailsPresenter(Context c) {
        this.context = c;
        this.provider = TasksProvider.getInstance(c, this);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy @ hh:mm a", Locale.getDefault());
    }

    @Override
    public void subscribe(TaskDetailsContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(TaskDetailsContract.View v) {
        if (v.equals(this.view)) {
            this.view = null;
        }
    }

    @Override
    public Task getTask(String id) {
        selectedTask = provider.getTasks().get(id);
        return selectedTask;
    }

    @Override
    public int getTaskTypeResId(int typeId) {
        switch (typeId) {
            case TaskTypeIds.TASK_TYPE_EVENT:
                return R.drawable.ic_event;
            case TaskTypeIds.TASK_TYPE_TASK:
                return R.drawable.ic_task;
            case TaskTypeIds.TASK_TYPE_TASK_CANCELLED:
                return R.drawable.ic_task_cancelled;
            case TaskTypeIds.TASK_TYPE_TASK_COMPLETED:
                return R.drawable.ic_task_completed;
            case TaskTypeIds.TASK_TYPE_TASK_RESCHEDULED:
                return R.drawable.ic_task_rescheduled;
            case TaskTypeIds.TASK_TYPE_TASK_SCHEDULED:
                return R.drawable.ic_task_scheduled;
            case TaskTypeIds.TASK_TYPE_DEADLINE:
                return R.drawable.ic_task_deadline;
            case TaskTypeIds.TASK_TYPE_URGENT:
                return R.drawable.ic_task_urgent;
            case TaskTypeIds.TASK_TYPE_NOTE:
                return R.drawable.ic_note;
            default:
                break;
        }

        return -1;
    }

    @Override
    public String getTaskDate(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat.format(calendar.getTime());
    }

    @Override
    public void onEditSelected(Task task) {
        if (TaskTypeIds.isATask(task.taskTypeId)) {
            this.view.setupEditView();
        } else {
            this.view.onError("Our apologies. This task is not allowed to be edited at the moment.");
        }
    }

    @Override
    public void onUpdateTaskSelected(String selectedType) {
        this.view.showLoading();
        new UpdateTask().execute(selectedType);
    }

    @Override
    public void onDeleteTaskSelected() {
        this.view.showLoading();
        new DeleteTask().execute();
    }

    @Override
    public void taskAdded() {

    }

    class UpdateTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String selectedType = params[0];

            selectedTask.taskType = selectedType;
            selectedTask.taskTypeId = TaskTypeIds.getTaskTypeId(selectedType, context);

            provider.updateTasksDatabase(selectedTask);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            view.updateStatus(selectedTask);
            view.hideEditView();
            view.hideLoading();
        }
    }

    class DeleteTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            provider.deleteTask(selectedTask);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            view.hideEditView();
            view.hideLoading();
        }
    }
}
