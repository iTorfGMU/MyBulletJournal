package com.torfin.mybulletjournal.taskslist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.dataobjects.Task;
import com.torfin.mybulletjournal.futurelog.FutureLogActivity;
import com.torfin.mybulletjournal.login.LoginActivity;
import com.torfin.mybulletjournal.monthlylog.MonthlyLogActivity;
import com.torfin.mybulletjournal.newtask.AddNewTaskActivity;
import com.torfin.mybulletjournal.taskslist.recyclerview.TasksRecyclerViewAdapter;
import com.torfin.mybulletjournal.viewlabels.ViewLabelsActivity;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TasksListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        TaskListContract.View, View.OnClickListener {

    @BindView(R.id.fab)
    FloatingActionButton fab;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.date_text_view)
    TextView currentDateTextView;

    @BindView(R.id.previous_date_button)
    ImageButton previousDateButton;

    @BindView(R.id.next_date_button)
    ImageButton nextDateButton;

    @BindView(R.id.tasks_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.tasks_recycler_view)
    RecyclerView tasksRecyclerView;

    @BindView(R.id.tasks_loading_icon)
    ProgressBar loadingIcon;

    @BindView(R.id.no_tasks_textview)
    TextView noTasksTextView;

    public static final String TAG = TasksListActivity.class.getSimpleName();

    public static final String DATE_KEY = "date_key";

    private TaskListPresenter presenter;

    private TasksRecyclerViewAdapter adapter;

    private boolean showDateTasks;

    private long dateInMilliseconds;

    public static void start(Context context) {
        Intent intent = new Intent(context, TasksListActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, long date) {
        Intent intent = new Intent(context, TasksListActivity.class);
        intent.putExtra(DATE_KEY, date);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        presenter = TaskListPresenter.newInstance(this);
        presenter.subscribe(this);

        final Context context = this;

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTaskActivity.start(context);
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        tasksRecyclerView.setHasFixedSize(true);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        nextDateButton.setOnClickListener(this);
        previousDateButton.setOnClickListener(this);

        //check if called from monthly log
        dateInMilliseconds = getIntent().getLongExtra(DATE_KEY, 0);
        if (dateInMilliseconds != 0) {
            showDateTasks = true;
        }

        refreshLayout.setOnRefreshListener(presenter);

        if (savedInstanceState != null) {
            showDateTasks = !savedInstanceState.getBoolean(TaskListPresenter.DISPLAY_ALL_TASKS, false);
            dateInMilliseconds = savedInstanceState.getLong(TaskListPresenter.DISPLAY_DATE_KEY, 0);
        }

        if (!showDateTasks) {
            currentDateTextView.setText(R.string.all_tasks_header);
            presenter.getTasks();
        } else {
            presenter.getTasksWithDate(dateInMilliseconds);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        setOutState(outState);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        setOutState(outState);
        super.onSaveInstanceState(outState);
    }

    private void setOutState(Bundle bundle) {
        bundle.putBoolean(TaskListPresenter.DISPLAY_ALL_TASKS, presenter.showAllTasks());
        bundle.putLong(TaskListPresenter.DISPLAY_DATE_KEY, presenter.getCurrentlyDisplayedDate());
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe(this);
        presenter = null;

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_signout) {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.start(this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.monthly_log:
                MonthlyLogActivity.start(this);
                break;
            case R.id.future_log:
                FutureLogActivity.start(this);
                break;
            case R.id.view_labels:
                ViewLabelsActivity.start(this);
                break;
            case R.id.nav_sign_out:
                FirebaseAuth.getInstance().signOut();
                LoginActivity.start(this);
                finish();
                break;
            default:
                break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showLoading() {
        loadingIcon.setVisibility(View.VISIBLE);
        tasksRecyclerView.setVisibility(View.GONE);
        noTasksTextView.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        loadingIcon.setVisibility(View.GONE);
    }

    @Override
    public void showNoTasks() {
        tasksRecyclerView.setVisibility(View.GONE);
        noTasksTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTasksList(HashMap<String, Task> tasks) {
        noTasksTextView.setVisibility(View.GONE);
        tasksRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void updateTasks() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setDate(String date) {
        currentDateTextView.setText(date);
    }

    @Override
    public void refreshAdapter(boolean setRefreshing) {
        refreshLayout.setRefreshing(setRefreshing);
    }

    @Override
    public void updateList() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setAdapter(HashMap<String, Task> tasks) {
        adapter = new TasksRecyclerViewAdapter(presenter.getListOfTasks(tasks), presenter);
        tasksRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        switch (viewId) {
            case R.id.next_date_button:
                getTasks(1);
                break;
            case R.id.previous_date_button:
                getTasks(-1);
                break;
            default:
                break;
        }
    }

    private void getTasks(int value) {
        presenter.onDateButtonPressed(value);
        presenter.getTasksWithDate();
    }
}
