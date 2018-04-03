package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ZuijiaHonestD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ZhuijiaHonestEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 追加诚信保证金
 * Created by BFTECH on 2017/2/21.
 */

public class ZuijiaHonestActivity extends BaseHttpActivity<ZuijiaHonestD> {
    private ZhuijiaHonestEntity zuijiaInfo;
    private TextView amount;
    private TextView gradeMoney;
    private TextInputEditText needMoney;
    private TextInputEditText passWord;
    @Override
    protected Class<ZuijiaHonestD> getDelegateClass() {
        return ZuijiaHonestD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("追加诚信保证金");
        amount = (TextView) findViewById(R.id.honest_money);
        gradeMoney = (TextView) findViewById(R.id.honest_grade_money);
        needMoney = (TextInputEditText) findViewById(R.id.need_honest_money);
        passWord = (TextInputEditText) findViewById(R.id.pass_word);
        viewDelegate.setOnClickListener(this,R.id.ok_but,R.id.forget_password);
        getZhuijiaInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            zuijiaInfo= MJsonUtils.jsonToZhuijiaHonestEntity(baseEntity.data);
            amount.setText(Html.fromHtml(StrUtils.setHtmlRed(null,zuijiaInfo.credit_money," 元")));
            gradeMoney.setText(zuijiaInfo.amount+" 元");
            double money = Double.parseDouble(zuijiaInfo.credit_money);
            if (money < 2000) {
                double m = 2000 - money;
                needMoney.setText(String.valueOf(Math.round(m * 100) * 0.01));
            }
        }
        if (code==2){
            viewDelegate.showSuccessHint(baseEntity.message, new KProgressDismissClickLister() {
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
        if (view.getId()==R.id.ok_but){
            zhuijiaMoney();
        }
        if (view.getId()==R.id.forget_password){
            viewDelegate.goToActivity(null,ForgetPasswordActivity.class,0);
        }
    }

    /**
     * 獲取追加信息
     */
    private void getZhuijiaInfo() {
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("正在获取...",true,Constants.API_ZHUI_JIA_HONEST_INFO,params);
    }

    /**
     * 追加保證金
     */
    private void zhuijiaMoney(){
        if (StrUtils.isNull(needMoney.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入补交金额",1,null);
            return;
        }
        if (Double.parseDouble(needMoney.getText().toString().trim()) <= 0) {
            viewDelegate.showErrorHint("请输入正确的金额",1,null);
            return;
        }
        if (StrUtils.isNull(passWord.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入支付密码",1,null);
            return;
        }
        requestId=2;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("paypass",passWord.getText().toString().trim());
        params.put("creditLevel", zuijiaInfo.amount);
        params.put("amount", needMoney.getText().toString().trim());
        baseHttp("正在提交...",true,Constants.API_ZHUI_JIA_HONEST_MONEY,params);
    }
}
