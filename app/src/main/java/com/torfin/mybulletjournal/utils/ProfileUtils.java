package com.torfin.mybulletjournal.utils;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;

/**
 * Created by torftorf1 on 12/26/17.
 */

public class ProfileUtils {

    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static void verifyToken(final VerifyListener callback) {
        if (user == null) {
            callback.error();
        }

        user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    if (idToken != null && idToken.length() != 0) {
                        callback.successful();
                    } else {
                        callback.error();
                    }
                } else {
                    callback.error();
                }
            }
        });
    }

    public static void signout() {
        FirebaseAuth.getInstance().signOut();
    }

    public interface VerifyListener {
        void successful();

        void error();
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    public static void setUser(FirebaseUser u) {
        user = u;
    }

}
