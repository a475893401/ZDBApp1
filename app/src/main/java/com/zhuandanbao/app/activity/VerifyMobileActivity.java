package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.AmendMobileD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopAuthInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 验证手机号
 * Created by BFTECH on 2017/2/24.
 */

public class VerifyMobileActivity extends BaseHttpActivity<AmendMobileD> {

    private Button button;
    private TextView smsHint;
    private TextView mobileHint;
    private EditText oldMobile;
    private EditText newMobile;
    private EditText verifyCode;
    private TextView getVerifyBut;
    private ShopInfoItemEntity shopInfo;
    private ShopAuthInfoEntity authInfoEntity;
    private LinearLayout oldAmendLayout;
    private LinearLayout newAmendLayout;

    @Override
    protected Class<AmendMobileD> getDelegateClass() {
        return AmendMobileD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setTitle("验证绑定手机号");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        button=viewDelegate.get(R.id.but_blue_hg);
        button.setText("下一步");
        smsHint=viewDelegate.get(R.id.amend_sms_hint);
        mobileHint=viewDelegate.get(R.id.amend_hint_text);
        oldMobile=viewDelegate.get(R.id.amend_old_mobile);
        newMobile=viewDelegate.get(R.id.amend_new_mobile);
        verifyCode=viewDelegate.get(R.id.amend_verify_code);
        getVerifyBut=viewDelegate.get(R.id.amend_get_verify_but);
        oldAmendLayout=viewDelegate.get(R.id.amend_old_layout);
        newAmendLayout=viewDelegate.get(R.id.amend_new_mobile);
        button.setOnClickListener(this);
        getVerifyBut.setOnClickListener(this);
        getShopInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            authInfoEntity = MJsonUtils.jsonToShopAuthInfoEntity(shopInfo.getShop_auth_info());
            if (authInfoEntity.getAuth_mobile().equals("1")) {
                mobileHint.setVisibility(View.VISIBLE);
                oldMobile.setText(StrUtils.setGetPhoneMask(shopInfo.getMobile()));
                oldMobile.setEnabled(false);
            } else {
                oldMobile.setText(shopInfo.getMobile());
                oldMobile.setEnabled(true);
            }
        }
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 1;
        loadingId=2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }
}
