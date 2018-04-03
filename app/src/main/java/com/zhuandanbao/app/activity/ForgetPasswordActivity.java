package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ForgetPasswordD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopAuthInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.TimeCountUtil;

/**
 * 忘记密码
 * Created by BFTECH on 2017/2/24.
 */

public class ForgetPasswordActivity extends BaseHttpActivity<ForgetPasswordD> {

    private Button button;
    private ShopInfoItemEntity shopInfo;
    private ShopAuthInfoEntity authInfoEntity;
    private EditText mobile;
    private EditText pass;
    private EditText rePass;
    private EditText verify;
    private TextView verifyBut;
    private TimeCountUtil countUtil;

    @Override
    protected Class<ForgetPasswordD> getDelegateClass() {
        return ForgetPasswordD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        getShopInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            if (shopInfo.getIs_set_paypass().equals("0")){
                viewDelegate.goToActivity(null,PayPasswordActivity.class,0);
                finish();
            }else {
                viewDelegate.setTitle("找回支付密码");
                viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
                button = viewDelegate.get(R.id.but_blue_hg);
                mobile = viewDelegate.get(R.id.forget_mobile);
                pass = viewDelegate.get(R.id.forget_pass);
                rePass = viewDelegate.get(R.id.forget_re_pass);
                verify = viewDelegate.get(R.id.forget_verify_code);
                verifyBut = viewDelegate.get(R.id.forget_get_verify_but);
                button.setText("保存");
                button.setOnClickListener(this);
                verifyBut.setOnClickListener(this);
                authInfoEntity = MJsonUtils.jsonToShopAuthInfoEntity(shopInfo.getShop_auth_info());
                mobile.setText(shopInfo.getMobile());
            }
        }
        if (requestId == 2) {
            countUtil = new TimeCountUtil(activity, 120 * 1000, 1000, verifyBut);
            countUtil.start();
            viewDelegate.showSuccessHint("验证码发送成功", null);
        }
        if (requestId == 3) {
            viewDelegate.showSuccessHint("保存成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_blue_hg) {
            if (isNull(false)) {
                requestId = 3;
                loadingId = 1;
                HttpParams params = new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                params.put("password", pass.getText().toString().trim());
                params.put("smscode", verify.getText().toString().trim());
                baseHttp("正在保存...", true, Constants.API_ACTION_SHOP_PAYPASS, params);
            }
        }
        if (view.getId() == R.id.forget_get_verify_but) {
            if (isNull(true)) {
                requestId = 2;
                loadingId = 1;
                HttpParams params = new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                baseHttp("正在发送验证码...", true, Constants.API_ACTION_SHOP_PAYPASS, params);
            }
        }
    }

    /**
     * 验证
     *
     * @param isGetVerify
     * @return
     */
    private boolean isNull(boolean isGetVerify) {
        String passStr = pass.getText().toString().trim();
        if (StrUtils.isNull(passStr)) {
            viewDelegate.showErrorHint("请输入新的支付密码", 1, null);
            return false;
        } else {
            if (passStr.length() < 6) {
                viewDelegate.showErrorHint("请输入大于6位的支付密码", 1, null);
                return false;
            } else if (passStr.length() > 16) {
                viewDelegate.showErrorHint("请输入小于16位的支付密码", 1, null);
                return false;
            }
        }
        if (!passStr.equals(rePass.getText().toString().trim())) {
            viewDelegate.showErrorHint("两次输入的密码不一样", 1, null);
            return false;
        }

        if (StrUtils.isNull(verify.getText().toString().trim()) && !isGetVerify) {
            viewDelegate.showErrorHint("请输入验证码", 1, null);
            return false;
        }
        return true;
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != countUtil) {
            countUtil.cancel();
        }
    }
}
