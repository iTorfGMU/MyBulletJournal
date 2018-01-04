package com.torfin.mybulletjournal.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.dataobjects.User;
import com.torfin.mybulletjournal.utils.CrashReportingUtils;


/**
 * Created by torftorf1 on 12/4/17.
 */

public class LoginProvider {

    public static final String TAG = LoginProvider.class.getSimpleName();

    private FirebaseAuth auth;

    private static LoginProvider instance;

    private LoginCallback callback;

    private DatabaseReference database;

    interface LoginCallback {

        void loginSuccessful();

        void loginFailed(String reason);

        void createUserFailed(String reason);

        void validationSuccessful();

        void validationFailed();
    }
    
    static LoginProvider getInstance(LoginCallback callback) {
        if (instance == null) {
            synchronized (LoginProvider.class) {
                if (instance == null) {
                    instance = new LoginProvider(callback);
                }
            }
        }

        return instance;
    }

    private LoginProvider(LoginCallback callback) {
        this.auth = FirebaseAuth.getInstance();
        this.callback = callback;
        this.database = FirebaseDatabase.getInstance().getReference();
    }

    public static void setMockInstance(LoginProvider mockInstance) {
        instance = mockInstance;
    }

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    public boolean createNewUser(final String email, final String password, Activity activity) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            createNewUser(email, password, user);
                            callback.loginSuccessful();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            CrashReportingUtils.logError("createUserWithEmail:failure " + task.getException());
                            callback.createUserFailed(task.getException().getLocalizedMessage());
                        }
                    }
                });

        return true;
    }

    public boolean loginUser(String email, String password, Activity activity) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            callback.loginSuccessful();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            CrashReportingUtils.logError("signInWithEmail:failure " + task.getException());
                            callback.loginFailed(task.getException().getLocalizedMessage());
                        }
                    }
                });

        return true;
    }

    private void createNewUser(String email, String password, FirebaseUser user) {
        User localUser = new User(email, password);
        this.database.child("users").child(user.getUid()).setValue(localUser);
    }

    @VisibleForTesting
    public LoginCallback getCallback() {
        return callback;
    }

}
