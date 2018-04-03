package com.zhuandanbao.app.utils;

import android.util.Log;

import com.zhuandanbao.app.constant.Constants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by BFTECH on 2016/4/26.
 */
public class Sign {

    /**
     * 签名
     *
     * @param params 应用参数
     * @return
     */
    public static String createSign(HashMap<String, Object> params) {
        Map<String, Object> treeMap = new TreeMap<String, Object>(params);
        StringBuilder strBulide = new StringBuilder();
        strBulide.append(Constants.APPKEY);
        for (Map.Entry entry : treeMap.entrySet()) {
            String k = entry.getKey().toString();
            String v = entry.getValue().toString();
            if (v != null && !v.isEmpty() && !k.equals("sign")
                    && !k.equals("sign_type")) {
                strBulide.append(k + "=" + v + "&");
            }
        }
        strBulide.append(Constants.APPKEY);
        Log.d("", "sign=" + getMD5(strBulide.toString()).toUpperCase());
        return getMD5(strBulide.toString()).toUpperCase();
    }

    /**
     * Mdf码获取
     *
     * @param input
     * @return
     */
    public static String getMD5(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(input.getBytes());
            return getHashString(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }
}
