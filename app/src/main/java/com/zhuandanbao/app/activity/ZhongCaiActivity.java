package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ZhongCaiD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.RefundDataEntity;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 申请仲裁
 * Created by BFTECH on 2017/3/15.
 */

public class ZhongCaiActivity extends BaseHttpActivity<ZhongCaiD> {
    private Button button;
    private EditText mobile;
    private EditText inputContent;
    private SkipInfoEntity skipInfoEntity;
    private RefundDataEntity refundDataEntity;
    @Override
    protected Class<ZhongCaiD> getDelegateClass() {
        return ZhongCaiD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            refundDataEntity= (RefundDataEntity) skipInfoEntity.data;
        }
        viewDelegate.setTitle("申请仲裁");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        button=viewDelegate.get(R.id.but_blue);
        mobile=viewDelegate.get(R.id.mobile);
        inputContent=viewDelegate.get(R.id.input_content);
        button.setText("申请仲裁");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        getShopInfo();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.but_blue){
            if (!StrUtils.isMobileNum(mobile.getText().toString().trim())){
                viewDelegate.showErrorHint("请输入手机号",1,null);
                return;
            }
            if (StrUtils.isNull(inputContent.getText().toString().trim())){
                viewDelegate.showErrorHint("请输入仲裁原因",1,null);
                return;
            }
            requestId=2;
            loadingId=1;
            HttpParams params=new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            params.put("rid", refundDataEntity.getRid());
            params.put("apply_tel", mobile.getText().toString().trim());
            params.put("apply_reason", inputContent.getText().toString().trim());
            baseHttp("正在提交....",true,Constants.API_SALE_ARBITRATION,params);
        }
    }


    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            ShopInfoItemEntity shopInfo = MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo());
            mobile.setText(shopInfo.getMobile());
        }
        if (code==2){
            viewDelegate.showSuccessHint("提交成功，转单宝会在2-3个工作日内回复", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
                    finish();
                }
            });
        }
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        loadingId=2;
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }
}
