package com.torfin.mybulletjournal.newtask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TimePicker;

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
import java.util.UUID;

public class AddNewTaskPresenter implements AddNewTaskContract.Presenter, ProfileUtils.VerifyListener, TasksProvider.TaskAdded,
        DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener, TaskLabelProvider.LabelsCallback{

    private AddNewTaskContract.View view;

    private TasksProvider tasksProvider;

    private TaskLabelProvider labelProvider;

    private Date selectedDate;

    private SimpleDateFormat dateFormat;

    private SimpleDateFormat timeFormat;

    private Context context;

    static AddNewTaskPresenter newInstance(Context c) {
        return new AddNewTaskPresenter(c);
    }

    private AddNewTaskPresenter(Context c) {
        this.context = c;
        tasksProvider = TasksProvider.getInstance(c, this);
        labelProvider = TaskLabelProvider.getInstance(this);
        dateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY", Locale.getDefault());
        timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        selectedDate = Calendar.getInstance().getTime();
    }

    @Override
    public void checkToken() {
        ProfileUtils.verifyToken(this);
    }

    @Override
    public List<String> getLabels() {
        List<String> labels = new ArrayList<>();
        labels.add("Choose a task label");

        for (String label : labelProvider.getLabels()) {
            labels.add(label);
        }

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
        if (name == null || name.length() == 0) {
            this.view.showMessage("Task Name is Required.");
            return false;
        }

        if (label == null || label.length() == 0 || label.equals("Choose a task label")) {
            this.view.showMessage("Task Label is Required.");
            return false;
        }

        if (type == null || type.length() == 0 || type.equals("Choose a task type")) {
            this.view.showMessage("Task Type is Required.");
            return false;
        }

        return true;
    }

    @Override
    public void createTask(@NonNull String name, @NonNull String type, int typeId, @NonNull String label) {
        this.view.showLoading();

        long date = -1;

        if (selectedDate != null) {
            date = selectedDate.getTime();
        }
        // Creating a random UUID (Universally unique identifier).
        UUID uuid = UUID.randomUUID();
        String uuidString = uuid.toString();

        Task task = new Task(uuidString, name, type, date, typeId, label);
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
        this.view.hideTimePicker();
        this.view.showDatePicker();
    }

    @Override
    public void handleTimePicker() {
        this.view.hideDatePicker();
        this.view.showTimePicker();
    }

    @Override
    public void dismissDatePicker() {
        this.view.hideDatePicker();
    }

    @Override
    public void dismissTimePicker() {
        this.view.hideTimePicker();
    }

    @Override
    public void successful() {
        //do nothing
    }

    @Override
    public void error() {
        ProfileUtils.signout();
        this.view.signout();
    }

    @Override
    public void taskAdded() {
        this.view.hideLoading();
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, monthOfYear, dayOfMonth);
        selectedDate = calendar.getTime();

        this.view.hideDatePicker();
        this.view.onDatePicked(DateUtils.formatDate(selectedDate, dateFormat));
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
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

            view.hideLoading();
            view.showMessage("Task Successfully Added");
            view.dismiss();
        }
    }
}
