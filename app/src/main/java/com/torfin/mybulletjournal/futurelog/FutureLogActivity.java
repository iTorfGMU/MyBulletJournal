package com.torfin.mybulletjournal.futurelog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.futurelog.recyclerview.FutureLogRecyclerAdapter;
import com.torfin.mybulletjournal.login.LoginActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ila on 12/25/17.
 */

public class FutureLogActivity extends AppCompatActivity implements FutureLogContract.View{

    @BindView(R.id.future_log_toolbar)
    Toolbar toolbar;

    @BindView(R.id.future_log_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.future_log_progress_bar_container)
    LinearLayout loadingView;

    @BindView(R.id.no_future_tasks_textview)
    TextView noFutureTasksView;

    private FutureLogRecyclerAdapter adapter;

    private FutureLogPresenter presenter;

    public static void start(Context c) {
        Intent intent = new Intent(c, FutureLogActivity.class);
        c.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_future_log);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        presenter = FutureLogPresenter.newInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.subscribe(this);
        presenter.configureRecyclerView();
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
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void setRecyclerView(List<Object> list) {
        adapter = new FutureLogRecyclerAdapter(list);
        recyclerView.setAdapter(adapter);

        if (list == null || list.size() == 0) {
            noFutureTasksView.setVisibility(View.VISIBLE);
        } else {
            noFutureTasksView.setVisibility(View.GONE);
        }
    }
}
