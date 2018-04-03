package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.AddHonestD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

/**
 * 加入诚信
 * Created by BFTECH on 2017/2/21.
 */

public class AddHonestActivity extends BaseHttpActivity<AddHonestD> {

    private ShopInfoItemEntity shopInfo;
    private TextView shopName;
    private TextView shopBluance;
    private EditText shopPassword;

    @Override
    protected Class<AddHonestD> getDelegateClass() {
        return AddHonestD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("加入诚信");
        shopName = viewDelegate.get(R.id.ah_shop_ed_name);
        shopBluance = viewDelegate.get(R.id.ah_shop_ed_bluance);
        shopPassword = viewDelegate.get(R.id.ah_honet_get_pay);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            ShopInfoEntity infoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            shopInfo = MJsonUtils.josnToShopInfoItemEntity(infoEntity.getShopInfo());
            showShopInfo();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("您已成功加入诚信保证", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    finish();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getShopInfo();
    }

    /**
     * 展示店铺信息
     */
    private void showShopInfo() {
        shopName.setText(shopInfo.getShop_name());
        shopBluance.setText("￥ "+shopInfo.getBalance());
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.hs_addhonesty_btn_ok) {//加入诚信保障
            addHonest();
        }
        if (view.getId()==R.id.ah_shop_ed_put_blance){//充值
            viewDelegate.goToActivity(null,RechargeActivity.class,0);
        }
        if (view.getId()==R.id.ah_honet_get_paypwd){//忘记支付密码
            viewDelegate.goToActivity(null,ForgetPasswordActivity.class,0);
        }
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this, R.id.ah_shop_ed_put_blance, R.id.ah_honet_get_paypwd, R.id.hs_addhonesty_btn_ok);
    }

    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("正在获取信息...", true, Constants.API_GET_SHOP_INFO, params);
    }

    /**
     * 加入誠信
     */
    private void addHonest() {
        String str = shopInfo.getBalance().trim().replaceAll(",", "");
        if (str.substring(0).equals("-") || Double.parseDouble(str) < 2000) {//余额
            viewDelegate.showErrorHint("店铺余额不足，请先充值", 1, null);
            return;
        }
        String pass = shopPassword.getText().toString().trim();
        if (StrUtils.isNull(pass)) {
            viewDelegate.showErrorHint("请输入支付密码", 1, null);
            return;
        }
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("paypass", pass);
        baseHttp("正在提交...", true, Constants.API_SHOP_ADD_HONESTY, params);
    }
}
