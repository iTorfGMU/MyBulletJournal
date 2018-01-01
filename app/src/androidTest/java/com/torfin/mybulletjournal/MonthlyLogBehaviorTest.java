package com.torfin.mybulletjournal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.google.firebase.auth.FirebaseUser;
import com.torfin.mybulletjournal.contentprovider.TaskLabelProvider;
import com.torfin.mybulletjournal.contentprovider.TasksProvider;
import com.torfin.mybulletjournal.monthlylog.MonthlyLogActivity;
import com.torfin.mybulletjournal.monthlylog.MonthlyLogPresenter;
import com.torfin.mybulletjournal.newtask.AddNewTaskActivity;
import com.torfin.mybulletjournal.newtask.AddNewTaskPresenter;
import com.torfin.mybulletjournal.utils.DateUtils;
import com.torfin.mybulletjournal.utils.ProfileUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by torftorf1 on 1/1/18.
 */

@RunWith(AndroidJUnit4.class)
public class MonthlyLogBehaviorTest {

    MonthlyLogPresenter presenter;

    TasksProvider provider;

    TaskLabelProvider labelProvider;

    @Rule
    public ActivityTestRule<MonthlyLogActivity> mActivityRule = new ActivityTestRule<>(MonthlyLogActivity.class);

    @Before
    public void setUp() throws Exception {

        mActivityRule.getActivity();

        provider = mock(TasksProvider.class);
        TasksProvider.setMockInstance(provider);

        labelProvider = mock(TaskLabelProvider.class);
        TaskLabelProvider.setMockInstance(labelProvider);

        FirebaseUser user = mock(FirebaseUser.class);
        ProfileUtils.setUser(user);

        presenter = MonthlyLogPresenter.newInstance(mActivityRule.getActivity());
        presenter.subscribe(mActivityRule.getActivity());
    }

    @Test
    public void viewTasksButton_IsDisplayed() {
        onView(withId(R.id.view_tasks_for_date_button)).check(matches(isDisplayed()));
    }

    @Test
    public void viewTasksButton_isClicked() {
        presenter.setSelectedDate(Calendar.getInstance().getTimeInMillis());

        onView(withId(R.id.view_tasks_for_date_button)).perform(click());
        onView(withId(R.id.date_text_view)).check(matches(isDisplayed()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMM dd, YYYY", Locale.getDefault());
        onView(withId(R.id.date_text_view))
                .check(matches(withText(DateUtils.formatDate(Calendar.getInstance().getTime(), dateFormat))));
    }

}
