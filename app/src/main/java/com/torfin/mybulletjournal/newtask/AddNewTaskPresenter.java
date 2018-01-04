package com.torfin.mybulletjournal.newtask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.utils.DateUtils;
import com.torfin.mybulletjournal.utils.ProfileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddNewTaskPresenter implements AddNewTaskContract.Presenter, ProfileUtils.VerifyListener, TasksProvider.TaskAdded,
        DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener, TaskLabelProvider.LabelsCallback {

    private AddNewTaskContract.View view;

    private TasksProvider tasksProvider;

    private TaskLabelProvider labelProvider;

    private Date selectedDate;

    private boolean timeSelected;

    private SimpleDateFormat dateFormat;

    private SimpleDateFormat timeFormat;

    private Context context;

    private Resubscribe callback;

    public static final String TASK_NAME_KEY = "task_name";

    public static final String TASK_TYPE_KEY = "task_label";

    public static final String TASK_LABEL_KEY = "task_type";

    public static final String TASK_DATE_KEY = "task_date";

    public static final String TASK_TIME_KEY = "task_time";

    public static AddNewTaskPresenter newInstance(Context c) {
        return newInstance(c, null);
    }

    public static AddNewTaskPresenter newInstance(Context c, Resubscribe callback) {
        return new AddNewTaskPresenter(c, callback);
    }

    private AddNewTaskPresenter(Context c, Resubscribe callback) {
        this.context = c;
        this.callback = callback;
        tasksProvider = TasksProvider.getInstance(c, this);
        labelProvider = TaskLabelProvider.getInstance(this);
        dateFormat = new SimpleDateFormat("EEEE, MMM dd, yyyy", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    }

    @Override
    public void checkToken() {
        ProfileUtils.verifyToken(this);
    }

    @Override
    public List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        labels.add(context.getString(R.string.first_label_option));
        labels.addAll(labelProvider.getLabels());
        return labels;
    }

    @Override
    public ArrayAdapter<String> getLabelArrayAdapter() {
        return new ArrayAdapter<>(
                this.context,
                android.R.layout.simple_spinner_item,
                getLabels()
        );
    }

    @Override
    public boolean verifyForm(String name, String type, String label) {
        checkView();
        if (name == null || name.length() == 0) {
            this.view.showMessage(R.string.snackbar_task_name_required);
            return false;
        }

        if (label == null || label.length() == 0 || label.equals("Choose a task label")) {
            this.view.showMessage(R.string.snackbar_task_label_required);
            return false;
        }

        if (type == null || type.length() == 0 || type.equals("Choose a task type")) {
            this.view.showMessage(R.string.snackbar_task_type_required);
            return false;
        }

        return true;
    }

    @Override
    public void createTask(@NonNull String name, @NonNull String type, int typeId, @NonNull String label) {
        checkView();
        this.view.showLoading();

        long date = -1;

        if (selectedDate != null) {
            date = selectedDate.getTime();
        } else {
            date = Calendar.getInstance().getTimeInMillis();
        }

        Task task = new Task("", name, type, date, typeId, label);
        new AddTask().execute(task);
    }

    @Override
    public void subscribe(AddNewTaskContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(AddNewTaskContract.View v) {
        if (v.equals(this.view)) {
            this.view = null;
        }
    }

    @Override
    public void handleDatePicker() {
        checkView();
        this.view.hideTimePicker();
        this.view.showDatePicker();
    }

    @Override
    public void handleTimePicker() {
        checkView();
        this.view.hideDatePicker();
        this.view.showTimePicker();
    }

    @Override
    public void dismissDatePicker() {
        checkView();
        this.view.hideDatePicker();
    }

    @Override
    public void dismissTimePicker() {
        checkView();
        this.view.hideTimePicker();
    }

    @Override
    public void onCreateTaskComplete() {
        checkView();
        this.view.hideLoading();
        this.view.showMessage(R.string.snackbar_task_added);
        this.view.dismiss();
    }

    @Override
    public void setDate() {
        checkView();
        selectedDate = this.view.getSelectedDate();

        this.view.hideDatePicker();
        this.view.onDatePicked(DateUtils.formatDate(selectedDate, dateFormat));
    }

    @Override
    public long getDate() {
        long date = -1;

        if (selectedDate != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(selectedDate);
            date = calendar.getTimeInMillis();
        }

        return date;
    }

    @Override
    public boolean wasTimeSelected() {
        return timeSelected;
    }

    @Override
    public void handleRotation(String name, long date, boolean selectedTime) {
        checkView();
        this.view.setTaskName(name);
        timeSelected = selectedTime;

        if (date > 0){
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);

            selectedDate = calendar.getTime();

            this.view.onDatePicked(DateUtils.formatDate(selectedDate, dateFormat));

            if (timeSelected) {
                this.view.onTimePicked(DateUtils.formatDate(selectedDate, timeFormat));
            }
        }
    }

    @Override
    public int getLabelPosition(String label) {
        return labelProvider.getLabels().indexOf(label);
    }

    @Override
    public void successful() {
        //do nothing
    }

    @Override
    public void error() {
        checkView();
        ProfileUtils.signout();
        this.view.signout();
    }

    @Override
    public void taskAdded() {
        this.view.hideLoading();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        checkView();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        selectedDate = calendar.getTime();

        this.view.hideDatePicker();
        this.view.onDatePicked(DateUtils.formatDate(selectedDate, dateFormat));
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        checkView();
        timeSelected = true;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        selectedDate = calendar.getTime();

        this.view.onTimePicked(DateUtils.formatDate(selectedDate, timeFormat));
    }

    @Override
    public void labelsReturned() {
        this.view.refreshLabelSpinner();
    }

    private class AddTask extends AsyncTask<Task, Void, Void> {

        @Override
        protected Void doInBackground(Task... params) {
            Task task = params[0];
            tasksProvider.addTask(task);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            onCreateTaskComplete();
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
    public AddNewTaskContract.View getView() {
        return this.view;
    }
}
