package com.example.completemute;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

public class Mediavolumecontrol extends AppCompatActivity {
    AudioManager mAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediavolumecontrol);
        mAudio = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        SeekBar alarm = (SeekBar)findViewById(R.id.alarm);
        SeekBar music = (SeekBar)findViewById(R.id.music);
        SeekBar ring = (SeekBar)findViewById(R.id.ring);
        SeekBar system = (SeekBar)findViewById(R.id.system);
        SeekBar voice = (SeekBar)findViewById(R.id.voice);
        initControls(alarm, AudioManager.STREAM_ALARM);
        initControls(music, AudioManager.STREAM_MUSIC);
        initControls(ring, AudioManager.STREAM_RING);
        initControls(system, AudioManager.STREAM_SYSTEM);
        initControls(voice, AudioManager.STREAM_VOICE_CALL);
    }
    private void initControls (SeekBar seek, final int stream)
    {
        seek.setMax(mAudio.getStreamMaxVolume(stream));
        seek.setProgress(mAudio.getStreamVolume(stream));
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar bar, int progress, boolean fromUser)
            {
                try {
                    mAudio.setStreamVolume(stream, progress, AudioManager.FLAG_PLAY_SOUND);
                }catch (Exception e){}

            }
            public void onStartTrackingTouch(SeekBar bar) {
            }
            public void onStopTrackingTouch(SeekBar bar) {
            }
        });
    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            MainActivity.vSwitch.setChecked(false);
        }
        return super.onKeyDown(keyCode, event);
    }
}
