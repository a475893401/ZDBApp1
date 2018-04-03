package com.zhuandanbao.app.im.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.YWAPI;
import com.alibaba.mobileim.YWIMKit;
import com.alibaba.mobileim.YWLoginParam;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.contact.IYWContactHeadClickListener;
import com.alibaba.mobileim.contact.IYWContactService;
import com.alibaba.mobileim.conversation.YWConversation;
import com.alibaba.mobileim.utility.IMPrefsTools;
import com.alibaba.mobileim.utils.IYWCacheService;
import com.lzy.okgo.OkGo;
import com.zhuandanbao.app.activity.ShopDetailsActivity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.callback.StringDialogCallback;
import com.zhuandanbao.app.mvp.view.HttpCallback;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by BFTECH on 2016/12/15.
 */
public class ImLoginHelper {

    private static ImLoginHelper sInstance = new ImLoginHelper();

    public static ImLoginHelper getInstance() {
        return sInstance;
    }

    // 23015524 默認  23556174 測試  23566938 正式
    public static String IM_APP_KEY = "23566938";
    //    public static String IM_USER_ID = PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME);
    public static String Im_PASSWORD = "456321";

    public static final String IM_APP_KEY_NAME = "appkey";
    public static final String IM_USER_ID_NAME = "userId";
    public static final String IM_PASSWORD_NAME = "password";

    // openIM UI解决方案提供的相关API，创建成功后，保存为全局变量使用
    private YWIMKit mIMKit;

    public YWIMKit getIMKit() {
        return mIMKit;
    }

    public void setIMKit(YWIMKit imkit) {
        mIMKit = imkit;
    }

    public void initIMKit(String userId, String appKey) {
        mIMKit = YWAPI.getIMKitInstance(userId, appKey);
    }

    public void imLogin(final Activity activity, String password, final HttpCallback callback) {
        //开始登录
        MLog.e("开始登录=" + PerfHelper.getStringData(IM_USER_ID_NAME) + ";密碼=" + password + ";md5=" + getMD5(password));
        if (StrUtils.isNull(PerfHelper.getStringData(IM_USER_ID_NAME))) {
            return;
        }
        if (null == mIMKit) {
            initIMKit(PerfHelper.getStringData(IM_USER_ID_NAME), IM_APP_KEY);
        }
        initProfileCallback();
        IYWLoginService loginService = mIMKit.getLoginService();
        YWLoginParam loginParam = YWLoginParam.createLoginParam(PerfHelper.getStringData(IM_USER_ID_NAME), getMD5(password));
        loginService.login(loginParam, new IWxCallback() {
            @Override
            public void onSuccess(Object... objects) {
                callback.onSuccess("登录成功");
                ImLoginHelper.getInstance().saveImInfo(activity);
                initIMKit(PerfHelper.getStringData(IM_USER_ID_NAME), IM_APP_KEY);
            }

            @Override
            public void onError(int i, String s) {
                callback.onError(i, s);
            }

            @Override
            public void onProgress(int i) {

            }
        });
    }

    public void saveImInfo(Context context) {
        IMPrefsTools.setStringPrefs(context, IM_USER_ID_NAME, PerfHelper.getStringData(IM_USER_ID_NAME));
        IMPrefsTools.setStringPrefs(context, IM_PASSWORD_NAME, PerfHelper.getStringData(IM_USER_ID_NAME) + Im_PASSWORD);
        IMPrefsTools.setStringPrefs(context, IM_APP_KEY_NAME, IM_APP_KEY);
    }


    /**
     * 注册
     */
    public static void IMReg(final Activity activity, final HttpCallback callback) {
        OkGo.post(Constants.REG_IM_USER).params(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID))
                .execute(new StringDialogCallback(activity) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        MLog.e("s=" + s);
                        try {
                            JSONObject o = new JSONObject(s);
                            if (o.optInt("code") == 0) {
                                sInstance.imLogin(activity, PerfHelper.getStringData(IM_USER_ID_NAME) + Im_PASSWORD, callback);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // md5加密 32位小写
    private String getMD5(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
//            System.out.println("result: " + result);//32位的加密
//            System.out.println("result: " + buf.toString().substring(8,24));//16位的加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void achae() {
        IYWCacheService mCacheService = getIMKit().getCacheService();
        /**
         * 清除缓存
         * @param callback 回调，成功回调IWxCallback.onSuccess()
         */
        mCacheService.clearCache(new IWxCallback() {
            @Override
            public void onSuccess(Object... result) {
                MLog.e("清空缓存成功");
            }

            @Override
            public void onError(int code, String info) {
                MLog.e("清空缓存失败");
            }

            @Override
            public void onProgress(int progress) {
            }
        });
    }


    private static boolean enableUseLocalUserProfile = true;

    //初始化，建议放在登录之前
    public static void initProfileCallback() {
        if (!enableUseLocalUserProfile) {
            //目前SDK会自动获取导入到OpenIM的帐户昵称和头像，如果用户设置了回调，则SDK不会从服务器获取昵称和头像
            return;
        }
        YWIMKit imKit = getInstance().getIMKit();
        if (imKit == null) {
            return;
        }
        final IYWContactService contactManager = imKit.getContactService();
        //头像点击的回调（开发者可以按需设置）
        contactManager.setContactHeadClickListener(new IYWContactHeadClickListener() {
            @Override
            public void onUserHeadClick(Fragment fragment, YWConversation conversation, String userId, String appKey, boolean isConversationListPage) {
//                IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "你点击了用户 " + userId + " 的头像");
                if (!PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME).equals(userId)) {
                    SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
                    skipInfoEntity.pushId = userId;
                    Intent intent = new Intent(fragment.getActivity(), ShopDetailsActivity.class);
                    intent.putExtra(Constants.SKIP_INFO_ID, skipInfoEntity);
                    fragment.startActivity(intent);
                }
            }

            @Override
            public void onTribeHeadClick(Fragment fragment, YWConversation conversation, long tribeId) {
//                IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "你点击了群 " + tribeId + " 的头像");
            }

            @Override
            public void onCustomHeadClick(Fragment fragment, YWConversation conversation) {
//                IMNotificationUtils.getInstance().showToast(fragment.getActivity(), "你点击了自定义会话 " + conversation.getConversationId() + " 的头像");
            }
        });
    }
}
