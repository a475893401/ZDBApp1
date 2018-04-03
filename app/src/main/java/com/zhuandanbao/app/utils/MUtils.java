package com.zhuandanbao.app.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.imagepicker.bean.ImageItem;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.entity.DataEntity;
import com.zhuandanbao.app.entity.OrderItemEntity;
import com.zhuandanbao.app.entity.OrderItemListEntity;
import com.zhuandanbao.app.entity.ShowButText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by BFTECH on 2017/2/13.
 */

public class MUtils {

    public static Dialog dialog;
    /**
     *内容显示
     */
    public static void showDialog(Context context,String titleStr, String deleteStr, String okStr,
                                  String contentStr, View.OnClickListener deleteOnClick,
                                  View.OnClickListener okOnClick){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
        TextView title= (TextView) view.findViewById(R.id.dialog_title);
        TextView content= (TextView) view.findViewById(R.id.dialog_content);
        final TextView delete= (TextView) view.findViewById(R.id.dialog_delete);
        TextView ok= (TextView) view.findViewById(R.id.dialog_ok);
        dialog=new Dialog(context,R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        if (StrUtils.isNotNull(titleStr)){
            title.setText(titleStr);
        }
        if (StrUtils.isNotNull(contentStr)){
            content.setVisibility(View.VISIBLE);
            content.setText(contentStr);
        }else {
            content.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(deleteStr)){
            delete.setText(deleteStr);
        }
        if (StrUtils.isNotNull(okStr)){
            ok.setText(okStr);
        }
        if (null!=deleteOnClick){
            delete.setOnClickListener(deleteOnClick);
        }else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null!=dialog){
                        dialog.cancel();
                    }
                }
            });
        }
        if (null!=okOnClick){
            ok.setOnClickListener(okOnClick);
        }else {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null!=dialog){
                        dialog.cancel();
                    }
                }
            });
        }
        dialog.show();
    }

    /**
     * 自定義佈局
     * @param context
     * @param itemView
     * @param titleStr
     * @param deleteStr
     * @param okStr
     * @param deleteOnClick
     * @param okOnClick
     */
    public static void showItemViewDialog(Context context, View itemView,String titleStr, String deleteStr, String okStr,
                                          View.OnClickListener deleteOnClick,
                                  View.OnClickListener okOnClick){
        View view= LayoutInflater.from(context).inflate(R.layout.dialog_layout,null);
        TextView title= (TextView) view.findViewById(R.id.dialog_title);
        LinearLayout contentLayout= (LinearLayout) view.findViewById(R.id.dialog_content_layout);
        final TextView delete= (TextView) view.findViewById(R.id.dialog_delete);
        TextView ok= (TextView) view.findViewById(R.id.dialog_ok);
        dialog=new Dialog(context,R.style.fullDialog);
        dialog.getWindow().setContentView(view);
        if (StrUtils.isNotNull(titleStr)){
            title.setText(titleStr);
        }
        if (null!=itemView){
            contentLayout.removeAllViews();
            contentLayout.addView(itemView);
        }
        if (StrUtils.isNotNull(deleteStr)){
            delete.setText(deleteStr);
        }else {
            delete.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(okStr)){
            ok.setText(okStr);
        }else {
            ok.setVisibility(View.GONE);
        }
        if (null!=deleteOnClick){
            delete.setOnClickListener(deleteOnClick);
        }else {
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null!=dialog){
                        dialog.cancel();
                    }
                }
            });
        }
        if (null!=okOnClick){
            ok.setOnClickListener(okOnClick);
        }else {
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null!=dialog){
                        dialog.cancel();
                    }
                }
            });
        }
        dialog.show();
    }
    /**
     * 返回天时分秒时间戳
     *
     * @param startTime
     * @param stopTime
     * @return
     */
    public static long[] callBacKTime(Long startTime, long stopTime) {
        //截止时间戳
            long diff = (stopTime - startTime)*1000;
            long day = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - day * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long mins = (diff - day * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            long s = (diff - day * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - mins * (1000 * 60)) / (1000);
            return new long[]{day, hours, mins, s};
    }


    /**
     * 店铺等级
     *
     * @return
     */
    public static DataEntity showCountShopGrade(long grade) {
        DataEntity shopGradeData = new DataEntity();
        // 1 桃心 2 砖石 3 绿色皇冠 4 金色皇冠
        if (0 <= grade && grade <= 10) {
            shopGradeData.gradeColor = 1;
            shopGradeData.grade = 1;
        } else if (11 <= grade && grade <= 40) {
            shopGradeData.gradeColor = 1;
            shopGradeData.grade = 2;
        } else if (41 <= grade && grade <= 90) {
            shopGradeData.gradeColor = 1;
            shopGradeData.grade = 3;
        } else if (91 <= grade && grade <= 150) {
            shopGradeData.gradeColor = 1;
            shopGradeData.grade = 4;
        } else if (150 <= grade && grade <= 250) {
            shopGradeData.gradeColor = 1;
            shopGradeData.grade = 5;
        } else if (251 <= grade && grade <= 500) {
            shopGradeData.gradeColor = 2;
            shopGradeData.grade = 1;
        } else if (501 <= grade && grade <= 1000) {
            shopGradeData.gradeColor = 2;
            shopGradeData.grade = 2;
        } else if (1000 <= grade && grade <= 2000) {
            shopGradeData.gradeColor = 2;
            shopGradeData.grade = 3;
        } else if (2001 <= grade && grade <= 5000) {
            shopGradeData.gradeColor = 2;
            shopGradeData.grade = 4;
        } else if (5001 <= grade && grade <= 10000) {
            shopGradeData.gradeColor = 2;
            shopGradeData.grade = 5;
        } else if (10001 <= grade && grade <= 20000) {
            shopGradeData.gradeColor = 3;
            shopGradeData.grade = 1;
        } else if (20001 <= grade && grade <= 50000) {
            shopGradeData.gradeColor = 3;
            shopGradeData.grade = 2;
        } else if (50001 <= grade && grade <= 100000) {
            shopGradeData.gradeColor = 3;
            shopGradeData.grade = 3;
        } else if (100001 <= grade && grade <= 200000) {
            shopGradeData.gradeColor = 3;
            shopGradeData.grade = 4;
        } else if (200001 <= grade && grade <= 500000) {
            shopGradeData.gradeColor = 3;
            shopGradeData.grade = 5;
        } else if (500001 <= grade && grade <= 1000000) {
            shopGradeData.gradeColor = 4;
            shopGradeData.grade = 1;
        } else if (1000001 <= grade && grade <= 2000000) {
            shopGradeData.gradeColor = 4;
            shopGradeData.grade = 2;
        } else if (2000001 <= grade && grade <= 5000000) {
            shopGradeData.gradeColor = 4;
            shopGradeData.grade = 3;
        } else if (5000001 <= grade && grade <= 10000000) {
            shopGradeData.gradeColor = 4;
            shopGradeData.grade = 4;
        } else if (grade > 10000000) {
            shopGradeData.gradeColor = 4;
            shopGradeData.grade = 5;
        }
        return shopGradeData;
    }


    // 倒计时时间戳转换为时间
    public static String getTo66TimePHP(String time, String timeType) {
        String re = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(timeType);
            long ttt = Long.parseLong(time);
            re = sdf.format(new Date(ttt * 1000L));
            //时间戳到具体显示的转化
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }


    // 倒计时时间戳转换为时间
    public static String getTo66TimeAndroid(String time, String timeType) {
        String re = null;
        SimpleDateFormat sdf = new SimpleDateFormat(timeType);
        long ttt = Long.parseLong(time);
        re = sdf.format(new Date(ttt));
        //时间戳到具体显示的转化
        return re;
    }

    /**
     * 显示抢单底部按钮
     *
     * @param view
     * @param info
     * @param listener
     */
    public static void showGradBut(LinearLayout view, ShowButText info, View.OnClickListener listener) {
        MLog.e("info.isShowOnline=="+info.isShowOnline);
        LinearLayout onlineLayout= (LinearLayout) view.findViewById(R.id.but_online_layout);
        TextView online = (TextView) view.findViewById(R.id.but_online);
        Button ok = (Button) view.findViewById(R.id.but_ok);
        Button cancle = (Button) view.findViewById(R.id.but_cancle);
        Button button = (Button) view.findViewById(R.id.but_blue);
        if (StrUtils.isNotNull(info.isShowOnline)) {
            onlineLayout.setVisibility(View.VISIBLE);
            online.setText(info.isShowOnline);
            if (null != listener)
                onlineLayout.setOnClickListener(listener);
        }else {
            onlineLayout.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(info.isShowAmend)) {
            button.setVisibility(View.VISIBLE);
            button.setText(info.isShowAmend);
            if (null != listener)
                button.setOnClickListener(listener);
        }else {
            button.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(info.isShowOk)) {
            if (null != listener)
                ok.setOnClickListener(listener);
            ok.setVisibility(View.VISIBLE);
            ok.setText(info.isShowOk);
        }else {
            ok.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(info.isShowCancle)) {
            if (null != listener)
                cancle.setOnClickListener(listener);
            cancle.setVisibility(View.VISIBLE);
            cancle.setText(info.isShowCancle);
        }else {
            cancle.setVisibility(View.GONE);
        }
    }

    private static void goToActivity(Context context, String className) {
        Intent intent = new Intent();
        intent.setClassName(context, className);
        context.startActivity(intent);
    }

    /**
     * show发单底部按钮
     *
     * @param view
     * @param butInfo
     * @param onClickListener
     */
    public static void showBillInfoBut(LinearLayout view, ShowButText butInfo, View.OnClickListener onClickListener) {
        TextView but_retry = (TextView) view.findViewById(R.id.but_retry);
        TextView but_cancel = (TextView) view.findViewById(R.id.but_cancel);
        TextView but_amend = (TextView) view.findViewById(R.id.but_amend);
        TextView but_pay = (TextView) view.findViewById(R.id.but_pay);
        if (StrUtils.isNotNull(butInfo.isShowOnline)) {//重发
            but_retry.setText(butInfo.isShowOnline);
            but_retry.setVisibility(View.VISIBLE);
            but_retry.setOnClickListener(onClickListener);
        } else {
            but_retry.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(butInfo.isShowCancle)) {//取消订单
            but_cancel.setText(butInfo.isShowCancle);
            but_cancel.setVisibility(View.VISIBLE);
            but_cancel.setOnClickListener(onClickListener);
        } else {
            but_cancel.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(butInfo.isShowAmend)) {//修改订单
            but_amend.setText(butInfo.isShowAmend);
            but_amend.setVisibility(View.VISIBLE);
            but_amend.setOnClickListener(onClickListener);
        } else {
            but_amend.setVisibility(View.GONE);
        }
        if (StrUtils.isNotNull(butInfo.isShowOk)) {//立即支付
            but_pay.setText(butInfo.isShowOk);
            but_pay.setVisibility(View.VISIBLE);
            but_pay.setOnClickListener(onClickListener);
        } else {
            but_pay.setVisibility(View.GONE);
        }
    }

    /**
     * @param time  比較時間
     * @param minut 過期分鐘
     * @return
     */
    public static boolean timePast(long time, int minut) {
        long s = (System.currentTimeMillis() - time) / (1000 * 60);
        if (s < minut) {
            return true;
        }
        return false;
    }

    /**
      * 将时间转换为时间戳
      */
    public static long dateToStamp(String s,String stype) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(stype);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }

    /**
     * 附件地址竖线
     *
     * @param list
     * @return
     */
    public static String getImageItemPath(List<ImageItem> list, String str) {
        String arr = null;
        if (null != list && list.size() != 0) {
            if (list.size() == 1) {
                arr = list.get(0).path;
            }
            if (list.size() == 2) {
                arr = list.get(0).path + str + list.get(1).path;
            }
            if (list.size() == 3) {
                arr = list.get(0).path + str + list.get(1).path + str + list.get(2).path;
            }
        }
        return arr;
    }

    //时间戳转换为时间
    public static long getTimeTo(String time, String timeType) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeType);
        Date date = null;
        try {
            date = simpleDateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public static String getOrderItemString(List<OrderItemEntity> list){
        if (null==list||list.size()==0){
            return null;
        }
        List<OrderItemListEntity> listEntities=new ArrayList<>();
        for (OrderItemEntity info:list){
            OrderItemListEntity itemEntity=new OrderItemListEntity();
            itemEntity.item_name=info.getItem_name();
            itemEntity.item_remarks=info.getItem_remarks();
            itemEntity.item_total=info.getItem_total();
            if (StrUtils.isNotNull(info.getItem_img())){
                itemEntity.item_img=new ArrayList<>();
                itemEntity.item_img.addAll(JSON.parseArray(info.getItem_img(),String.class));
            }
            listEntities.add(itemEntity);
        }
        return JSON.toJSONString(listEntities);
    }

    public static List<OrderItemListEntity> getOrderItemList(List<OrderItemEntity> list){
        if (null==list||list.size()==0){
            return null;
        }
        List<OrderItemListEntity> listEntities=new ArrayList<>();
        for (OrderItemEntity info:list){
            OrderItemListEntity itemEntity=new OrderItemListEntity();
            itemEntity.item_name=info.getItem_name();
            itemEntity.item_remarks=info.getItem_remarks();
            itemEntity.item_total=info.getItem_total();
            if (StrUtils.isNotNull(info.getItem_img())){
                itemEntity.item_img=new ArrayList<>();
                itemEntity.item_img.addAll(JSON.parseArray(info.getItem_img(),String.class));
            }
            listEntities.add(itemEntity);
        }
        return listEntities;
    }

    /**
     * @return 1全部 2沒有上午 3沒有中午
     */
    public static int getDvTimeList(String time) {
        Date date = new Date();
        date.setTime(getTimeTo(time, "yyyy-MM-dd"));
        int dvTime = 1;
        //现在时间
        Date now = new Date();
        now.setTime(System.currentTimeMillis());
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(now);
        MLog.e("nowD==" + calendar1.get(Calendar.HOUR_OF_DAY));
        int hour = calendar1.get(Calendar.HOUR_OF_DAY);
        if (getShowDV(date)) {//是否為當天時間
            if (hour < 11) {//上午
                dvTime = 1;
            } else if (hour >= 11 && hour < 14) {//中午
                dvTime = 2;
            } else if (hour >= 14) {//下午
                dvTime = 3;
            }
        }
        return dvTime;
    }
    /**
     * 判斷顯示怎樣的時間段
     *
     * @return
     */
    public static boolean getShowDV(Date date) {
        boolean isDangtian = true;// 是否为当天 false 不是
        try {
            //现在时间
            Date now = new Date();
            now.setTime(System.currentTimeMillis());
            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(now);
            int nowY = calendar1.get(Calendar.YEAR);
            int nowM = calendar1.get(Calendar.MONTH) + 1;
            int nowD = calendar1.get(Calendar.DAY_OF_MONTH);
            //選擇時間
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(date);
            int choiceY = calendar2.get(Calendar.YEAR);
            int choiceM = calendar2.get(Calendar.MONTH) + 1;
            int choiceD = calendar2.get(Calendar.DAY_OF_MONTH);
            if (nowY == choiceY && nowM == choiceM && nowD == choiceD) {//则选择的是当天时间
                isDangtian = true;
            } else {
                isDangtian = false;
            }
        } catch (Exception e) {
            isDangtian = false;
        }
        return isDangtian;
    }


    /**
     * 附件地址数组
     *
     * @param list
     * @return
     */
    public static String[] getPathStr(List<ImageItem> list) {
        String[] arr = null;
        if (null != list && list.size() != 0) {
            List<String> img = new ArrayList<>();
            final int size = list.size();
            for (int i = 0; i < size; i++) {
                img.add(list.get(i).path);
            }
            arr = img.toArray(new String[size]);
        }
        MLog.e("arr==="+arr);
        return arr;
    }

    /**
     * 附件地址竖线
     *
     * @param list
     * @return
     */
    public static String getPath(List<ImageItem> list, String str) {
        String arr = null;
        if (null != list && list.size() != 0) {
            if (list.size() == 1) {
                arr = list.get(0).path;
            }
            if (list.size() == 2) {
                arr = list.get(0).path + str + list.get(1).path;
            }
            if (list.size() == 3) {
                arr = list.get(0).path + str + list.get(1).path + str + list.get(2).path;
            }
        }
        return arr;
    }


    /**
     * String转ImageItem
     * @return
     */
    public static List<ImageItem> itemToString(List<String> strList ){
        List<ImageItem> list=new ArrayList<>();
        if (null!=strList&&strList.size()>0){
            for (String s:strList){
                ImageItem imageItem=new ImageItem();
                imageItem.url=s;
                list.add(imageItem);
            }
        }
        return list;
    }


    public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

}
