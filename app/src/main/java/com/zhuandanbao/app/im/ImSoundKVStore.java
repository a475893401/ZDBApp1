package com.zhuandanbao.app.im;

import com.alibaba.wxlib.util.SimpleKVStore;
import com.zhuandanbao.app.im.utils.ImLoginHelper;

/**
 * Created by BFTECH on 2016/12/16.
 */
public class ImSoundKVStore {

    private static String NEED_SOUND = "need_sound";//是否静音
    private static String NEED_VIBRATION = "need_vibration";//是否震动

    public static int getNeedSound() {
        return SimpleKVStore.getIntPrefs(getUserId() + NEED_SOUND, 1);
    }

    public static void setNeedSound(int value) {
        SimpleKVStore.setIntPrefs(getUserId() + NEED_SOUND, value);
    }

    public static int getNeedVibration() {
        return SimpleKVStore.getIntPrefs(getUserId() + NEED_VIBRATION, 1);
    }

    public static void setNeedVibration(int value) {
        SimpleKVStore.setIntPrefs(getUserId() + NEED_VIBRATION, value);
    }

    private static String getUserId() {
        return ImLoginHelper.getInstance().getIMKit().getIMCore().getLongLoginUserId();
    }
}
