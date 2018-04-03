package com.zhuandanbao.app.fragment;

import android.view.View;
import android.widget.EditText;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.RegisterActivity;
import com.zhuandanbao.app.activity.ShopGuide1Activity;
import com.zhuandanbao.app.activity.ShopGuide2Activity;
import com.zhuandanbao.app.activity.ShopGuide3Activity;
import com.zhuandanbao.app.appdelegate.LoginAccountD;
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

import java.util.Set;

import cn.jpush.android.api.TagAliasCallback;

/**
 * 账号登录
 * Created by BFTECH on 2017/2/20.
 */

public class LoginAccountFragment extends BaseHttpFragment<LoginAccountD> {

    private VipLoginInfoEntity vipInfo;
    private EditText account;
    private EditText password;

    public static LoginAccountFragment getInstance() {
        LoginAccountFragment fragment = new LoginAccountFragment();
        return fragment;
    }

    @Override
    public void initView() {
        super.initView();
        account = viewDelegate.get(R.id.account_et);
        password = viewDelegate.get(R.id.password_et);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.login_but,R.id.reg_but);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.login_but){
            login();
        }
        if (view.getId()==R.id.reg_but){
            viewDelegate.goToActivity(null, RegisterActivity.class,200);
        }
    }

    @Override
    protected void initD() {
    }

    @Override
    protected Class<LoginAccountD> getDelegateClass() {
        return LoginAccountD.class;
    }

    @Override
    public void initData(int code, final BaseEntity baseEntity) {
        super.initData(code,baseEntity);
        if (code==1) {
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
                    MLog.e("openid=" + vipInfo.getOpenid() + "vipInfo.getStype()=" + vipInfo.getStype());
                    PerfHelper.setInfo(Constants.APP_OPENID, vipInfo.getOpenid());
                    SkipInfoEntity infoEntity = new SkipInfoEntity();
                    infoEntity.data = vipInfo;
                    if (vipInfo.getStype().equals("0")) {//没有设置
                        viewDelegate.goToActivity(infoEntity, ShopGuide1Activity.class, 0);
                    }
                    if (vipInfo.getStype().equals("1")) {//1 发单
                        if (vipInfo.getIs_setbaseinfo().equals("1")
                                && vipInfo.getIs_setContactUser().equals("1")
                                && vipInfo.getIs_set_paypass().equals("1")) {//完善
                            infoEntity.index = 1;
                            viewDelegate.goToActivity(infoEntity, MainActivity.class, 0);
                        } else {
                            viewDelegate.goToActivity(infoEntity, ShopGuide2Activity.class, 0);
                        }
                    }
                    if (vipInfo.getStype().equals("2") || vipInfo.getStype().equals("3")) {//接单处理 已完善
                        if (vipInfo.getIs_setbaseinfo().equals("1")
                                && vipInfo.getIs_setContactUser().equals("1")
                                && (vipInfo.getAudit_state().equals("1") || vipInfo.getAudit_state().equals("2"))) {
                            infoEntity.index = 0;
                            viewDelegate.goToActivity(infoEntity, MainActivity.class, 0);
                        } else {
                            viewDelegate.goToActivity(infoEntity, ShopGuide3Activity.class, 0);
                        }
                    }
                    activity.finish();
                }
            });
        }
    }

    /**
     * 登录
     */
    private void login() {
        MLog.e("login()");
        if (StrUtils.isNull(account.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入登录账号", 1, null);
            return;
        }
        if (StrUtils.isNull(password.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入登录密码", 1, null);
            return;
        }
        requestId=1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put("mobile", account.getText().toString().trim());
        params.put("logintype", "passlogin");
        params.put("password", password.getText().toString().trim());
        baseHttp("正在登录...", isPost, Constants.API_ACTION_LOGIN, params);
    }
}
