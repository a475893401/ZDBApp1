package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.AmendPasswordD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 修改登陆密码
 * Created by BFTECH on 2017/2/28.
 */

public class AmendPasswrodActivity extends BaseHttpActivity<AmendPasswordD> {

    private Button button;
    private EditText oldPass;
    private EditText newPass;
    private EditText rePass;
    @Override
    protected Class<AmendPasswordD> getDelegateClass() {
        return AmendPasswordD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("修改登录密码");
        button=viewDelegate.get(R.id.but_blue_hg);
        oldPass=viewDelegate.get(R.id.amend_old_pass);
        newPass=viewDelegate.get(R.id.amend_new_pass);
        rePass=viewDelegate.get(R.id.amend_re_pass);
        button.setText("确认修改");
        button.setOnClickListener(this);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            viewDelegate.showSuccessHint("修改密码成功", new KProgressDismissClickLister() {
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
        if (view.getId()==R.id.but_blue_hg){
            setPass();
        }
    }

    /**
     * 提交修改
     */
    private void setPass(){
        if (StrUtils.isNull(oldPass.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入以前的登陆密码",1,null);
            return;
        }
        if (StrUtils.isNull(newPass.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入新的登陆密码",1,null);
            return;
        } else if (newPass.getText().toString().trim().length() < 6) {
            viewDelegate.showErrorHint("请输入大于6位的密码",1,null);
            return;
        } else if (newPass.getText().toString().trim().length() > 16) {
            viewDelegate.showErrorHint("请输入小于16位的密码",1,null);
            return;
        }
        if (!newPass.getText().toString().trim().equals(rePass.getText().toString().trim())) {
            viewDelegate.showErrorHint("两次输入密码不一样",1,null);
            return;
        }
        requestId=1;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("oldpass", oldPass.getText().toString().trim());
        params.put("password", newPass.getText().toString().trim());
        baseHttp("正在提交...",true,Constants.API_ACTION_SHOP_UPLOGIN_PWD,params);
    }
}
