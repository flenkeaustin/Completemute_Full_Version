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

import static com.example.completemute.MainActivity.five;
import static com.example.completemute.MainActivity.four;
import static com.example.completemute.MainActivity.one;
import static com.example.completemute.MainActivity.three;
import static com.example.completemute.MainActivity.two;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetPartOn extends AppWidgetProvider {
    public static int alarm,music,ring,system,voice_call;
    private static final String fs_part_on="com.example.completemute.customview.part_on.click";
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_part_on);
        Intent clickIntent = new Intent();
        clickIntent.setAction(fs_part_on);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
        remoteViews.setOnClickPendingIntent(R.id.tv_content_part_on,pendingIntent);
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
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.part_on);
        //判断是否是自定义点击action
        if (action.equals(fs_part_on)){
            Toast.makeText(context, "兼容模式已开启", Toast.LENGTH_SHORT).show();
            AudioManager audioManager=(AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
            alarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            music = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            ring = audioManager.getStreamVolume(AudioManager.STREAM_RING);
            system=audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
            voice_call = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            try{
                if (one.isChecked())
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM,0,0);
                }
                if (two.isChecked())
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                }
                if (three.isChecked())
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_RING,0,0);
                }
                if (four.isChecked())
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
                }
                if (five.isChecked())
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,0);
                }
            }catch (Exception e){}
            new Thread(new Runnable() {
                @Override
                public void run() {
                    AppWidgetManager manager = AppWidgetManager.getInstance(context);
                    for (int i = 0; i < 37; i++) {
                        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_part_on);
                        remoteViews.setImageViewBitmap(R.id.tv_content_part_on,rotateBitmap(bitmap,i*10));
                        Intent clickIntent = new Intent();
                        clickIntent.setAction(fs_part_on);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, clickIntent, 0);
                        remoteViews.setOnClickPendingIntent(R.id.tv_content_part_on,pendingIntent);
                        Log.d("", "run: ");
                        manager.updateAppWidget(new ComponentName(context, WidgetPartOn.class),remoteViews);
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

