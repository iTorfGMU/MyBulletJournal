package com.torfin.mybulletjournal.monthlylog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CalendarView;

import com.google.firebase.auth.FirebaseAuth;
import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.login.LoginActivity;
import com.torfin.mybulletjournal.taskslist.TasksListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class MonthlyLogActivity extends AppCompatActivity implements MonthlyLogContract.View {

    @BindView(R.id.monthly_log_toolbar)
    Toolbar toolbar;

    @BindView(R.id.monthly_log_calendar_view)
    CalendarView calendarView;

    @BindView(R.id.view_tasks_for_date_button)
    Button viewTasksButton;

    private MonthlyLogPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, MonthlyLogActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_monthly_log);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        presenter = MonthlyLogPresenter.newInstance(this);

        presenter.subscribe(this);

        calendarView.setOnDateChangeListener(presenter);

        viewTasksButton.setOnClickListener(presenter);
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe(this);
        presenter = null;

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
    public void showTaskListActivity(long selectedDate) {
        TasksListActivity.start(this, selectedDate);
        finish();
    }
}
