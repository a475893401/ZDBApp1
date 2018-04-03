package com.zhuandanbao.app.weixin;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.WeixinEntity;
import com.zhuandanbao.app.utils.MLog;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


/**
 * Created by BFTECH on 2016/3/31.
 */
public class WXPay {

    private PayReq req;
    private IWXAPI msgApi;
    private String APP_ID;
    private String MCH_ID;
    private String API_KEY;
    private Context context;
    private Handler handler;
    private WeixinEntity weixinEntity;

    public static String total_fee;// 商品总金额 单位是分
    private String notify_url;// 接收微信支付异步通知回调地址
    private String body;// 商品或支付单简要描述
    public static String outTradNo;// 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
    private String prepay_id;
    private String sign;
    private String result_code;
    private String timeStamp;
    private String nonce_str;

    private WXPay(Context context, Handler handler, WeixinEntity weixinEntity) {
        this.context = context;
        this.handler = handler;
        this.weixinEntity = weixinEntity;
        req = new PayReq();
        msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(Constants.APP_ID);
    }

    public static WXPay getInstance(Context context, Handler handler, WeixinEntity weixinEntity) {
        WXPay wxPay = new WXPay(context, handler, weixinEntity);
        return wxPay;
    }

    public void doPay() {
        if (!isWXAppInstalledAndSupported()) {
            Toast.makeText(context, "请先下载微信", Toast.LENGTH_LONG).show();
            return;
        }
        initData();
    }

    private void initData() {
        Activity activity = (Activity) context;
        total_fee = weixinEntity.getPay_amount();
        notify_url = weixinEntity.getNotify_url();
        body = weixinEntity.getPay_desc();
        outTradNo = weixinEntity.getPay_sn();
        APP_ID = weixinEntity.getAppid();
        MCH_ID = weixinEntity.getMchid();
        API_KEY = weixinEntity.getApikey();
        prepay_id = weixinEntity.getPrepay_id();
        sign = weixinEntity.getSign();
        result_code = weixinEntity.getReturn_code();
        timeStamp = weixinEntity.getTimeStamp();
        nonce_str = weixinEntity.getNonce_str();
        if (weixinEntity.getResult_code().equals("SUCCESS")) {
            Runnable checkRunnable = new Runnable() {
                @Override
                public void run() {
                    // 构造PayTask 对象
                    genPayReq();
                }
            };
            Thread checkThread = new Thread(checkRunnable);
            checkThread.start();
            activity.finish();
        } else {
            Message msg = new Message();
            msg.obj = result_code;
            msg.what = 800;
            handler.sendMessage(msg);
        }
    }

    /**
     * 调起支付
     */
    private void genPayReq() {
        req.appId = APP_ID;
        req.partnerId = MCH_ID;
        req.prepayId = prepay_id;
        req.packageValue = "Sign=WXPay";
        req.nonceStr = nonce_str;
        req.timeStamp = timeStamp;
        Map<String, Object> map = new HashMap<>();
        map.put("appid", req.appId);
        map.put("noncestr", req.nonceStr);
        map.put("package", req.packageValue);
        map.put("partnerid", req.partnerId);
        map.put("prepayid", req.prepayId);
        map.put("timestamp", req.timeStamp);
        req.sign = genPackageSign(map);
        sendPayReq();
    }

    private void sendPayReq() {
        msgApi.registerApp(APP_ID);
        msgApi.sendReq(req);
    }

    /**
     * 生成签名
     */

    private String genPackageSign(Map<String, Object> map) {
        StringBuilder sb = new StringBuilder();
        map = new TreeMap<>(map);
        for (Map.Entry entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append('=');
            sb.append(entry.getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        MLog.e("orion", "packageSign=" + packageSign);
        return packageSign;
    }

    public  boolean isWXAppInstalledAndSupported() {
        boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
        return sIsWXAppInstalledAndSupported;
    }
}
