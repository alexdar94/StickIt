package com.now.stickit;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

/**
 * Created by User on 5/10/2015.
 */
public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
            remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent(context));

            //remoteViews.setImageViewBitmap(R.id.img_widget, BitmapFactory.decodeFile());
            //pushWidgetUpdate(context, remoteViews);
            //appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
            pushWidgetUpdate(context,appWidgetId,remoteViews);
        }
            /*try {
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);
                remoteViews.setOnClickPendingIntent(R.id.widget, pendingIntent(context));
                pushWidgetUpdate(context, remoteViews);
            } catch (Exception e) {
                Log.e("Failed", "");
            }*/
    }

    public static PendingIntent pendingIntent(Context context) {
        Intent intent = new Intent(context, AppWidgetConfigurationActivity.class);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    /*public static void pushWidgetUpdate(Context context, RemoteViews remoteViews) {
        //ComponentName myWidget = new ComponentName(context, AppWidgetProvider.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(myWidget, remoteViews);
    }*/
    public static void pushWidgetUpdate(Context context, int myWidget, RemoteViews remoteViews) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(myWidget, remoteViews);
    }
}
