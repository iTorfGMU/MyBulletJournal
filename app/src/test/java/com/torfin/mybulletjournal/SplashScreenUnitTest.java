package com.torfin.mybulletjournal;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.torfin.mybulletjournal.splashscreen.SplashScreenContract;
import com.torfin.mybulletjournal.splashscreen.SplashScreenPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Context.class, FirebaseAuth.class})
public class SplashScreenUnitTest {

    SplashScreenPresenter presenter;

    @Mock
    SplashScreenContract.View view;

    @Mock
    FirebaseUser user;

    @Mock
    Context context;

    @Before
    public void setup() {
        initMocks(this);

        presenter = SplashScreenPresenter.newInstance(context);
    }

    @Test
    public void test_subscribe() {
        presenter.subscribe(view);
        assertNotNull(presenter.getView());
    }

    @Test
    public void test_unsubscribe() {
        presenter.unsubscribe(view);
        assertNull(presenter.getView());
    }

    @Test
    public void test_chooseNextPage_UserLoggedIn() {
        presenter.subscribe(view);

        presenter.setUser(user);
        presenter.determineNextPage();

        verify(view, never()).userSignIn();
        verify(view).userLoggedIn();
    }

    @Test
    public void test_chooseNextPage_UserNotLoggedIn() {
        presenter.subscribe(view);
        presenter.determineNextPage();

        verify(view, never()).userLoggedIn();
        verify(view).userSignIn();
    }
}