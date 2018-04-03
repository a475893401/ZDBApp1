package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ShopGuide5D;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.VipLoginInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;

/**
 * 接单方完成的信息
 * Created by BFTECH on 2017/4/5.
 */

public class ShopGuide5Activity extends BaseHttpActivity<ShopGuide5D> {

    private SkipInfoEntity skipInfoEntity;
    private VipLoginInfoEntity vipInfo;
    @Override
    protected Class<ShopGuide5D> getDelegateClass() {
        return ShopGuide5D.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            vipInfo= (VipLoginInfoEntity) skipInfoEntity.data;
        }
        viewDelegate.setOnClickListener(this, R.id.shop_guide5_layout,R.id.shop_guide5_next);
        LinearLayout passLayout=viewDelegate.get(R.id.shop_guide5_layout);
        TextView hintTx=viewDelegate.get(R.id.shop_guide5_hint_tx);
        ImageView hintImg=viewDelegate.get(R.id.shop_guide5_hint_img);
        int finishColor= Color.parseColor("#f29000");
        int unFinishColor=Color.parseColor("#5e9eed");
        if (vipInfo.getIs_set_paypass().equals("1")){
            hintTx.setTextColor(finishColor);
            hintTx.setText("已完善");
            hintImg.setImageResource(R.mipmap.img_shop_guide_check);
            passLayout.setClickable(false);
        }else {
            hintTx.setTextColor(unFinishColor);
            hintTx.setText("去设置");
            hintImg.setImageResource(R.mipmap.img_shop_guide_right);
            passLayout.setClickable(true);
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.shop_guide5_layout){
            viewDelegate.goToActivity(null,PayPasswordActivity.class,300);
        }
        if (view.getId()==R.id.shop_guide5_next){
            SkipInfoEntity infoEntity=new SkipInfoEntity();
            infoEntity.index=0;
            viewDelegate.goToActivity(infoEntity, MainActivity.class,0);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==300&&resultCode== Constants.SHOP_GUIDE_PASS_SUCCESS){
            SkipInfoEntity infoEntity=new SkipInfoEntity();
            infoEntity.index=0;
            viewDelegate.goToActivity(infoEntity, MainActivity.class,0);
            finish();
        }
    }
}
