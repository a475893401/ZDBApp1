package com.zhuandanbao.app.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by BFTECH on 2016/3/8.
 */
public class CardIdUtil {

    /**
     * 验证身份证号
     *
     * @param cardId
     * @return
     */
    public static boolean isCardID(String cardId) {
        String expression = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[a-zA-Z])$|^[a-zA-Z]{1,2}\\d{6}\\([0-9a-zAZ-Z]\\)$";
        CharSequence inputStr = cardId;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }


    public static boolean isApproveNum(String approveNum) {
        String expression = "^\\w{15,18}$";
        CharSequence inputStr = approveNum;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.matches();
    }

}
