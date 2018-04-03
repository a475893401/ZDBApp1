package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.text.Html;
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
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 修改绑定手机号
 * Created by BFTECH on 2017/2/24.
 */

public class AmendMobileActivity extends BaseHttpActivity<AmendMobileD> {

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
    @Override
    protected Class<AmendMobileD> getDelegateClass() {
        return AmendMobileD.class;
    }
    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setTitle("修改绑定手机号");
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
        button.setOnClickListener(this);
        getVerifyBut.setOnClickListener(this);
        getShopInfo();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.but_blue_hg){
            if (button.getText().toString().equals("下一步")){
                verify();
            }else {
                amendMobile();
            }
        }
        if (view.getId()==R.id.amend_get_verify_but){//获取验证码
            getVerify();
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            authInfoEntity=MJsonUtils.jsonToShopAuthInfoEntity(shopInfo.getShop_auth_info());
            if (authInfoEntity.getAuth_mobile().equals("1")){
                mobileHint.setVisibility(View.VISIBLE);
                oldMobile.setText(StrUtils.setGetPhoneMask(shopInfo.getMobile()));
                oldMobile.setEnabled(false);
            }else {
                oldMobile.setText(shopInfo.getMobile());
                oldMobile.setEnabled(true);
            }
        }
        if (code==2){
            viewDelegate.showSuccessHint("验证码发送成功",null);
            smsHint.setVisibility(View.VISIBLE);
            smsHint.setText(Html.fromHtml(StrUtils.setHtmlRed("短信验证码已发送至",oldMobile.getText().toString(),"，请注意查收")));
            oldAmendLayout.setVisibility(View.GONE);
            getVerifyBut.setVisibility(View.GONE);
        }
        if (code==3){
            viewDelegate.showSuccessHint("验证成功",null);
            smsHint.setVisibility(View.VISIBLE);
            smsHint.setText(Html.fromHtml(StrUtils.setHtmlRed("短信验证码已发送至",StrUtils.setGetPhoneMask(newMobile.getText().toString().trim()),"，请注意查收")));
            verifyCode.setText("");
            button.setText("确认修改");
        }
        if (code==4){
            viewDelegate.showSuccessHint("修改成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(300);
                    finish();
                }
            });
        }
    }

    /**
     * 获取验证码
     */
    private void getVerify(){
        if (isNull(true)){
            requestId=2;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            if (authInfoEntity.getAuth_mobile().equals("0")) {
                params.put("mobile", shopInfo.getMobile());
            }
            baseHttp("正在获取验证码...",true,Constants.API_ACTION_SHOP_MOBILESAFE,params);
        }
    }

    /**
     * 修改
     */
    private void amendMobile(){
        if (StrUtils.isNull(verifyCode.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入验证码",1,null);
            return;
        }
        requestId=4;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("mobile", newMobile.getText().toString().trim());
        params.put("smscode", verifyCode.getText().toString().trim());
        baseHttp("正在保存...",true,Constants.API_ACTION_SHOP_MOBILESAFE,params);
    }

    /**
     * 验证
     */
    private void verify(){
        if (!StrUtils.isMobileNum(newMobile.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入正确新的绑定手机号",1,null);
            return;
        }
        if (StrUtils.isNull(verifyCode.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入验证码",1,null);
            return;
        }
        requestId=3;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("mobile", newMobile.getText().toString().trim());
        params.put("smscode", verifyCode.getText().toString().trim());
        baseHttp("正在验证...",true,Constants.API_ACTION_SHOP_MOBILESAFE,params);
    }

    /**
     * 判断所需信息是否为null
     * @param isGetVerify
     * @return
     */
    private boolean isNull(boolean isGetVerify){
        if (!StrUtils.isMobileNum(shopInfo.getMobile())){
            viewDelegate.showErrorHint("请输入正确的已绑定手机号",1,null);
            return false;
        }
        if (!StrUtils.isMobileNum(newMobile.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入正确新的绑定手机号",1,null);
            return false;
        }
        if (StrUtils.isNull(verifyCode.getText().toString().trim())&&!isGetVerify){
            viewDelegate.showErrorHint("请输入验证码",1,null);
            return false;
        }
        return true;
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
