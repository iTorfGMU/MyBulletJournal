package com.torfin.mybulletjournal.newtask;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.TaskTypeIds;
import com.torfin.mybulletjournal.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddNewTaskActivity extends AppCompatActivity implements AddNewTaskContract.View, View.OnClickListener {

    @BindView(R.id.add_tasks_toolbar)
    Toolbar toolbar;

    @BindView(R.id.taskname_edittext_view)
    EditText taskNameEditView;

    @BindView(R.id.tasklabel_spinner_view)
    Spinner taskLabelSpinner;

    @BindView(R.id.tasktype_spinner_view)
    Spinner taskTypeSpinner;

    @BindView(R.id.taskdate_textview)
    TextView taskDateTextView;

    @BindView(R.id.task_time_textview)
    TextView taskTimeTextView;

    @BindView(R.id.tasktime_textview)
    TextView taskSelectTimeTextView;

    @BindView(R.id.date_picker_container)
    LinearLayout datePickerContainer;

    @BindView(R.id.task_date_picker)
    DatePicker datePicker;

    @BindView(R.id.time_picker_container)
    LinearLayout timePickerContainer;

    @BindView(R.id.task_time_picker)
    TimePicker timePicker;

    @BindView(R.id.add_task_progress_bar_container)
    LinearLayout loadingContainer;

    @BindView(R.id.add_new_task_button)
    Button addNewTaskButton;

    private AddNewTaskPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, AddNewTaskActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.presenter = AddNewTaskPresenter.newInstance(this);

        taskDateTextView.setOnClickListener(this);
        taskSelectTimeTextView.setOnClickListener(this);
        addNewTaskButton.setOnClickListener(this);

        datePickerContainer.setOnClickListener(this);
        if (Build.VERSION.SDK_INT >= 26) {
            datePicker.setOnDateChangedListener(presenter);
        }

        timePickerContainer.setOnClickListener(this);
        timePicker.setOnTimeChangedListener(presenter);

        if (!isNetworkConnected()) {
            showMessage("It appears you are not connected to a network.");
        }

        openActivityOnTokenExpire();

        ArrayAdapter<String> labelArrayAdapter = presenter.getLabelArrayAdapter();
        labelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        taskLabelSpinner.setAdapter(labelArrayAdapter);
        taskLabelSpinner.setSelection(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.presenter.subscribe(this);
    }

    @Override
    protected void onDestroy() {
        this.presenter.unsubscribe(this);
        this.presenter = null;
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_signout:
                FirebaseAuth.getInstance().signOut();
                LoginActivity.start(this);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void showLoading() {
        loadingContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingContainer.setVisibility(View.GONE);
    }

    @Override
    public void openActivityOnTokenExpire() {
        presenter.checkToken();
    }

    @Override
    public void showMessage(String message) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void signout() {
        LoginActivity.start(this);
        finish();
    }

    @Override
    public void refreshLabelSpinner() {
        ArrayAdapter<String> labelArrayAdapter = presenter.getLabelArrayAdapter();
        labelArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        taskLabelSpinner.setAdapter(labelArrayAdapter);
        taskLabelSpinner.setSelection(0);
    }

    @Override
    public void showDatePicker() {
        datePickerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDatePicker() {
        datePickerContainer.setVisibility(View.GONE);
    }

    @Override
    public void showTimePicker() {
        timePickerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTimePicker() {
        timePickerContainer.setVisibility(View.GONE);
    }

    @Override
    public void onDatePicked(String date) {
        taskDateTextView.setText(date);

        taskTimeTextView.setVisibility(View.VISIBLE);
        taskSelectTimeTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTimePicked(String date) {
        taskSelectTimeTextView.setText(date);
    }

    @Override
    public void dismiss() {
        finish();
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.add_new_task_button:
                onAddTaskButtonClicked();
                break;
            case R.id.taskdate_textview:
                presenter.handleDatePicker();
                break;
            case R.id.tasktime_textview:
                presenter.handleTimePicker();
                break;
            case R.id.date_picker_container:
                presenter.dismissDatePicker();
                break;
            case R.id.time_picker_container:
                presenter.dismissTimePicker();
                break;
            default:
                break;
        }
    }

    private void onAddTaskButtonClicked() {
        boolean isCorrectForm = presenter.verifyForm(
                taskNameEditView.getText().toString(),
                taskTypeSpinner.getSelectedItem().toString(),
                taskLabelSpinner.getSelectedItem().toString());
        if (isCorrectForm) {
            presenter.createTask(
                    taskNameEditView.getText().toString(),
                    taskTypeSpinner.getSelectedItem().toString(),
                    TaskTypeIds.getTaskTypeId(taskTypeSpinner.getSelectedItem().toString(), this),
                    taskLabelSpinner.getSelectedItem().toString());
        }
    }
}
