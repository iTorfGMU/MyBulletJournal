package com.torfin.mybulletjournal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.common.data.DataBufferObserver;
import com.torfin.mybulletjournal.login.LoginActivity;
import com.torfin.mybulletjournal.login.LoginPresenter;
import com.torfin.mybulletjournal.login.LoginProvider;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import java.util.Observable;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by Ila on 12/31/17.
 */

@RunWith(AndroidJUnit4.class)
public class LoginBehaviorTests {

    LoginPresenter presenter;

    @Mock
    LoginProvider mockProvider;

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(LoginActivity.class);

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        LoginProvider.setMockInstance(mockProvider);
        presenter = LoginPresenter.newInstance(mActivityRule.getActivity());
    }

    @Test
    public void verifyViewsAre_Visible() {
        onView(withId(R.id.email_edittext)).check(matches(isDisplayed()));
        onView(withId(R.id.password_edittext)).check(matches(isDisplayed()));
        onView(withId(R.id.create_user)).check(matches(isDisplayed()));
        onView(withId(R.id.login_user_button)).check(matches(isDisplayed()));
    }

    @Test
    public void verifyEmail_TextEdit() {
        String email = "test@email.com";
        onView(withId(R.id.email_edittext)).perform(typeText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.email_edittext)).check(matches(withText(email)));
    }

    @Test
    public void verifyPassword_TextEdit() {
        String password = "testPassword";
        onView(withId(R.id.password_edittext)).perform(typeText("testPassword"), closeSoftKeyboard());
        onView(withId(R.id.password_edittext)).check(matches(withText(password)));
    }

    @Test
    public void verifyCreateUser() {
        onView(withId(R.id.email_edittext)).perform(typeText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password_edittext)).perform(typeText("testPassword"), closeSoftKeyboard());

        onView(withId(R.id.create_user)).perform(click());

        onView(withId(R.id.loadingBackground)).check(matches(isDisplayed()));
    }


    @Test
    public void verifyCreateUser_IncorrectForm() {
        onView(withId(R.id.email_edittext)).perform(typeText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password_edittext)).perform(typeText("testPassword"), closeSoftKeyboard());

        onView(withId(R.id.create_user)).perform(click());

        onView(withId(R.id.loadingBackground)).check(matches(isDisplayed()));
    }


    @Test
    public void verifyLogin() {
        onView(withId(R.id.email_edittext)).perform(typeText("test@email.com"), closeSoftKeyboard());
        onView(withId(R.id.password_edittext)).perform(typeText("testPassword"), closeSoftKeyboard());

        onView(withId(R.id.login_user_button)).perform(click());

        onView(withId(R.id.loadingBackground)).check(matches(isDisplayed()));
    }

}
