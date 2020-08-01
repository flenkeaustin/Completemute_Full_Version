package com.example.completemute;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class SensorSettingService  extends TileService implements SensorEventListener {
        private final int STATE_OFF = 0;
        private final int STATE_ON = 1;
        private final String LOG_TAG = "QuickSettingService";
        private int toggleState = STATE_ON;
        private SensorManager sensorManager;
        private PowerManager.WakeLock mWakeLock;
        private PowerManager mPowerManager;
        private Sensor mSensor;
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
            if (toggleState == STATE_ON) {
                toggleState = STATE_OFF;
                icon =  Icon.createWithResource(getApplicationContext(), R.drawable.ic_ear_up);
                getQsTile().setState(Tile.STATE_INACTIVE);// 更改成非活跃状态
                sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                //息屏设置
                mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "");
                //注册传感器,先判断有没有传感器
                if (mSensor != null)
                    sensorManager.registerListener(SensorSettingService.this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                toggleState = STATE_ON;
                icon = Icon.createWithResource(getApplicationContext(), R.drawable.ic_ear_off);
                getQsTile().setState(Tile.STATE_ACTIVE);//更改成活跃状态
                //传感器取消监听
                sensorManager.unregisterListener(SensorSettingService.this);
                //释放息屏
                if (mWakeLock.isHeld())
                    mWakeLock.release();
                mWakeLock = null;
                mPowerManager = null;
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
    @Override
    public void onAccuracyChanged(Sensor p1, int p2) {

    }
    //传感器变化
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] == 0.0) {
            //贴近手机
            //关闭屏幕
            if (!mWakeLock.isHeld())
                mWakeLock.acquire();
        } else {
            //离开手机
            //唤醒设备
            if (mWakeLock.isHeld())
                mWakeLock.release();
        }
    }
}
