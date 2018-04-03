package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.RegisterD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.VipLoginInfoEntity;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.JGUtil;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.TimeCountUtil;

import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

/**
 * 注册账号
 * Created by BFTECH on 2017/2/20.
 */

public class RegisterActivity extends BaseHttpActivity<RegisterD> {

    private EditText mobile;
    private EditText password;
    private EditText twoPassword;
    private EditText verify;
    private TextView getVerifyBut;
    private VipLoginInfoEntity vipInfo;
    @Override
    protected Class<RegisterD> getDelegateClass() {
        return RegisterD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        loginOut();
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("手机注册");
        mobile=viewDelegate.get(R.id.reg_mobile);
        password=viewDelegate.get(R.id.reg_password);
        twoPassword=viewDelegate.get(R.id.reg_two_password);
        verify=viewDelegate.get(R.id.reg_verify);
        getVerifyBut=viewDelegate.get(R.id.get_verify_but);
        viewDelegate.setOnClickListener(this,R.id.get_verify_but,R.id.reg_but);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.get_verify_but){
            getVerify();
        }
        if (view.getId()==R.id.reg_but){
            register();
        }
    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code,baseEntity);
        if (code==1){
            TimeCountUtil countUtil = new TimeCountUtil(activity, 120 * 1000, 1000, getVerifyBut);
            countUtil.start();
            viewDelegate.showSuccessHint("验证码发送成功",null);
        }
        if (code==2){
            viewDelegate.showSuccessHint("注册成功", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    vipInfo = MJsonUtils.josnToVipLoginInfoEntity(baseEntity.data);
                    JGUtil.setAlias(activity, vipInfo.getSid(), new TagAliasCallback() {
                        @Override
                        public void gotResult(int i, String s, Set<String> set) {
                            PerfHelper.setInfo(Constants.JG_PUSH, "1");
                            MLog.e("i=" + i + ";s+" + s);
                        }
                    });
                    MLog.e("openid=" + vipInfo.getOpenid()+"vipInfo.getStype()="+vipInfo.getStype());
                    PerfHelper.setInfo(Constants.APP_OPENID, vipInfo.getOpenid());
                    SkipInfoEntity infoEntity=new SkipInfoEntity();
                    infoEntity.data=vipInfo;
                    if (vipInfo.getStype().equals("0")){//没有设置
                        viewDelegate.goToActivity(infoEntity, ShopGuide1Activity.class,0);
                    }
                    if (vipInfo.getStype().equals("1")){//1 发单
                        if (vipInfo.getIs_setbaseinfo().equals("1")
                                &&vipInfo.getIs_setContactUser().equals("1")
                                &&vipInfo.getIs_set_paypass().equals("1")){//完善
                            infoEntity.index=1;
                            viewDelegate.goToActivity(infoEntity, MainActivity.class,0);
                        }else {
                            viewDelegate.goToActivity(infoEntity, ShopGuide2Activity.class,0);
                        }
                    }
                    if (vipInfo.getStype().equals("2")||vipInfo.getStype().equals("3")){//接单处理 已完善
                        if (vipInfo.getIs_setbaseinfo().equals("1")
                                &&vipInfo.getIs_setContactUser().equals("1")
                                &&(vipInfo.getAudit_state().equals("1")||vipInfo.getAudit_state().equals("2"))) {
                            infoEntity.index = 0;
                            viewDelegate.goToActivity(infoEntity, MainActivity.class, 0);
                        }else {
                            viewDelegate.goToActivity(infoEntity, ShopGuide3Activity.class,0);
                        }
                    }
                    setResult(200);
                    activity.finish();
                }
            });
        }
    }

    private void getVerify(){
        if (!StrUtils.isMobileNum(mobile.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入正确手机号",1,null);
            return;
        }
        requestId=1;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put("mobile",mobile.getText().toString().trim());
        baseHttp("获取验证码...",true, Constants.API_ACTION_REGISTER,params);
    }

    private void register(){
        if (!StrUtils.isMobileNum(mobile.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入正确手机号",1,null);
            return;
        }
        if (StrUtils.isNull(password.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入登录密码",1,null);
            return;
        }else if (password.getText().toString().trim().length()<6){
            viewDelegate.showErrorHint("请输入大于6位的密码",1,null);
            return;
        }
        if (!password.getText().toString().trim().equals(twoPassword.getText().toString().trim())){
            viewDelegate.showErrorHint("两次输入的密码不相同",1,null);
            return;
        }
        if (StrUtils.isNull(verify.getText().toString().trim())){
            viewDelegate.showErrorHint("请输入验证码",1,null);
            return;
        }
        requestId=2;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put("mobile",mobile.getText().toString().trim());
        params.put("smscode",verify.getText().toString().trim());
        params.put("password",password.getText().toString().trim());
        baseHttp("正在注册...",true,Constants.API_ACTION_REGISTER,params);
    }

    /**
     * 登出Im
     */
    private void loginOut() {
        if (ImLoginHelper.getInstance().getIMKit() == null) {
            return;
        }
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = ImLoginHelper.getInstance().getIMKit().getLoginService();
        mLoginService.logout(new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                MLog.e("退出成功");
            }
            @Override
            public void onProgress(int arg0) {
            }
            @Override
            public void onError(int arg0, String arg1) {
                MLog.e("arg0=" + arg0 + ";arg1=" + arg1);
            }
        });
    }
}
