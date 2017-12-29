package com.torfin.mybulletjournal.login;

import android.app.Activity;

/**
 * Created by torftorf1 on 12/4/17.
 */

public class LoginPresenter implements LoginContract.Presenter<LoginContract.View>, LoginProvider.LoginCallback {

    private Activity activity;

    private LoginProvider provider;

    private LoginContract.View view;

    static LoginPresenter newInstance(Activity activity) {
        return new LoginPresenter(activity);
    }

    private LoginPresenter(Activity activity) {
        this.activity = activity;
        this.provider = LoginProvider.getInstance(this);
    }

    @Override
    public void subscribe(LoginContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(LoginContract.View v) {
        this.view = null;
    }

    @Override
    public void onCreateAccountClicked(String email, String password) {
        this.view.showLoading();
        this.provider.createNewUser(email, password, this.activity);
    }

    @Override
    public void onLoginClicked(String email, String password) {
        this.view.showLoading();
        this.provider.loginUser(email, password, this.activity);
    }

    //Provider Callback Methods
    @Override
    public void loginSuccessful() {
        this.view.hideLoading();
        this.view.loginUser();
    }

    @Override
    public void loginFailed() {
        this.view.hideLoading();
        this.view.onError("Our apologies, login failed. Please try again later.");
    }

    @Override
    public void createUserFailed() {
        this.view.hideLoading();
        this.view.onError("Our apologies, creating a new user failed. Please try again later.");
    }

    @Override
    public void validationSuccessful() {
        this.view.hideLoading();
    }

    @Override
    public void validationFailed() {
        this.view.hideLoading();
    }
}
