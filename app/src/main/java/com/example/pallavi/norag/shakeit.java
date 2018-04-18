package com.example.pallavi.norag;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class shakeit extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shakeit);
        //views.setTextViewText(R.id.shake, widgetText);
        // Instruct the widget manager to update the widget
        //PendingIntent intent
       // views.setOnClickPendingIntent(R.id.shake);
       // final Button shake=find
        Intent intent = new Intent(context, SensorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.shake,pendingIntent);
      //  views.
        appWidgetManager.updateAppWidget(appWidgetId, views);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
           // int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity


            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shakeit);
         //   views.setOnClickPendingIntent(R.id.shake, getPendingIntent(views.));

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private PendingIntent getPendingIntent(Context context, boolean s) {
        Intent intent = new Intent(context, SensorService.class);
        if(s)
            intent.setAction("on");
        else intent.setAction("off");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        return pendingIntent;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created

    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

