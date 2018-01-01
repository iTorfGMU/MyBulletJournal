package com.torfin.mybulletjournal.login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by torftorf1 on 12/4/17.
 */

public class LoginContract {

    public interface Presenter<V extends View> {

        void subscribe(View v);

        void unsubscribe(View v);

        void onCreateAccountClicked(String email, String password, Activity activity);

        void onLoginClicked(String email, String password, Activity activity);

        boolean validateForm(String str1, String str2);
    }

    public interface View {

        void loginUser();

        void showLoading();

        void hideLoading();

        void onError(@StringRes int resId);

        void onError(String message);

        void showMessage(String message);

        void showMessage(@StringRes int resId);

        boolean isNetworkConnected();
    }

}
