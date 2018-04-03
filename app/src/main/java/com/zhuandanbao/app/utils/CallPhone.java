package com.zhuandanbao.app.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by BFTECH on 2016/3/29.
 */
public class CallPhone {

    public static void callPhone(Context context, String phone) {
        if (TextUtils.isEmpty(phone)) {
            return;
        }
        Uri uri = Uri.parse("tel:" + phone);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_DIAL); // 手工播出 ACTION_CALL//直接播出
        intent.setData(uri);
        context.startActivity(intent);
    }

}
