package com.zhuandanbao.app.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View.OnClickListener;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhuandanbao.app.activity.PayResultActivity;
import com.zhuandanbao.app.appdelegate.WxPayD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.utils.MLog;

public class WXPayEntryActivity extends BaseHttpActivity<WxPayD> implements IWXAPIEventHandler, OnClickListener {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        api.unregisterApp();
    }

    @Override
    protected Class<WxPayD> getDelegateClass() {
        return WxPayD.class;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(final BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            MLog.e("errCode=" + resp.errCode + ";errStr=" + resp.errStr);
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.index=2;//微信
            skipInfoEntity.payCode=resp.errCode+"";
            viewDelegate.goToActivity(skipInfoEntity, PayResultActivity.class,0);
            finish();
            switch (resp.errCode) {
                case 800://商户订单号重复或生成错误
                    break;
                case 0://恭喜！支付成功
                    break;
                case -1://支付失败
                    break;
                case -2://取消支付
                    break;
            }
        }
    }
}