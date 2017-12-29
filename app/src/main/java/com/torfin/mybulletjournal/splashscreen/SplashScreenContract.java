package com.torfin.mybulletjournal.splashscreen;

import android.support.annotation.NonNull;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class SplashScreenContract {

    public interface View {
        void userLoggedIn();

        void userSignIn();
    }

    public interface Presenter {
        void determineNextPage();

        void subscribe(@NonNull SplashScreenContract.View v);

        void unsubscribe(@NonNull SplashScreenContract.View v);
    }
}
