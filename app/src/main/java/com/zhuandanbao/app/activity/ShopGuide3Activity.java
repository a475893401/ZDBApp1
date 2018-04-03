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
import com.zhuandanbao.app.appdelegate.ShopGuide3D;
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
 * 接单方
 * Created by BFTECH on 2017/4/5.
 */

public class ShopGuide3Activity extends BaseHttpActivity<ShopGuide3D> {

    private SkipInfoEntity skipInfoEntity;
    private VipLoginInfoEntity vipInfo;
    private LinearLayout infoLayout;
    private TextView infoTx;
    private ImageView infoImg;
    private LinearLayout contactLayout;
    private TextView contactTx;
    private ImageView contactImg;
    private LinearLayout rzLayout;
    private TextView rzTx;
    private ImageView rzImg;
    private int finishColor;
    private int unFinishColor;

    @Override
    protected Class<ShopGuide3D> getDelegateClass() {
        return ShopGuide3D.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            vipInfo = (VipLoginInfoEntity) skipInfoEntity.data;
        }
        viewDelegate.setOnClickListener(this, R.id.shop_info_layout, R.id.shop_contact_layout, R.id.shop_rz_layout,R.id.shop_guide3_finish,R.id.shop_guide3_back);
        infoLayout = viewDelegate.get(R.id.shop_info_layout);
        infoTx = viewDelegate.get(R.id.shop_info_hint_tx);
        infoImg = viewDelegate.get(R.id.shop_info_hint_img);
        contactLayout = viewDelegate.get(R.id.shop_contact_layout);
        contactTx = viewDelegate.get(R.id.shop_contact_hint_tx);
        contactImg = viewDelegate.get(R.id.shop_contact_hint_img);
        rzLayout = viewDelegate.get(R.id.shop_rz_layout);
        rzTx = viewDelegate.get(R.id.shop_rz_hint_tx);
        rzImg = viewDelegate.get(R.id.shop_rz_hint_img);
        finishColor=Color.parseColor("#f29000");
        unFinishColor=Color.parseColor("#5e9eed");
        init(vipInfo.getIs_setbaseinfo(),vipInfo.getIs_setContactUser(),vipInfo.getIs_audit());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.shop_info_layout) {
            SkipInfoEntity infoEntity=new SkipInfoEntity();
            infoEntity.index=1;
            viewDelegate.goToActivity(infoEntity,ShopInfoActivity.class,300);
        }
        if (view.getId() == R.id.shop_contact_layout) {
            viewDelegate.goToActivity(null,UpdataTouchActivity.class,300);
        }
        if (view.getId() == R.id.shop_rz_layout) {
            SkipInfoEntity infoEntity=new SkipInfoEntity();
            infoEntity.index=1;
            viewDelegate.goToActivity(infoEntity,ApproveActivity.class,300);
        }
        if (view.getId()==R.id.shop_guide3_finish){
            SkipInfoEntity infoEntity=new SkipInfoEntity();
            infoEntity.index=4;
            viewDelegate.goToActivity(infoEntity, MainActivity.class,0);
            finish();
        }
        if (view.getId()==R.id.shop_guide3_back){
            viewDelegate.goToActivity(skipInfoEntity,ShopGuide1Activity.class,0);
            finish();
        }
    }

    private void init(String infoState,String contactState,String auditState){
        MLog.e("infoState="+infoState+";contactState="+contactState+";auditState="+auditState);
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
        if (auditState.equals("1")||auditState.equals("2")){//店铺认证
            rzTx.setTextColor(finishColor);
            rzTx.setText("已完善");
            rzImg.setImageResource(R.mipmap.img_shop_guide_check);
            rzLayout.setClickable(false);
        }else {
            rzTx.setTextColor(unFinishColor);
            rzTx.setText("去认证");
            rzImg.setImageResource(R.mipmap.img_shop_guide_right);
            rzLayout.setClickable(true);
        }
        if (infoState.equals("1")&&contactState.equals("1")&&auditState.equals("1")){
            viewDelegate.showSuccessHint("已完善信息", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    viewDelegate.goToActivity(skipInfoEntity,ShopGuide5Activity.class,0);
                    setResult(200);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&(resultCode==Constants.SHOP_GUIDE_INFO_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_CONTACT_SUCCESS
                ||resultCode==Constants.SHOP_GUIDE_RE_SUCCESS)){
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
            init(shopInfo.getIs_setbaseinfo(),shopInfo.getIs_setContactUser(),shopInfo.getAudit_state());
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
