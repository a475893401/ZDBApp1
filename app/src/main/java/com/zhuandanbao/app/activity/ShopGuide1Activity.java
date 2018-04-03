package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ShopGuide1D;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.VipLoginInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.PerfHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 选择接单方或发单方
 * Created by BFTECH on 2017/4/5.
 */

public class ShopGuide1Activity extends BaseHttpActivity<ShopGuide1D> {
    private SkipInfoEntity skipInfoEntity;
    private VipLoginInfoEntity vipInfo;

    @Override
    protected Class<ShopGuide1D> getDelegateClass() {
        return ShopGuide1D.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            vipInfo= (VipLoginInfoEntity) skipInfoEntity.data;
        }
        viewDelegate.setOnClickListener(this,R.id.bill_layout,R.id.taking_layout);
        TextView billNum=viewDelegate.get(R.id.bill_num);
        TextView takingNum=viewDelegate.get(R.id.taking_num);
        billNum.setText(vipInfo.getFadan_stroe_num()+"家网店通过转单宝管理订单");
        takingNum.setText(vipInfo.getJiedan_stroe_num()+"家店铺累计接到"+vipInfo.getJiedan_chengjiao_num()+"张订单");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.bill_layout){
            upData(1);
        }
        if (view.getId()==R.id.taking_layout){
            upData(2);
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            viewDelegate.goToActivity(skipInfoEntity,ShopGuide2Activity.class,300);
            finish();
        }
        if (code==2){
            viewDelegate.goToActivity(skipInfoEntity,ShopGuide3Activity.class,300);
            finish();
        }
    }

    private void upData(int type){
        requestId=type;
        loadingId=1;
        HttpParams params=new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("type",type+"");
        baseHttp("正在提交...",true,Constants.API_UPDATA_SHOP_STYE,params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode==200){
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
            // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
        } else {
            this.getApplication().onTerminate();
            finish();
        }
    }
}
