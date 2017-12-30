package com.torfin.mybulletjournal.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.torfin.mybulletjournal.R;
import com.torfin.mybulletjournal.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by torftorf1 on 12/29/17.
 */

public class TasksWidgetProvider extends AppWidgetProvider {

    public static final String DATA_FETCHED = "com.torfin.mybulletjournal.tasksWidget.DATA_FETCHED";

    private static SimpleDateFormat defaultFormat = new SimpleDateFormat("EEEE, MMM. dd, yyyy", Locale.getDefault());

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent svcIntent = new Intent(context, TasksWidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(appWidgetId, R.id.listViewWidget, svcIntent);

        remoteViews.setTextViewText(R.id.widget_date_view,
                DateUtils.formatDate(Calendar.getInstance().getTime(), defaultFormat));

        remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        final String action = intent.getAction();

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        AppWidgetManager.getInstance(context)
                .updateAppWidget(intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID), views);
    }
}
