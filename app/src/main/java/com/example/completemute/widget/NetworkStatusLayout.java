package com.example.completemute.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.completemute.R;

public class NetworkStatusLayout extends LinearLayout {
    public NetworkStatusLayout(Context context) {
        super(context);
        inflate(context, R.layout.layout_network_status, this);
    }
}
