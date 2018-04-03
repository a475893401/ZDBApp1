package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ShopGuide2D;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.VipLoginInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 发单方
 * Created by BFTECH on 2017/4/5.
 */

public class ShopGuide2Activity extends BaseHttpActivity<ShopGuide2D> {
    private SkipInfoEntity skipInfoEntity;
    private VipLoginInfoEntity vipInfo;
    private LinearLayout infoLayout;
    private TextView infoTx;
    private ImageView infoImg;
    private LinearLayout contactLayout;
    private TextView contactTx;
    private ImageView contactImg;
    private LinearLayout passLayout;
    private TextView passTx;
    private ImageView passImg;
    private int finishColor;
    private int unFinishColor;

    @Override
    protected Class<ShopGuide2D> getDelegateClass() {
        return ShopGuide2D.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            vipInfo= (VipLoginInfoEntity) skipInfoEntity.data;
        }
        viewDelegate.setOnClickListener(this, R.id.shop_info_layout, R.id.shop_contact_layout, R.id.shop_pay_pass_layout,R.id.shop_guide2_finish,R.id.shop_guide2_back);
        infoLayout = viewDelegate.get(R.id.shop_info_layout);
        infoTx = viewDelegate.get(R.id.shop_info_hint_tx);
        infoImg = viewDelegate.get(R.id.shop_info_hint_img);
        contactLayout = viewDelegate.get(R.id.shop_contact_layout);
        contactTx = viewDelegate.get(R.id.shop_contact_hint_tx);
        contactImg = viewDelegate.get(R.id.shop_contact_hint_img);
        passLayout = viewDelegate.get(R.id.shop_pay_pass_layout);
        passTx = viewDelegate.get(R.id.shop_pay_pass_hint_tx);
        passImg = viewDelegate.get(R.id.shop_pay_pass_hint_img);
        finishColor= Color.parseColor("#f29000");
        unFinishColor=Color.parseColor("#5e9eed");
        init(vipInfo.getIs_setbaseinfo(),vipInfo.getIs_setContactUser(),vipInfo.getIs_set_paypass());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        SkipInfoEntity infoEntity=new SkipInfoEntity();
        if (view.getId() == R.id.shop_info_layout) {
            infoEntity.index=1;
            viewDelegate.goToActivity(infoEntity,ShopInfoActivity.class,300);
        }
        if (view.getId() == R.id.shop_contact_layout) {
            viewDelegate.goToActivity(null,UpdataTouchActivity.class,300);
        }
        if (view.getId() == R.id.shop_pay_pass_layout) {
            viewDelegate.goToActivity(null,PayPasswordActivity.class,300);
        }
        if (view.getId()==R.id.shop_guide2_finish){
            infoEntity.index=4;
            viewDelegate.goToActivity(infoEntity, MainActivity.class,0);
            finish();
        }
        if (view.getId()==R.id.shop_guide2_back){
            viewDelegate.goToActivity(skipInfoEntity,ShopGuide1Activity.class,0);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&(resultCode==Constants.SHOP_GUIDE_INFO_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_CONTACT_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_PASS_SUCCESS)){
            getShopInfo();
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            MainApplication.cache.put(Constants.SHOP_INFO, shopInfoEntity.getShopInfo());
            ShopInfoItemEntity shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            init(shopInfo.getIs_setbaseinfo(),shopInfo.getIs_setContactUser(),shopInfo.getIs_set_paypass());
        }
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        loadingId=0;
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }

    private void init(String infoState,String contactState,String passState){
        MLog.e("infoState="+infoState+";contactState="+contactState+";passState="+passState);
        if (infoState.equals("1")){//店铺信息已完善
            infoTx.setTextColor(finishColor);
            infoTx.setText("已完善");
            infoImg.setImageResource(R.mipmap.img_shop_guide_check);
            infoLayout.setClickable(false);
        }else {
            infoTx.setTextColor(unFinishColor);
            infoTx.setText("去完善");
            infoImg.setImageResource(R.mipmap.img_shop_guide_right);
            infoLayout.setClickable(true);
        }
        if (contactState.equals("1")){//店铺联系人
            contactTx.setTextColor(finishColor);
            contactTx.setText("已完善");
            contactImg.setImageResource(R.mipmap.img_shop_guide_check);
            contactLayout.setClickable(false);
        }else {
            contactTx.setTextColor(unFinishColor);
            contactTx.setText("去添加");
            contactImg.setImageResource(R.mipmap.img_shop_guide_right);
            contactLayout.setClickable(true);
        }
        if (passState.equals("1")){//设置密码
            passTx.setTextColor(finishColor);
            passTx.setText("已完善");
            passImg.setImageResource(R.mipmap.img_shop_guide_check);
            passLayout.setClickable(false);
        }else {
            passTx.setTextColor(unFinishColor);
            passTx.setText("去设置");
            passImg.setImageResource(R.mipmap.img_shop_guide_right);
            passLayout.setClickable(true);
        }
        if (infoState.equals("1")&&contactState.equals("1")&&passState.equals("1")){
            viewDelegate.showSuccessHint("已完善信息", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.goToActivity(skipInfoEntity,ShopGuide4Activity.class,0);
                    setResult(200);
                    finish();
                }
            });
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
