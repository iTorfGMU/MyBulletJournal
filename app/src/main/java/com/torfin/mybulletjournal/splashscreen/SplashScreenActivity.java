package com.torfin.mybulletjournal.splashscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.login.LoginActivity;
import com.torfin.mybulletjournal.taskslist.TasksListActivity;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class SplashScreenActivity extends AppCompatActivity implements SplashScreenContract.View {

    private SplashScreenPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash_screen);

        presenter = SplashScreenPresenter.newInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.subscribe(this);
        presenter.determineNextPage();
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe(this);
        presenter = null;
        super.onDestroy();
    }

    @Override
    public void userLoggedIn() {
        TasksListActivity.start(this);
        finish();
    }

    @Override
    public void userSignIn() {
        LoginActivity.start(this);
        finish();
    }
}
