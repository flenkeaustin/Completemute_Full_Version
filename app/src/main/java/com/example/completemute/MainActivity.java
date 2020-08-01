package com.example.completemute;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity implements SensorEventListener {
    public static SwitchCompat mSwitch,iSwitch,pSwitch,tSwitch,hSwitch,cSwitch,vSwitch,rSwitch,aSwitch,uSwitch,mpSwitch,fSwitch;
    private Timer timer;
    private TimerTask task;
    private int time,alarm,music,ring,system,voice_call,notification;
    private EditText editTime;
    private SensorManager sensorManager;
    private PowerManager.WakeLock mWakeLock;
    private PowerManager mPowerManager;
    private Sensor mSensor;
    public static AudioManager audioManager;
    public static CheckBox one,two,three,four,five;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取sharedPrefernces对象判断，是不是第一次启动软件
        SharedPreferences preferences = getSharedPreferences("first_pref",
                MODE_PRIVATE);
        boolean isFirstIn = preferences.getBoolean("isFirstIn", true);
        // 如果是第一次启动，则跳转到引导页面，否则继续往下执行
        if (isFirstIn) {
            // start guideactivity
            Intent intent = new Intent(this,
                    GradientBackgroundExampleActivity.class);
            startActivity(intent);
            finish();
        }
        //切换插件页
        Button btnTouchEvent = findViewById(R.id.btn_touchevent);
        btnTouchEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, TouchActivity.class));
                finish();
            }
        });
        //网络检测
        Button btCheck=(Button)findViewById(R.id.tv_btCheck) ;
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
            }
        });

        //初始化Audiomanager
        initAudioManager();

        //听筒切换
        final boolean cSwitchfalg = false;
        SharedPreferences cSwitchpreferences;
        cSwitch=(SwitchCompat)findViewById(R.id.switch_change);
        // 从SharedPreferences获取数据:
        cSwitchpreferences = getSharedPreferences("cSwitchuser", Context.MODE_PRIVATE);
        if (cSwitchpreferences != null) {
            boolean cSwitchname = cSwitchpreferences.getBoolean("cSwitchflag", cSwitchfalg);
            cSwitch.setChecked(cSwitchname);
        }
        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    changeToEarMode();
                    //将数据保存至SharedPreferences:
                    SharedPreferences cSwitchpreferences = getSharedPreferences("cSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor cSwitcheditor = cSwitchpreferences.edit();
                    cSwitcheditor.putBoolean("cSwitchflag", true);
                    cSwitcheditor.commit();
                }
                else
                {
                    changeToSpeakerMode();
                    //将数据保存至SharedPreferences:
                    SharedPreferences cSwitchpreferences = getSharedPreferences("cSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor cSwitcheditor = cSwitchpreferences.edit();
                    cSwitcheditor.putBoolean("cSwitchflag", false);
                    cSwitcheditor.commit();
                }
            }
        });
        //音量控制
        vSwitch=(SwitchCompat)findViewById(R.id.switch_volume);
        vSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Intent intent=new Intent(MainActivity.this,Mediavolumecontrol.class);
                    startActivity(intent);
                }
            }
        });
        //媒体面板
        mpSwitch=(SwitchCompat)findViewById(R.id.switch_panel);
        mpSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Util util=new Util(MainActivity.this);
                    util.showVol();
                }
            }
        });
        //正常模式
        final boolean mSwitchfalg = false;
        SharedPreferences mSwitchpreferences;
        mSwitch = (SwitchCompat) findViewById(R.id.switch_all);
        // 从SharedPreferences获取数据:
        mSwitchpreferences = getSharedPreferences("mSwitchuser", Context.MODE_PRIVATE);
        if (mSwitchpreferences != null) {
            boolean mSwitchname = mSwitchpreferences.getBoolean("mSwitchflag", mSwitchfalg);
            mSwitch.setChecked(mSwitchname);
        }
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        pSwitch.setEnabled(false);
                        tSwitch.setEnabled(false);
                        alarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                        music = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        ring = audioManager.getStreamVolume(AudioManager.STREAM_RING);
                        system=audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
                        voice_call = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
                        notification = audioManager.getStreamVolume(AudioManager.STREAM_NOTIFICATION);

                        //存储音量
                        SaveVolume("alarm",alarm);
                        SaveVolume("music",music);
                        SaveVolume("ring",ring);
                        SaveVolume("system",system);
                        SaveVolume("voice_call",voice_call);
                        SaveVolume("notification",notification);

                        if (tSwitch.isChecked())
                        {
                            EnableMuteTimerTask();
                        }else{
                            audioManager.setStreamVolume(AudioManager.STREAM_ALARM,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,0);
                        }
                        //将数据保存至SharedPreferences:
                        SharedPreferences mSwitchpreferences = getSharedPreferences("mSwitchuser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mSwitcheditor = mSwitchpreferences.edit();
                        mSwitcheditor.putBoolean("mSwitchflag", true);
                        mSwitcheditor.commit();
                    } else {
                        stopTimer();
                        pSwitch.setEnabled(true);
                        tSwitch.setEnabled(true);

                        //读取音量
                        alarm=ReadVolume("alarm",alarm);
                        music=ReadVolume("music",music);
                        ring=ReadVolume("ring",ring);
                        system=ReadVolume("system",system);
                        voice_call=ReadVolume("voice_call",voice_call);
                        notification=ReadVolume("notification",notification);

                        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,alarm,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,music,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_RING,ring,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,system,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,voice_call,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,notification,0);
                        //将数据保存至SharedPreferences:
                        SharedPreferences mSwitchpreferences = getSharedPreferences("mSwitchuser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mSwitcheditor = mSwitchpreferences.edit();
                        mSwitcheditor.putBoolean("mSwitchflag", false);
                        mSwitcheditor.commit();
                    }
                }catch (Exception e){}
            }
        });
        //自选模式
        final boolean pSwitchfalg = false;
        final SharedPreferences pSwitchpreferences;
        pSwitch = (SwitchCompat) findViewById(R.id.switch_part);
        // 从SharedPreferences获取数据:
        pSwitchpreferences = getSharedPreferences("pSwitchuser", Context.MODE_PRIVATE);
        if (pSwitchpreferences != null) {
            boolean pSwitchname = pSwitchpreferences.getBoolean("pSwitchflag", pSwitchfalg);
            pSwitch.setChecked(pSwitchname);
        }
        pSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked) {
                        mSwitch.setEnabled(false);
                        tSwitch.setEnabled(false);
                        alarm = audioManager.getStreamVolume(AudioManager.STREAM_ALARM);
                        music = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                        ring = audioManager.getStreamVolume(AudioManager.STREAM_RING);
                        system=audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
                        voice_call = audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);

                        //存储音量
                        SaveVolume("alarm",alarm);
                        SaveVolume("music",music);
                        SaveVolume("ring",ring);
                        SaveVolume("system",system);
                        SaveVolume("voice_call",voice_call);

                        if (tSwitch.isChecked())
                        {
                            EnablePartMuteTimerTask();
                        }else {
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
                        }
                        //将数据保存至SharedPreferences:
                        SharedPreferences pSwitchpreferences = getSharedPreferences("pSwitchuser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor pSwitcheditor = pSwitchpreferences.edit();
                        pSwitcheditor.putBoolean("pSwitchflag", true);
                        pSwitcheditor.commit();
                    } else {
                        stopTimer();
                        mSwitch.setEnabled(true);
                        tSwitch.setEnabled(true);

                        //读取音量
                        alarm=ReadVolume("alarm",alarm);
                        music=ReadVolume("music",music);
                        ring=ReadVolume("ring",ring);
                        system=ReadVolume("system",system);
                        voice_call=ReadVolume("voice_call",voice_call);

                        audioManager.setStreamVolume(AudioManager.STREAM_ALARM,alarm,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,music,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_RING,ring,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,system,0);
                        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,voice_call,0);
                        //将数据保存至SharedPreferences:
                        SharedPreferences pSwitchpreferences = getSharedPreferences("pSwitchuser", Context.MODE_PRIVATE);
                        SharedPreferences.Editor pSwitcheditor = pSwitchpreferences.edit();
                        pSwitcheditor.putBoolean("pSwitchflag", false);
                        pSwitcheditor.commit();
                    }
                }catch (Exception e){}
            }
        });

        //获取CheckBox状态
        one = (CheckBox) findViewById(R.id.one);
        two = (CheckBox) findViewById(R.id.two);
        three = (CheckBox) findViewById(R.id.three);
        four = (CheckBox) findViewById(R.id.four);
        five = (CheckBox) findViewById(R.id.five);

        SaveState(one,"one");
        SaveState(two,"two");
        SaveState(three,"three");
        SaveState(four,"four");
        SaveState(five,"five");

        //贴耳息屏
        final boolean iSwitchfalg = false;
        final SharedPreferences iSwitchpreferences;
        iSwitch = (SwitchCompat) findViewById(R.id.switch_interest);
        // 从SharedPreferences获取数据:
        iSwitchpreferences = getSharedPreferences("iSwitchuser", Context.MODE_PRIVATE);
        if (iSwitchpreferences != null) {
            boolean iSwitchname = iSwitchpreferences.getBoolean("iSwitchflag", iSwitchfalg);
            iSwitch.setChecked(iSwitchname);
        }
        iSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
                    mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
                    //息屏设置
                    mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "");
                    //注册传感器,先判断有没有传感器
                    if (mSensor != null)
                        sensorManager.registerListener(MainActivity.this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

                    //将数据保存至SharedPreferences:
                    SharedPreferences iSwitchpreferences = getSharedPreferences("iSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor iSwitcheditor = iSwitchpreferences.edit();
                    iSwitcheditor.putBoolean("iSwitchflag", true);
                    iSwitcheditor.commit();
                } else {
                    //传感器取消监听
                    sensorManager.unregisterListener(MainActivity.this);
                    //释放息屏
                    if (mWakeLock.isHeld())
                        mWakeLock.release();
                    mWakeLock = null;
                    mPowerManager = null;

                    //将数据保存至SharedPreferences:
                    SharedPreferences iSwitchpreferences = getSharedPreferences("iSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor iSwitcheditor = iSwitchpreferences.edit();
                    iSwitcheditor.putBoolean("iSwitchflag", false);
                    iSwitcheditor.commit();
                }
            }
        });
        //时间间隔
        final boolean tSwitchfalg = false;
        final SharedPreferences tSwitchpreferences;
        tSwitch = (SwitchCompat) findViewById(R.id.switch_time);
        // 从SharedPreferences获取数据:
        tSwitchpreferences = getSharedPreferences("tSwitchuser", Context.MODE_PRIVATE);
        if (tSwitchpreferences != null) {
            boolean tSwitchname = tSwitchpreferences.getBoolean("tSwitchflag", tSwitchfalg);
            tSwitch.setChecked(tSwitchname);
        }
        tSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {

                    //将数据保存至SharedPreferences:
                    SharedPreferences tSwitchpreferences = getSharedPreferences("tSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor tSwitcheditor = tSwitchpreferences.edit();
                    tSwitcheditor.putBoolean("tSwitchflag", true);
                    tSwitcheditor.commit();
                }else{
                    //将数据保存至SharedPreferences:
                    SharedPreferences tSwitchpreferences = getSharedPreferences("tSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor tSwitcheditor = tSwitchpreferences.edit();
                    tSwitcheditor.putBoolean("tSwitchflag", false);
                    tSwitcheditor.commit();
                }
            }
        });
        //翻转静音
        final boolean fSwitchfalg = false;
        final SharedPreferences fSwitchpreferences;
        fSwitch = (SwitchCompat) findViewById(R.id.switch_flip);
        // 从SharedPreferences获取数据:
        fSwitchpreferences = getSharedPreferences("fSwitchuser", Context.MODE_PRIVATE);
        if (fSwitchpreferences != null) {
            boolean fSwitchname = fSwitchpreferences.getBoolean("fSwitchflag", fSwitchfalg);
            fSwitch.setChecked(fSwitchname);
        }
        fSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    //将数据保存至SharedPreferences:
                    SharedPreferences fSwitchpreferences = getSharedPreferences("fSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor fSwitcheditor = fSwitchpreferences.edit();
                    fSwitcheditor.putBoolean("fSwitchflag", true);
                    fSwitcheditor.commit();
                }else{
                    //将数据保存至SharedPreferences:
                    SharedPreferences fSwitchpreferences = getSharedPreferences("fSwitchuser", Context.MODE_PRIVATE);
                    SharedPreferences.Editor fSwitcheditor = fSwitchpreferences.edit();
                    fSwitcheditor.putBoolean("fSwitchflag", false);
                    fSwitcheditor.commit();
                }
            }
        });

        //测试音源
        rSwitch = (SwitchCompat) findViewById(R.id.switch_test);
        rSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            }
        });
        editTime = (EditText) findViewById(R.id.editText_time);

        //正常模式
        try {
            if (mSwitch.isChecked()) {
                pSwitch.setEnabled(false);
                tSwitch.setEnabled(false);

                if (tSwitch.isChecked())
                {
                    EnableMuteTimerTask();
                }else{
                    audioManager.setStreamVolume(AudioManager.STREAM_ALARM,0,0);
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                    audioManager.setStreamVolume(AudioManager.STREAM_RING,0,0);
                    audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
                    audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,0);
                    audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,0);
                }
            }
        }catch (Exception e){}

        //自选模式
        try {
            if (pSwitch.isChecked()) {
                mSwitch.setEnabled(false);
                tSwitch.setEnabled(false);

                if (tSwitch.isChecked())
                {
                    EnablePartMuteTimerTask();
                }else {
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
                }
            }
        }catch (Exception e){}

        //贴耳息屏
        if (iSwitch.isChecked()) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            mSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            //息屏设置
            mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = mPowerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "");
            //注册传感器,先判断有没有传感器
            if (mSensor != null)
                sensorManager.registerListener(MainActivity.this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
            }

        //时间间隔
        if (tSwitch.isChecked())
        {

        }

        //听筒切换
        if (cSwitch.isChecked())
        {
            changeToEarMode();
        }
    }
    private void EnableMuteTimerTask() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (alarm != 0 || music != 0 || ring != 0||system!=0 || voice_call != 0||notification!=0) {
                            audioManager.setStreamVolume(AudioManager.STREAM_ALARM,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_RING,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,0,0);
                            audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION,0,0);
                        }
                    }catch (Exception e){}
                }
            };
            startTimer();
        }
    }
    private void EnablePartMuteTimerTask() {
        if (timer == null) {
            timer = new Timer();
        }
        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    if (alarm != 0 || music != 0 || ring != 0||system!=0 || voice_call != 0) {
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
                    }
                }
            };
            startTimer();
        }
    }
    private void startTimer(){
        if (editTime.getText().toString().isEmpty()||Integer.parseInt(editTime.getText().toString())==0)
        {
            time=100;
        }else{
            time = Integer.parseInt(editTime.getText().toString());
        }
        if (timer != null && task != null) {
            timer.schedule(task, 0, time);//每100毫秒检测一次
        }
    }
    private void stopTimer(){
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
            task = null;
        }
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
            if (fSwitch.isChecked())
            {
                tSwitch.setChecked(true);
                pSwitch.setChecked(true);
            }
        } else {
            //离开手机
            //唤醒设备
            if (mWakeLock.isHeld())
                mWakeLock.release();
            if (fSwitch.isChecked()) {
                tSwitch.setChecked(false);
                pSwitch.setChecked(false);
            }
        }
    }
    /**
     * 初始化音频管理器
     */
    private void initAudioManager() {
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setSpeakerphoneOn(true);//默认为扬声器播放
    }
    /**
     * 切换到听筒模式
     */
    public void changeToEarMode() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            audioManager.setMode(AudioManager.MODE_IN_CALL);
        }
        audioManager.setSpeakerphoneOn(false);
    }
    /**
     * 切换到外放模式
     */
    public void changeToSpeakerMode(){
        audioManager.setMode(AudioManager.MODE_NORMAL);
        audioManager.setSpeakerphoneOn(true);
    }

    //CheckBox状态存储
    public void SaveState(final CheckBox ck,final String item)
    {
        final boolean before = false;
        SharedPreferences sp;
        // 从SharedPreferences获取数据:
        sp = getSharedPreferences("info", Context.MODE_PRIVATE);
        if (sp != null) {
            boolean after = sp.getBoolean(item, before);
            ck.setChecked(after);
        }

        ck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                {
                    //存数据
                    SharedPreferences sp=getSharedPreferences("info",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putBoolean(item,true);
                    edit.commit();
                }
                else{
                    //存数据
                    SharedPreferences sp=getSharedPreferences("info",MODE_PRIVATE);
                    SharedPreferences.Editor edit=sp.edit();
                    edit.putBoolean(item,false);
                    edit.commit();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder mDialog = new AlertDialog.Builder(
                MainActivity.this);
        mDialog.setTitle("Confirm Exit..!!!");
        mDialog.setMessage("Are you sure,You want to exit?");
        mDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
        mDialog.setNegativeButton("No", null);
        mDialog.show();
    }

    //存储音量
    public  void SaveVolume(String item,int value)
    {
        SharedPreferences preferences = getSharedPreferences("info",
                MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(item, value);
        editor.commit();
    }

    //读取音量
    public  int ReadVolume(String item,int value)
    {
        SharedPreferences preferences = getSharedPreferences("info",
                MODE_PRIVATE);
        value = preferences.getInt(item, value);
        return  value;
    }
}
