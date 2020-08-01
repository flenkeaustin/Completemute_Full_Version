package com.example.completemute;

import android.content.Context;
import android.media.AudioManager;

class Util {
    private AudioManager am;
    private Context context;

    Util(Context context) {
        this.context = context;
        am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
    }

    void showVol() {
        am.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_SAME, AudioManager.FLAG_SHOW_UI);
    }
}
