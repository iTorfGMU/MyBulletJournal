package com.torfin.mybulletjournal.taskdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by torftorf1 on 12/25/17.
 */

public class TaskDetailsActivity extends AppCompatActivity implements TaskDetailsContract.View, View.OnClickListener{

    @BindView(R.id.task_details_toolbar)
    Toolbar toolbar;

    @BindView(R.id.details_task_name_textview)
    TextView taskNameTextView;

    @BindView(R.id.details_task_status_icon)
    ImageView taskStatusIcon;

    @BindView(R.id.details_task_status_textview)
    TextView taskStatusTextView;

    @BindView(R.id.update_task_status_spinner)
    AppCompatSpinner updateStatusSpinner;

    @BindView(R.id.task_details_date_textview)
    TextView taskDateTextView;

    @BindView(R.id.task_details_label_textview)
    TextView taskLabelTextView;

    @BindView(R.id.update_task_button)
    Button updateTaskButton;

    @BindView(R.id.delete_task_button)
    Button deleteTaskButton;

    @BindView(R.id.task_details_progress_bar_container)
    LinearLayout loadingContainer;

    private static final String taskKey = "TASK_UID";

    private TaskDetailsPresenter presenter;

    private Task selectedTask;

    public static void start(Context context, String taskId) {
        Intent intent = new Intent(context, TaskDetailsActivity.class);
        intent.putExtra(taskKey, taskId);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_details);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        presenter = TaskDetailsPresenter.newInstance(this);
        presenter.subscribe(this);

        selectedTask = presenter.getTask(getIntent().getStringExtra(taskKey) == null ? "" : getIntent().getStringExtra(taskKey));

        updateTaskButton.setOnClickListener(this);
        deleteTaskButton.setOnClickListener(this);

        if (selectedTask != null) {
            taskNameTextView.setText(selectedTask.taskName);
            taskStatusIcon.setImageResource(presenter.getTaskTypeResId(selectedTask.taskTypeId));
            taskStatusTextView.setText(selectedTask.taskType);
            taskDateTextView.setText(presenter.getTaskDate(selectedTask.taskDate));
            taskLabelTextView.setText(selectedTask.taskLabel);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe(this);
        presenter = null;

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_details_menu, menu);

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
            case R.id.action_edit:
                presenter.onEditSelected(selectedTask);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

        int viewId = v.getId();

        switch (viewId) {
            case R.id.update_task_button:
                presenter.onUpdateTaskSelected((String)updateStatusSpinner.getSelectedItem());
                break;
            case R.id.delete_task_button:
                presenter.onDeleteTaskSelected();
                break;
            default:
                break;
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
    public void setupEditView() {
        taskStatusTextView.setVisibility(View.GONE);
        updateStatusSpinner.setVisibility(View.VISIBLE);

        updateTaskButton.setVisibility(View.VISIBLE);
        deleteTaskButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEditView() {
        taskStatusTextView.setVisibility(View.VISIBLE);
        updateStatusSpinner.setVisibility(View.GONE);

        updateTaskButton.setVisibility(View.GONE);
        deleteTaskButton.setVisibility(View.GONE);
    }

    @Override
    public void setupDeleteTaskViews() {
        deleteTaskButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDeleteTask() {
        deleteTaskButton.setVisibility(View.GONE);
    }

    @Override
    public void updateStatus(Task updatedTask) {
        selectedTask = updatedTask;
        taskStatusTextView.setText(updatedTask.taskType);
        taskStatusIcon.setImageResource(presenter.getTaskTypeResId(updatedTask.taskTypeId));
    }

    @Override
    public void onError(@StringRes int resId) {
        showMessage(resId);
    }

    @Override
    public void onError(String message) {
        showMessage(message);
    }

    @Override
    public void showMessage(String message) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(@StringRes int resId) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, resId, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void dismiss() {
        finish();
    }
}
