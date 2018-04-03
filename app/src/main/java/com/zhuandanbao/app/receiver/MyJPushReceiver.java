package com.zhuandanbao.app.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.BillOrderInfoActivity;
import com.zhuandanbao.app.activity.GradInfoActivity;
import com.zhuandanbao.app.activity.SalesInfoActivity;
import com.zhuandanbao.app.activity.TakingOrderInfoActivity;
import com.zhuandanbao.app.activity.ZdbInfoActivity;
import com.zhuandanbao.app.activity.ZdbMsgInfoActivity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.SuiJiShu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by BFTECH on 2016/11/30.
 *
 * type 3待搶單  type 2 待接單  type  8 售後   type 4 狀態改變
 */
public class MyJPushReceiver extends BroadcastReceiver {
    private static final String TAG = "MyJPushReceiver";

    @Override
    public void onReceive(final Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            MLog.e(TAG, "[JGMyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            processCustomMessage(context, bundle);
            return;
        }
    }

    private void processCustomMessage(Context context, Bundle bundle) {
        NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //为了版本兼容  选择V7包下的NotificationCompat进行构造
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        //Ticker是状态栏显示的提示
        builder.setTicker(bundle.getString(JPushInterface.EXTRA_TITLE));
        //第一行内容  通常作为通知栏标题
        builder.setContentTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
        //第二行内容 通常是通知正文
        builder.setContentText(bundle.getString(JPushInterface.EXTRA_MESSAGE));
        //可以点击通知栏的删除按钮删除
        builder.setAutoCancel(true);
        //系统状态栏显示的小图标
        builder.setSmallIcon(R.mipmap.logo);
        show(context, bundle, builder);
        Notification notification = builder.build();
        builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Intent intent = new Intent();
        skip(context, bundle, intent);
        int id = SuiJiShu.Num();
        //getBroadcast
        MLog.e("===================id=" + id);
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.contentIntent = contentIntent;
        manger.notify(id, notification);
    }

    private void skip(final Context context, Bundle bundle, final Intent intent) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SkipInfoEntity indexInfo = new SkipInfoEntity();
        String hint = null;
        indexInfo.isPush = true;
        try {
            JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
            indexInfo.pushId = json.getString("orderSn");
            if (json.getString("isSender").equals("1")) {//发单
                intent.setClass(context, BillOrderInfoActivity.class);
            } else {//接单
                if (json.getString("orderType").equals("1")) {//抢单
                    intent.setClass(context, GradInfoActivity.class);
                } else if (json.getString("orderType").equals("0")) {//一对一
                    intent.setClass(context, TakingOrderInfoActivity.class);
                }
            }
            if (json.getString("type").equals("15")) {
                indexInfo.pushId = json.getString("articleId");
                intent.setClass(context, ZdbInfoActivity.class);
            }
            if (json.getString("type").equals("16")) {
                indexInfo.pushId = json.getString("articleId");
                intent.setClass(context, ZdbMsgInfoActivity.class);
            }
            if (StrUtils.isNotNull(json.getString("refundId")) && !json.getString("refundId").equals("0")) {
                indexInfo.pushId = json.getString("refundId");
                intent.setClass(context, SalesInfoActivity.class);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra(Constants.SKIP_INFO_ID, indexInfo);
    }

    private void show(Context context, Bundle bundle, NotificationCompat.Builder mBuilder) {
        AudioManager as = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        try {
            JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
            String soundType = json.getString("type");
            MLog.e("soundType=="+soundType);
            Date d = new Date();
            int hours = d.getHours();
            if (!(Integer.parseInt(PerfHelper.getStringData(Constants.REMIND_START_TIME)) <= hours && hours < Integer.parseInt(PerfHelper.getStringData(Constants.REMIND_STOP_TIME)))) {
                //提醒时间不提醒
                mBuilder.setSound(null);
                mBuilder.setVibrate(null);
            } else {
                switch (as.getRingerMode()) {
                    case AudioManager.RINGER_MODE_SILENT:  //静音
                        mBuilder.setSound(null);
                        mBuilder.setVibrate(null);
                        break;
                    case AudioManager.RINGER_MODE_VIBRATE://震动模式，值为1，这时候震动，不响铃
                        mBuilder.setSound(null);
                        mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                        break;
                    case AudioManager.RINGER_MODE_NORMAL://常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
                        boolean isSoundFlagTime = "1".equals(PerfHelper.getStringData(Constants.REMIND_VOICE)) ? true : false; //声音
                        boolean isLibrateFlagTime = "1".equals(PerfHelper.getStringData(Constants.REMIND_LIBRATE)) ? true : false;//震动
                        //获取软件的设置
                        if (isSoundFlagTime && isLibrateFlagTime) {//声音和震动同时开启
                            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
                            if (StrUtils.isNull(soundType)){
                                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                            }else {
                                if (soundType.equals("3")) {
                                    if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_NEW)) && PerfHelper.getStringData(Constants.SOUND_NEW).equals("1")) {
                                        mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.jiedan));
                                    } else {
                                        mBuilder.setSound(null);
                                    }
                                } else if (soundType.equals("4")) {
                                    if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_STATUS)) && PerfHelper.getStringData(Constants.SOUND_STATUS).equals("1")) {
                                        mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.changestate));
                                    } else {
                                        mBuilder.setSound(null);
                                    }
                                } else if (soundType.equals("2")) {
                                    if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE)) && PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE).equals("1")) {
                                        mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.jiedan));
                                    }else {
                                        mBuilder.setSound(null);
                                    }
                                } else if (soundType.equals("8")) {
                                    if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_SALES)) && PerfHelper.getStringData(Constants.SOUND_SALES).equals("1")) {
                                        mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.tuikuan));
                                    } else {
                                        mBuilder.setSound(null);
                                    }
                                }else {
                                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                                }
                            }
                        } else {
                            if (isSoundFlagTime && isLibrateFlagTime == false) {//声音开启不开震动
                                if (StrUtils.isNull(soundType)) {
                                    mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                                } else {
                                    if (soundType.equals("3")) {
                                        if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_NEW)) && PerfHelper.getStringData(Constants.SOUND_NEW).equals("1")) {
                                            mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.jiedan));
                                        } else {
                                            mBuilder.setSound(null);
                                        }
                                    } else if (soundType.equals("4")) {
                                        if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_STATUS)) && PerfHelper.getStringData(Constants.SOUND_STATUS).equals("1")) {
                                            mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.changestate));
                                        } else {
                                            mBuilder.setSound(null);
                                        }
                                    } else if (soundType.equals("2")) {
                                        if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE)) && PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE).equals("1")) {
                                            mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.yijiedan));
                                        }else {
                                            mBuilder.setSound(null);
                                        }
                                    } else if (soundType.equals("8")) {
                                        if (StrUtils.isNotNull(PerfHelper.getStringData(Constants.SOUND_SALES)) && PerfHelper.getStringData(Constants.SOUND_SALES).equals("1")) {
                                            mBuilder.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.tuikuan));
                                        } else {
                                            mBuilder.setSound(null);
                                        }
                                    }else {
                                        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                                    }
                                }
                                mBuilder.setVibrate(null);
                            } else if (isSoundFlagTime == false && isLibrateFlagTime) {//开启震动不开启声音
                                mBuilder.setSound(null);
                                mBuilder.setDefaults(Notification.DEFAULT_SOUND);
                            }
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    MLog.i(TAG, "This message has no Extra data");
                    continue;
                }
                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" + myKey + " - " + json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    MLog.e(TAG, "Get message extra JSON error!");
                }
            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }
}
