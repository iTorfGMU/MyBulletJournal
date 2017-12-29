package com.torfin.mybulletjournal.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.dataobjects.User;


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

        void loginFailed();

        void createUserFailed();

        void validationSuccessful();

        void validationFailed();
    }

    private LoginProvider(LoginCallback callback) {
        this.auth = FirebaseAuth.getInstance();
        this.callback = callback;
        this.database = FirebaseDatabase.getInstance().getReference();
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

    public FirebaseUser getCurrentUser() {
        return auth.getCurrentUser();
    }

    void createNewUser(final String email, final String password, Activity activity) {
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
                            callback.createUserFailed();
                        }
                    }
                });
    }

    void loginUser(String email, String password, Activity activity) {
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
                            callback.loginFailed();
                        }
                    }
                });
    }

    //TODO 12/25/17: do i need this?
    void validateEmail(Activity activity) {
        final FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(activity, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            callback.validationSuccessful();
                        } else {
                            Log.e(TAG, "sendEmailVerification", task.getException());
                            callback.validationFailed();
                        }
                    }
                });
    }

    private void createNewUser(String email, String password, FirebaseUser user) {
        User localUser = new User(email, password);
        this.database.child("users").child(user.getUid()).setValue(localUser);
    }

}
