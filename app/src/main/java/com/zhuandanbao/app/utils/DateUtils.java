package com.zhuandanbao.app.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BFTECH on 2018/4/2.
 */

public class DateUtils {
    // 将字符串转为时间戳
    public static long getTime(String user_time,String format) {
        String re_time = null;
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d;
        try {
            d = sdf.parse(user_time);
            long l = d.getTime();
            String str = String.valueOf(l);
            re_time = str.substring(0, 10);
        }catch (ParseException e) {
            // TODO Auto-generated catch block e.printStackTrace();
        }
        return Long.parseLong(re_time);
    }
}
