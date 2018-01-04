package com.torfin.mybulletjournal.login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.taskslist.TasksListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ila on 12/4/17.
 */

public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    @BindView(R.id.email_edittext)
    EditText email;

    @BindView(R.id.password_edittext)
    EditText password;

    @BindView(R.id.create_user)
    Button createUserButton;

    @BindView(R.id.login_user_button)
    Button loginButton;

    @BindView(R.id.loadingBackground)
    LinearLayout loadingView;

    private LoginPresenter presenter;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        presenter = LoginPresenter.newInstance();
        presenter.subscribe(this);

        createUserButton.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        presenter.unsubscribe(this);
        presenter = null;

        super.onDestroy();
    }

    @Override
    public void loginUser() {
        TasksListActivity.start(this);
    }

    @Override
    public void showLoading() {
        this.loadingView.setVisibility(View.VISIBLE);
        this.createUserButton.setVisibility(View.INVISIBLE);
        this.loginButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLoading() {
        this.loadingView.setVisibility(View.GONE);
        this.createUserButton.setVisibility(View.VISIBLE);
        this.loginButton.setVisibility(View.VISIBLE);
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
    public void showMessage(@StringRes int resId) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, getString(resId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showMessage(String message) {
        View parentLayout = findViewById(android.R.id.content);
        Snackbar.make(parentLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //View OnClickListener Imported Methods
    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (!isNetworkConnected()) {
            showMessage(getString(R.string.no_connectivity_error));
        }

        switch(id) {
            case R.id.create_user:
                presenter.onCreateAccountClicked(email.getText().toString(), password.getText().toString(), this);
                break;
            case R.id.login_user_button:
                presenter.onLoginClicked(email.getText().toString(), password.getText().toString(), this);
                break;
            default:
                break;
        }
    }
}
