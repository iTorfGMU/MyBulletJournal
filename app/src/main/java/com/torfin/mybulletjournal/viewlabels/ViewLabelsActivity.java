package com.torfin.mybulletjournal.viewlabels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.login.LoginActivity;
import com.torfin.mybulletjournal.viewlabels.recyclerview.LabelsRecyclerViewAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class ViewLabelsActivity extends AppCompatActivity implements ViewLabelsContract.View,
        SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.view_labels_toolbar)
    Toolbar toolbar;

    @BindView(R.id.labels_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.view_labels_recyclerview)
    RecyclerView labelsRecyclerView;

    @BindView(R.id.view_label_progress_bar_container)
    LinearLayout progressBarContainer;

    private ViewLabelsPresenter presenter;

    private LabelsRecyclerViewAdapter adapter;

    public static void start(Context context) {
        Intent intent = new Intent(context, ViewLabelsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_labels);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        presenter = ViewLabelsPresenter.newInstance(this);
        presenter.subscribe(this);

        labelsRecyclerView.setHasFixedSize(true);
        labelsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccentLight),
                getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorAccentDark));
    }

    @Override
    protected void onStart() {
        super.onStart();

        presenter.setupViews();
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
        progressBarContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        progressBarContainer.setVisibility(View.GONE);
    }

    @Override
    public void setRecyclerView(List<String> labels) {
        adapter = new LabelsRecyclerViewAdapter(this, labels);
        labelsRecyclerView.setAdapter(adapter);
    }

    @Override
    public void updateRecyclerView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void stopRefresh() {
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        presenter.getUpdatedLabelList();
    }
}
