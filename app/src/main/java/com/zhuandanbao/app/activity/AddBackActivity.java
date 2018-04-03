package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.AddBackD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.BackListEntity;
import com.zhuandanbao.app.entity.BackNameEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import app.zhuandanbao.com.reslib.widget.area.PopArea;
import app.zhuandanbao.com.reslib.widget.area.SelectAreaWheelPopWOnClick;

/**
 * 添加/修改银行卡
 * Created by BFTECH on 2017/3/14.
 */

public class AddBackActivity extends BaseHttpActivity<AddBackD> {
    private Button button;//保存银行卡信息
    private SkipInfoEntity skipInfoEntity;
    private BackListEntity backListEntity;//银行卡列表实体类
    private TextView name;
    private TextView area;
    private EditText account;
    private EditText backNum;
    private EditText backHangName;
    private EditText pass;
    private ImageView backImg;
    private PopArea popArea;
    private String province = null;
    private String city = null;

    @Override
    protected Class<AddBackD> getDelegateClass() {
        return AddBackD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        button = viewDelegate.get(R.id.but_blue);
        name = viewDelegate.get(R.id.back_name);
        area = viewDelegate.get(R.id.back_area);
        account = viewDelegate.get(R.id.back_account);
        backNum = viewDelegate.get(R.id.back_num);
        backHangName = viewDelegate.get(R.id.back_hang_name);
        backImg = viewDelegate.get(R.id.back_img);
        pass = viewDelegate.get(R.id.back_pass);
        button.setText("保存");
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        viewDelegate.setOnClickListener(this, R.id.back_forget_pass, R.id.back_name, R.id.back_area);
        if (skipInfoEntity.index == 1) {
            viewDelegate.setRight("刪除", 0, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    delete();
                }
            });
            viewDelegate.setTitle("修改银行卡");
            backListEntity = (BackListEntity) skipInfoEntity.data;
            name.setText(backListEntity.getName());
            name.setTag(backListEntity.getCode());
            if (StrUtils.isNotNull(backListEntity.getLogo())) {
                backImg.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(backListEntity.getLogo(), backImg, MyImage.deployMemory());
            } else {
                backImg.setVisibility(View.GONE);
            }
            area.setText(backListEntity.getProvince_name() + "|" + backListEntity.getCity_name());
            province = backListEntity.getProvince_name();
            city = backListEntity.getCity_name();
            area.setTag("1");
            account.setText(backListEntity.getAccount());
            backNum.setText(backListEntity.getBank_card_num());
            backHangName.setText(backListEntity.getBank_full_name());
        } else {
            viewDelegate.setTitle("添加银行卡");
        }
        ShopInfoItemEntity shopInfo = MJsonUtils.josnToShopInfoItemEntity(MainApplication.cache.getAsString(Constants.SHOP_INFO));
        viewDelegate.showShopinfoDialog(shopInfo,"请先完善店铺基本信息");
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.but_blue) {//保存
            save();
        }
        if (view.getId() == R.id.back_forget_pass) {//忘记密码
            viewDelegate.goToActivity(null, ForgetPasswordActivity.class, 0);
        }
        if (view.getId() == R.id.back_area) {//获取地址
            getAllArea();
        }
        if (view.getId() == R.id.back_name) {
            SkipInfoEntity infoEntity = new SkipInfoEntity();
            if (skipInfoEntity.index == 1) {
                infoEntity.pushId = backListEntity.getCode();
            } else {
                infoEntity.pushId = "0";
            }
            viewDelegate.goToActivity(infoEntity, BackNameListActivity.class, 300);
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            showAddress(baseEntity.data);
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("已保存", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
                    finish();
                }
            });
        }
        if (code == 3) {
            viewDelegate.showSuccessHint("已删除", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
                    finish();
                }
            });
        }
    }

    private void delete() {
        MUtils.showDialog(context, "转单宝提示", "再想想", "删除", "你即将删除此张银行卡信息？", null, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MUtils.dialog.cancel();
                requestId = 3;
                loadingId = 1;
                HttpParams params = new HttpParams();
                params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
                params.put("bcid", backListEntity.getBcid());
                baseHttp("正在删除...", true, Constants.API_FINANCE_DETELE_BANK_CARD, params);
            }
        });
    }

    private void save() {
        if (StrUtils.isNull(name.getText().toString().trim())) {
            viewDelegate.showErrorHint("请选择开户银行", 1, null);
            return;
        }
        if (StrUtils.isNull(area.getText().toString().trim())) {
            viewDelegate.showErrorHint("请选择开户地区", 1, null);
            return;
        }
        if (StrUtils.isNull(account.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入店主姓名", 1, null);
            return;
        }
        if (StrUtils.isNull(backNum.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入银行卡号", 1, null);
            return;
        }
        if (StrUtils.isNull(backHangName.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入开户行名", 1, null);
            return;
        }
        if (StrUtils.isNull(pass.getText().toString().trim())) {
            viewDelegate.showErrorHint("请输入支付密码", 1, null);
            return;
        }
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("province", province);
        params.put("city", city);
        params.put("account", account.getText().toString().trim());
        params.put("bank_card_num", backNum.getText().toString().trim());
        params.put("bank_full_name", backHangName.getText().toString());
        params.put("paypass", pass.getText().toString().trim());
        if (skipInfoEntity.index == 1) {//编辑
            params.put("bcid", backListEntity.getBcid());
            params.put("bank", backListEntity.getCode());
        } else {//添加
            params.put("bank", name.getTag().toString());
        }
        baseHttp("正在保存...", true, Constants.API_FINANCE_ADD_OR_EDIT_BANK_CARD, params);
    }

    /**
     * 獲取地區
     */
    private void getAllArea() {
        if (StrUtils.isNotNull(MainApplication.cache.getAsString(Constants.ADDRESS_JSON_DATA))) {
            showAddress(MainApplication.cache.getAsString(Constants.ADDRESS_JSON_DATA));
        } else {
            requestId = 1;
            loadingId = 1;
            HttpParams params = new HttpParams();
            params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
            baseHttp("正在获取地区...", true, Constants.API_GET_AREA_ALL, params);
        }
    }

    /**
     * show地址
     */
    private void showAddress(String josnStr) {
        if (null == popArea) {
            popArea = PopArea.getInstance(josnStr, this);
        }
        String[] spAddressArray = null;
        if (StrUtils.isNotNull(area.getText().toString().trim())) {
            spAddressArray = area.getText().toString().trim().split("\\|");
        }
        popArea.showPopw(area, true, spAddressArray, new SelectAreaWheelPopWOnClick() {
            @Override
            public void sureOnClick(int provinceId, int cityId, int regionId, String provinceName, String cityName, String regionName) {
                area.setText(deteleChar(provinceName) + "|" + deteleChar(cityName));
                province = deteleChar(provinceName);
                city = deteleChar(cityName);
            }

            @Override
            public void cancleOnClick() {

            }
        });
    }

    private String deteleChar(String string) {
        try {
            int length = string.length();
            String str = String.valueOf(string.charAt(length - 1));
            if (str.equals("市") || str.equals("省")) {
                string = string.substring(0, string.length() - 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 300 && resultCode == 6666) {
            if (null != data && null != data.getSerializableExtra(Constants.SKIP_INFO_ID)) {
                BackNameEntity info = (BackNameEntity) data.getSerializableExtra(Constants.SKIP_INFO_ID);
                name.setText(info.getName());
                name.setTag(info.getCode());
                backImg.setVisibility(View.VISIBLE);
                ImageLoader.getInstance().displayImage(info.getLogo(), backImg, MyImage.deployMemoryNoLoading());
            }
        }
    }
}
