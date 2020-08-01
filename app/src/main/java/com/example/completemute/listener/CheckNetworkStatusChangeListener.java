package com.example.completemute.listener;

public interface CheckNetworkStatusChangeListener {
    /*
      网络变化会调用
     */
    void onEvent(Status status);
    /**
     * 网络状态
     * TYPE_UN_NETWORK 沒有网络
     * TYPE_WIFI WiFi连接
     * TYPE_MOBILE 移动数据
     */
    enum Status {
        TYPE_UN_NETWORK,
        TYPE_WIFI,
        TYPE_MOBILE,
    }
}
