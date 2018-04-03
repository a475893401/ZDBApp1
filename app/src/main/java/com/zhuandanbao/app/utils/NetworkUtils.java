package com.zhuandanbao.app.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {
    /**
     * 判断有无网络
     */
    public static boolean isNetworkAvailable(Activity activity) {
        boolean isHaveNetwork = false;
        Context context = activity.getApplicationContext();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (null != info) {
            if (info.getState() == NetworkInfo.State.CONNECTED) {
                MLog.e("网络变化", "网络已连接");
                isHaveNetwork = true;
            } else {
                MLog.e("网络变化", "网络已断开");
                isHaveNetwork = false;
            }
        } else {
            MLog.e("网络变化", "无网络");
            isHaveNetwork = false;
        }
        return isHaveNetwork;
    }
}
