package com.example.completemute;

import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

//继承TileService
public class QuickSettingService extends TileService{
    private final int STATE_OFF = 0;
    private final int STATE_ON = 1;
    private final String LOG_TAG = "QuickSettingService";
    private int toggleState = STATE_ON;
    private int alarm,music,ring,system,voice_call,notification;
    //当用户从Edit栏添加到快速设定中调用
    @Override
    public void onTileAdded() {
        Log.d(LOG_TAG, "onTileAdded");
    }
    //当用户从快速设定栏中移除的时候调用
    @Override
    public void onTileRemoved() {
        Log.d(LOG_TAG, "onTileRemoved");
    }
    // 点击的时候
    @Override
    public void onClick() {
        Log.d(LOG_TAG, "onClick state = " + Integer.toString(getQsTile().getState()));
        Icon icon;
        AudioManager audioManager=(AudioManager)getSystemService(AUDIO_SERVICE);
        if (toggleState == STATE_ON) {
            toggleState = STATE_OFF;
            icon =  Icon.createWithResource(getApplicationContext(), R.drawable.ic_volume_off);
            getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态
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

        } else {
            toggleState = STATE_ON;
            icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_volume_up);
            getQsTile().setState(Tile.STATE_ACTIVE);//更改成活跃状态
            try {
                audioManager.setStreamVolume(AudioManager.STREAM_ALARM,alarm,0);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,music,0);
                audioManager.setStreamVolume(AudioManager.STREAM_RING,ring,0);
                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,system,0);
                audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,voice_call,0);
                audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,notification,0);
            }catch (Exception e){}
        }
        getQsTile().setIcon(icon);//设置图标
        getQsTile().updateTile();//更新Tile
    }
    // 打开下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    //在TleAdded之后会调用一次
    @Override
    public void onStartListening () {
        Log.d(LOG_TAG, "onStartListening");
    }
    // 关闭下拉菜单的时候调用,当快速设置按钮并没有在编辑栏拖到设置栏中不会调用
    // 在onTileRemoved移除之前也会调用移除
    @Override
    public void onStopListening () {
        Log.d(LOG_TAG, "onStopListening");
    }
}
