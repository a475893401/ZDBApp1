package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.PayResultD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.AlipayEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.WeixinEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;

/**
 * 支付结果
 * Created by BFTECH on 2017/3/13.
 */

public class PayResultActivity  extends BaseHttpActivity<PayResultD> {
    private SkipInfoEntity skipInfoEntity;
    private ImageView img;
    private TextView hint;
    private TextView money;
    private TextView code;
    private Button cancel;
    private Button ok;

    @Override
    protected Class<PayResultD> getDelegateClass() {
        return PayResultD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("支付结果");
        img=viewDelegate.get(R.id.pay_result_img);
        hint=viewDelegate.get(R.id.pay_result_hint);
        money=viewDelegate.get(R.id.pay_result_money);
        code=viewDelegate.get(R.id.pay_result_code);
        cancel=viewDelegate.get(R.id.pay_result_but_cancel);
        ok=viewDelegate.get(R.id.pay_result_but_ok);
        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);
        if (skipInfoEntity.index==1){//支付宝充值
            AlipayEntity alipayEntity= (AlipayEntity) MainApplication.cache.getAsObject(Constants.PAY_INFO);
            money.setText(alipayEntity.getPay_amount());
            code.setText(alipayEntity.getPay_sn());
            if (skipInfoEntity.payCode.equals("9000")){
                img.setImageResource(R.mipmap.a_finance_pay_success);
                hint.setText("恭喜！充值成功");
            }else {
                img.setImageResource(R.mipmap.a_finance_pay_fail);
                hint.setText("充值失败");
            }
        }else {
            WeixinEntity weixinEntity= (WeixinEntity) MainApplication.cache.getAsObject(Constants.PAY_INFO);
            money.setText(weixinEntity.getPay_amount());
            code.setText(weixinEntity.getPay_sn());
            if (skipInfoEntity.payCode.equals("0")){
                img.setImageResource(R.mipmap.a_finance_pay_success);
                hint.setText("恭喜！充值成功");
            }else if (skipInfoEntity.payCode.equals("-2")){//取消充值
                img.setImageResource(R.mipmap.a_finance_pay_fail);
                hint.setText("取消充值");
            }else {
                img.setImageResource(R.mipmap.a_finance_pay_fail);
                hint.setText("充值失败");
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.pay_result_but_cancel){
            finish();
        }
        if (view.getId()==R.id.pay_result_but_ok){
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.msg=money.getText().toString().trim();
            viewDelegate.goToActivity(skipInfoEntity,RechargeActivity.class,0);
            finish();
        }
    }
}
