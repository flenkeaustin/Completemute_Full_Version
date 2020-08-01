package com.example.completemute;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.os.SystemClock;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetNormalOn extends AppWidgetProvider {
    public static int alarm,music,ring,system,voice_call,notification;
    private static final String fs_normal_on="com.example.completemute.customview.normal_on.click";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_normal_on);
        Intent clickIntent = new Intent();
        clickIntent.setAction(fs_normal_on);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.tv_content_normal_on,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }
    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    @Override
    public void onReceive(final Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.d("", "onReceive: ");
        String action = intent.getAction();
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.normal_on);
        //判断是否是自定义点击action
        if (action.equals(fs_normal_on)){
            Toast.makeText(context, "正常模式已开启", Toast.LENGTH_SHORT).show();
            AudioManager audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            alarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            music = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            ring = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            system=audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
            voice_call = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            notification = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
            try {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM,0,0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                audioManager.setStreamVolume(AudioManager.STREAM_RING,0,0);
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,0);
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,0);
            }catch (Exception e){}

            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_normal_on);
                        remoteViews.setImageViewBitmap(R.id.tv_content_normal_on,rotateBitmap(bitmap,i*10));
                        Intent clickIntent = new Intent();
                        clickIntent.setAction(fs_normal_on);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.tv_content_normal_on,pendingIntent);
                        Log.d("", "run: ");
                        manager.updateAppWidget(new ComponentName(context, WidgetNormalOn.class),remoteViews);
                        SystemClock.sleep(30);
                    }
                }
            }).start();
        }
    }
    private Bitmap rotateBitmap(Bitmap bitmap,float degree) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degree);
        Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bm;
    }
}

