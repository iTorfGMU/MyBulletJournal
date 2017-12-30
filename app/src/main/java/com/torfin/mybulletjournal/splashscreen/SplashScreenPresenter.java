package com.torfin.mybulletjournal.splashscreen;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.utils.AnalyticUtils;

/**
 * Created by torftorf1 on 12/25/17.
 */

public class SplashScreenPresenter implements SplashScreenContract.Presenter {

    private SplashScreenContract.View view;

    static SplashScreenPresenter newInstance(Context c) {
        return new SplashScreenPresenter(c);
    }

    private SplashScreenPresenter(Context c) {
        AnalyticUtils.init(c);
    }

    @Override
    public void determineNextPage() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            this.view.userSignIn();
        } else {
            this.view.userLoggedIn();
        }
    }

    @Override
    public void subscribe(@NonNull SplashScreenContract.View v) {
        this.view = v;
    }

    @Override
    public void unsubscribe(@NonNull SplashScreenContract.View v) {
        if (v == this.view) {
            this.view = null;
        }
    }
}
