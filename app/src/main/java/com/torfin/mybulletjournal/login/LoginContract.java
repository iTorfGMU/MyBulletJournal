package com.torfin.mybulletjournal.login;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by torftorf1 on 12/4/17.
 */

public class LoginContract {

    public interface Presenter<V extends View> {

        void subscribe(View v);

        void unsubscribe(View v);

        void onCreateAccountClicked(String email, String password);

        void onLoginClicked(String email, String password);
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
