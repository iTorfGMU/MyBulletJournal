package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.torfin.mybulletjournal.login.LoginActivity;
import com.torfin.mybulletjournal.login.LoginContract;
import com.torfin.mybulletjournal.login.LoginPresenter;
import com.torfin.mybulletjournal.login.LoginProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by torftorf1 on 1/1/18.
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({FirebaseAuth.class, FirebaseDatabase.class})
public class LoginPresenterTest {

    LoginPresenter presenter;

    @Mock
    LoginProvider provider;

    @Mock
    LoginContract.View view;

    @Mock
    LoginActivity activity;

    @Mock
    Context context;

    @Before
    public void setup() {
        initMocks(this);

        LoginProvider.setMockInstance(provider);
        presenter = LoginPresenter.newInstance();
    }

    @Test
    public void test_subscribe() {
        presenter.subscribe(view);
        assertNotNull(presenter.getView());
    }

    @Test
    public void test_unsubscribe() {
        presenter.subscribe(view);
        assertNotNull(presenter.getView());

        presenter.unsubscribe(view);
        assertNull(presenter.getView());
    }

    @Test
    public void test_verify_createUser() {
        presenter.subscribe(view);

        String email = "test@gmail.com";
        String password = "testing";

        presenter.onCreateAccountClicked(email, password, activity);
        verify(view).showLoading();
        verify(provider).createNewUser(email, password, activity);
    }

    @Test
    public void test_verify_login() {
        presenter.subscribe(view);

        String email = "test@gmail.com";
        String password = "testing";


        presenter.onLoginClicked(email, password, activity);
        verify(view).showLoading();
        verify(provider).loginUser(email, password, activity);
    }

    @Test
    public void test_verify_validateForm_valid() {
        String email = "test@gmail.com";
        String password = "testing";

        assertTrue(presenter.validateForm(email, password));
    }

    @Test
    public void test_verify_validateForm_invalid_email() {
        String email = "";
        String password = "testing";

        assertFalse(presenter.validateForm(email, password));
    }

    @Test
    public void test_verify_validateForm_invalid_emailNull() {
        String email = null;
        String password = "testing";

        assertFalse(presenter.validateForm(email, password));
    }

    @Test
    public void test_verify_validateForm_invalid_password() {
        String email = "test@gmail.com";
        String password = "";

        assertFalse(presenter.validateForm(email, password));
    }

    @Test
    public void test_verify_validateForm_invalid_password_null() {
        String email = "test@gmail.com";
        String password = null;

        assertFalse(presenter.validateForm(email, password));
    }

    @Test
    public void test_verify_login_successful() {
        presenter.subscribe(view);

        presenter.loginSuccessful();

        verify(view).hideLoading();
        verify(view, never()).onError(anyString());
        verify(view).loginUser();
    }

    @Test
    public void test_verify_login_failed() {
        presenter.subscribe(view);

        presenter.loginFailed("Invalid email");

        verify(view).hideLoading();
        verify(view, never()).loginUser();
        verify(view).onError(anyString());
    }

    @Test
    public void test_verify_createUser_failed() {
        presenter.subscribe(view);

        presenter.createUserFailed("Invalid email");

        verify(view).hideLoading();
        verify(view, never()).loginUser();
        verify(view).onError(anyString());
    }

}
