package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.PayPasswordD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopSafeInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.TimeCountUtil;

/**
 * 设置/修改密码
 * Created by BFTECH on 2017/2/24.
 */

public class PayPasswordActivity extends BaseHttpActivity<PayPasswordD> {

    private Button button;
    private ShopSafeInfoEntity safeInfoEntity;
    private TextView butVerify;
    private EditText newPass;
    private EditText rePass;
    private EditText verifyCode;
    private LinearLayout oldPassLayout;
    private EditText oldPass;
    private TimeCountUtil countUtil;
    private TextView forgetPassword;


    @Override
    protected Class<PayPasswordD> getDelegateClass() {
        return PayPasswordD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        button = viewDelegate.get(R.id.but_blue_hg);
        butVerify = viewDelegate.get(R.id.get_verify_but);
        newPass = viewDelegate.get(R.id.pay_password);
        rePass = viewDelegate.get(R.id.re_pay_password);
        verifyCode = viewDelegate.get(R.id.pay_pass_verify);
        oldPassLayout = viewDelegate.get(R.id.old_pass_layout);
        oldPass = viewDelegate.get(R.id.pay_old_password);
        forgetPassword=viewDelegate.get(R.id.forget_password);
        button.setOnClickListener(this);
        butVerify.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
        try{
            long nowTime=System.currentTimeMillis();
           long time= (long) MainApplication.cache.getAsObject(Constants.PAY_TIME);
            if (time-nowTime>0){
                countUtil=new TimeCountUtil(activity,time-nowTime,1000,butVerify);
                countUtil.start();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        getSafeInfo();
    }


    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            safeInfoEntity = JSON.parseObject(baseEntity.data, ShopSafeInfoEntity.class);
            if (safeInfoEntity.getIs_set_paypass() == 0) {
                viewDelegate.setTitle("设置支付密码");
                button.setText("设置");
                oldPassLayout.setVisibility(View.GONE);
                forgetPassword.setVisibility(View.GONE);
            } else {
                viewDelegate.setTitle("修改支付密码");
                button.setText("修改");
                oldPassLayout.setVisibility(View.VISIBLE);
                forgetPassword.setVisibility(View.VISIBLE);
            }
        }else if (code==4){
            MainApplication.cache.put(Constants.PAY_TIME,120*1000+System.currentTimeMillis());
            countUtil=new TimeCountUtil(activity,120*1000,1000,butVerify);
            countUtil.start();
            viewDelegate.showSuccessHint("验证码已发送至绑定手机",null);
        }else if (code==3){
            viewDelegate.showSuccessHint("支付密码更新成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }else if (code==2){
            viewDelegate.showSuccessHint("支付密码设置成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(Constants.SHOP_GUIDE_PASS_SUCCESS);
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_blue_hg) {
            if (safeInfoEntity.getIs_set_paypass()==0){
                setPayPass();
            }else {
                updataPayPass();
            }
        }
        if (view.getId() == R.id.get_verify_but) {
            if (safeInfoEntity.getIs_set_paypass()==0){
                getVerifyCode(true);
            }else {
                getVerifyCode(false);
            }
        }
        if (view.getId()==R.id.forget_password){
            viewDelegate.goToActivity(null,ForgetPasswordActivity.class,0);
            finish();
        }
    }

    /**
     * 获取用户安全信息
     */
    private void getSafeInfo() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_ACTION_SHOP_SAFEINFO, params);
    }

    /**
     * 设置支付密码
     */
    private void setPayPass() {
        if (isNull(true,true)){
            requestId=2;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("password", newPass.getText().toString().trim());
            params.put("smscode", verifyCode.getText().toString().trim());
            baseHttp("正在提交...",true,Constants.API_ACTION_SHOP_PAYPASS,params);
        }
    }

    /**
     * 设置支付密码
     */
    private void updataPayPass() {
        if (isNull(false,true)){
            requestId=3;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("oldpass", oldPass.getText().toString().trim());
            params.put("password", newPass.getText().toString().trim());
            params.put("smscode", verifyCode.getText().toString().trim());
            baseHttp("正在更新...",true,Constants.API_ACTION_SHOP_PAYPASS,params);
        }
    }

    /**
     * 获取验证碼
     */
    private void getVerifyCode(boolean isSet){
        if (isNull(isSet,false)){
            requestId=4;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            baseHttp("正获取验证码",true,Constants.API_ACTION_SHOP_PAYPASS,params);
        }
    }

    /**
     * 驗證
     *
     * @param isSetPass
     * @return
     */
    private boolean isNull(boolean isSetPass,boolean isGetVerify) {
        if (StrUtils.isNull(oldPass.getText().toString().trim())&&!isSetPass){
            viewDelegate.showErrorHint("请输入以前的支付密码",1,null);
            return false;
        }
        String passStr = newPass.getText().toString().trim();
        if (StrUtils.isNull(passStr)) {
            viewDelegate.showErrorHint("请输入支付密码", 1, null);
            return false;
        } else {
            if (passStr.length() < 6) {
                viewDelegate.showErrorHint("请输入大于6位的支付密码", 1, null);
                return false;
            }
            if (passStr.length() > 16) {
                viewDelegate.showErrorHint("请输入小于16位的支付密码", 1, null);
                return false;
            }
        }
        if (!passStr.equals(rePass.getText().toString().trim())) {
            viewDelegate.showErrorHint("两次输入的密码不相同", 1, null);
            return false;
        }
        if (StrUtils.isNull(verifyCode.getText().toString().trim())&&isGetVerify) {
            viewDelegate.showErrorHint("请输入验证码", 1, null);
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null!=countUtil) {
            countUtil.cancel();
        }
    }
}
