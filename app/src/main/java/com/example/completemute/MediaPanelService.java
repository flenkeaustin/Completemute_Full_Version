package com.example.completemute;

import android.service.quicksettings.TileService;

public class MediaPanelService extends TileService {
    Util util;

    @Override
    public void onClick() {
        util.showVol();
    }

    @Override
    public void onStartListening() {
        util = new Util(this);
    }
}