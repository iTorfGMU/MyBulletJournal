package com.torfin.mybulletjournal.login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.VisibleForTesting;

/**
 * Created by torftorf1 on 12/4/17.
 */

public class LoginPresenter implements LoginContract.Presenter<LoginContract.View>, LoginProvider.LoginCallback {

    private LoginProvider provider;

    private LoginContract.View view;

    public static LoginPresenter newInstance() {
        return new LoginPresenter();
    }

    private LoginPresenter() {
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
    public void onCreateAccountClicked(String email, String password, Activity activity) {
        if (!validateForm(email, password)) {
            this.view.showMessage("Some required information is missing.");
            return;
        }

        this.view.showLoading();
        this.provider.createNewUser(email, password, activity);
    }

    @Override
    public void onLoginClicked(String email, String password, Activity activity) {
        if (!validateForm(email, password)) {
            this.view.showMessage("Some required information is missing.");
            return;
        }

        this.view.showLoading();
        this.provider.loginUser(email, password, activity);
    }

    @Override
    public boolean validateForm(String str1, String str2) {

        boolean formValid = true;

        if (str1 == null || str1.length() == 0) {
            formValid = false;
        }

        if (str2 == null || str2.length() == 0) {
            formValid = false;
        }

        return formValid;

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

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public LoginContract.View getView() {
        return this.view;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public LoginProvider getProvider() {
        return this.provider;
    }
}
