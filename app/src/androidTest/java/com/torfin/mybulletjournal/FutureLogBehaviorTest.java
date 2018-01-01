package com.torfin.mybulletjournal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.futurelog.FutureLogActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.Mockito.mock;

/**
 * Created by Ila on 1/1/18.
 */

@RunWith(AndroidJUnit4.class)
public class FutureLogBehaviorTest {

    TasksProvider provider;

    @Rule
    public ActivityTestRule<FutureLogActivity> mActivityRule = new ActivityTestRule<>(FutureLogActivity.class);

    @Before
    public void setUp() throws Exception {
        provider = mock(TasksProvider.class);
        TasksProvider.setMockInstance(provider);
    }

    @Test
    public void test_CheckViewIsVisible() {
        onView(withId(R.id.future_log_toolbar)).check(matches(isDisplayed()));
    }

}
