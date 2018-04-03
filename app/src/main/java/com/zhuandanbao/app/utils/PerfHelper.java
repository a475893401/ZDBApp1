/**
 *
 */
package com.zhuandanbao.app.utils;


import android.content.Context;
import android.content.SharedPreferences;

import com.zhuandanbao.app.MainApplication;

/**
 * @description 配置信息读写工具类
 */

public class PerfHelper {
    private static final String P_NAME = "artanddesign";
    public static final String WIDTH = "p_width";
    public static final String HEIGHT = "p_height";
    private static SharedPreferences sp;
    private static PerfHelper ph;

    private PerfHelper() {

    }

    public static PerfHelper getPerferences(Context a) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        return ph;
    }

    public static PerfHelper getPerferences() {
        return ph;
    }

    public static void setInfo(String name, String data) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        SharedPreferences.Editor e = sp.edit().putString(name, data);
        e.commit();
    }

    public static void setInfo(String name, int data) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        SharedPreferences.Editor e = sp.edit().putInt(name, data);
        e.commit();
    }

    public static void setInfo(String name, boolean data) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        SharedPreferences.Editor e = sp.edit().putBoolean(name, data);
        e.commit();
    }

    public static int getIntData(String name) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        return sp.getInt(name, 0);
    }

    public static String getStringData(String name) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        return sp.getString(name, "");
    }

    /**
     * @param name
     * @param defaultStr
     * @return 自定义默认值的String
     */
    public static String getStringData(String name, String defaultStr) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        return sp.getString(name, defaultStr);
    }

    public static boolean getBooleanData(String name) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        return sp.getBoolean(name, false);
    }

    public static void setInfo(String name, long data) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        SharedPreferences.Editor e = sp.edit().putLong(name, data);
        e.commit();
    }

    public static long getLongData(String name) {
        if (ph == null) {
            ph = new PerfHelper();
            sp = MainApplication.sInstance.getSharedPreferences(P_NAME, 0);
        }
        return sp.getLong(name, 0);
    }
}
