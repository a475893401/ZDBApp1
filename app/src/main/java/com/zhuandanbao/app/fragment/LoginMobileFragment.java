package com.zhuandanbao.app.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.RegisterActivity;
import com.zhuandanbao.app.activity.ShopGuide1Activity;
import com.zhuandanbao.app.activity.ShopGuide2Activity;
import com.zhuandanbao.app.activity.ShopGuide3Activity;
import com.zhuandanbao.app.appdelegate.LoginMobileD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.VipLoginInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpFragment;
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
 * 手机号登录
 * Created by BFTECH on 2017/2/20.
 */

public class LoginMobileFragment extends BaseHttpFragment<LoginMobileD> {

    private VipLoginInfoEntity vipInfo;
    private EditText mobile;
    private EditText verify;
    private TextView verifyBut;


    public static LoginMobileFragment getInstance() {
        LoginMobileFragment fragment = new LoginMobileFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        mobile = viewDelegate.get(R.id.mobile_et);
        verify = viewDelegate.get(R.id.verify_et);
        verifyBut = viewDelegate.get(R.id.get_verify_but);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.get_verify_but, R.id.login_but, R.id.reg_but);
    }

    @Override
    protected void initD() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.get_verify_but) {
            getVerify();
        }
        if (view.getId() == R.id.login_but) {
            login();
        }
        if (view.getId() == R.id.reg_but) {
            viewDelegate.goToActivity(null, RegisterActivity.class, 200);
        }
    }

    @Override
    protected Class<LoginMobileD> getDelegateClass() {
        return LoginMobileD.class;
    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code,baseEntity);
        switch (code) {
            case 1:
                TimeCountUtil countUtil = new TimeCountUtil(activity, 120 * 1000, 1000, verifyBut);
                countUtil.start();
                viewDelegate.showSuccessHint("验证码发送成功", null);
                break;
            case 2:
                viewDelegate.showSuccessHint("登录成功", new KProgressDismissClickLister() {
                    @Override
                    public void onDismiss() {
                        vipInfo = MJsonUtils.josnToVipLoginInfoEntity(baseEntity.data);
                        JGUtil.setAlias(getActivity(), vipInfo.getSid(), new TagAliasCallback() {
                            @Override
                            public void gotResult(int i, String s, Set<String> set) {
                                PerfHelper.setInfo(Constants.JG_PUSH, "1");
                                MLog.e("i=" + i + ";s+" + s);
                            }
                        });
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
                        activity.finish();
                    }
                });
                break;
        }
    }

    /**
     * 登录
     */
    private void login() {
        if (!StrUtils.isMobileNum(mobile.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入正确的手机号", 1, null);
            return;
        }
        if (StrUtils.isNull(verify.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入验证码", 1, null);
            return;
        }
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put("mobile", mobile.getText().toString().trim());
        params.put("logintype", "smslogin");
        params.put("smscode", verify.getText().toString().trim());
        baseHttp("正在登录...", true, Constants.API_ACTION_LOGIN, params);
    }

    /**
     * 获取登陆验证码
     */
    private void getVerify() {
        if (!StrUtils.isMobileNum(mobile.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入正确的手机号", 1, null);
            return;
        }
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put("mobile", mobile.getText().toString().trim());
        params.put("logintype", "getSmsCode");
        baseHttp("获取验证码...", true, Constants.API_ACTION_LOGIN, params);
    }
}
