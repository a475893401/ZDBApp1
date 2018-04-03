package com.zhuandanbao.app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.WuliuD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.WuLiuEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.PopWuliu;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.ArrayList;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.area.SelectAreaWheelPopWOnClick;

/**
 * 添加或修改物流
 * Created by BFTECH on 2017/3/16.
 */

public class WuliuActivity extends BaseHttpActivity<WuliuD> {

    private Button button;
    private SkipInfoEntity skipInfoEntity;
    private TextView orderInfo;
    private TextView name;
    private EditText wuliuCode;
    private List<WuLiuEntity> wuLiuList = new ArrayList<>();
    private PopWuliu popWuliu;
    private ImageView scan;

    private String expressName = null;
    private String expressId = null;

    @Override
    protected Class<WuliuD> getDelegateClass() {
        return WuliuD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        button = viewDelegate.get(R.id.but_blue);
        orderInfo = viewDelegate.get(R.id.order_info);
        name = viewDelegate.get(R.id.wuliu_com);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            if (StrUtils.isNotNull(skipInfoEntity.mobile)) {
                viewDelegate.setTitle("修改物流");
                button.setText("确认修改");
                expressName = skipInfoEntity.mobile;
                expressId = skipInfoEntity.expressId;
            } else {
                viewDelegate.setTitle("发货");
                button.setText("确认发货");
            }
        }
        wuliuCode = viewDelegate.get(R.id.wuliu_code);
        scan = viewDelegate.get(R.id.img_scan);
        orderInfo.setText(skipInfoEntity.msg);
        wuliuCode.setText(skipInfoEntity.payCode);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(this);
        name.setOnClickListener(this);
        scan.setOnClickListener(this);
        postGetWuLiu();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.wuliu_com) {
            popWuliu.showPopw(view, expressName, new SelectAreaWheelPopWOnClick() {
                @Override
                public void sureOnClick(int provinceId, int cityId, int regionId, String provinceName, String cityName, String regionName) {
                    MLog.e("provinceId=" + provinceId + "provinceName=" + provinceName);
                    name.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流公司：", provinceName)));
                    expressName = provinceName;
                    expressId = provinceId + "";
                }

                @Override
                public void cancleOnClick() {

                }
            });
        }
        if (view.getId() == R.id.but_blue) {
            if (StrUtils.isNull(wuliuCode.getText().toString().trim())) {
                viewDelegate.showErrorHint("请输入运单号", 1, null);
                return;
            }
            if (skipInfoEntity.index == 1) {
                wuliu();
            } else {
                otherWuliu();
            }
        }
        if (view.getId() == R.id.img_scan) {
            impower();
        }
    }

    /**
     * 三方添加物流
     */
    private void otherWuliu() {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderSn", skipInfoEntity.pushId);
        params.put("expressId", expressId);
        params.put("expressSN", wuliuCode.getText().toString().trim());
        baseHttp("正在提交...", true, Constants.API_OTHER_ADD_WULIU, params);
    }

    /**
     * 接單物流
     */
    private void wuliu() {
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("orderId", skipInfoEntity.pushId);
        params.put("expressId", expressId);
        params.put("expressSN", wuliuCode.getText().toString().trim());
        baseHttp("正在提交...", true, Constants.API_ACTION_ORDERS_ADD_EXPRESS, params);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            wuLiuList.clear();
            wuLiuList.addAll(MJsonUtils.jsonToWuLiuEntity(baseEntity.data));
            if (null == popWuliu) {
                popWuliu = PopWuliu.getInstance(wuLiuList, context);
            }
            if (StrUtils.isNotNull(skipInfoEntity.mobile)) {
                name.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流公司：", skipInfoEntity.mobile)));
            } else {
                name.setText(Html.fromHtml(StrUtils.setHtml999(null, "物流公司：", wuLiuList.get(0).getName())));
                expressId = wuLiuList.get(0).getId();
            }
        }
        if (code == 2 || code == 3) {
            viewDelegate.showSuccessHint("已保存", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    setResult(6666);
                    finish();
                }
            });
        }
    }

    /**
     * 获取物流
     */
    private void postGetWuLiu() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_ACTION_ORDERS_EXPRESS_INFO, params);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String result = scanResult.getContents();
            if (StrUtils.isNotNull(result)) {
                wuliuCode.setText(result);
            }
        }
    }

    /**
     * 授权
     */
    private void impower() {
        if (Build.VERSION.SDK_INT >= 23) {
            // 手机信息功能
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //申请CAMERA权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 4);
            } else {
                //条形码扫描操作
                IntentIntegrator integrator = new IntentIntegrator(WuliuActivity.this);
                integrator.initiateScan();
            }
        } else {
            //条形码扫描操作
            IntentIntegrator integrator = new IntentIntegrator(WuliuActivity.this);
            integrator.initiateScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (4 == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权
                //条形码扫描操作
                IntentIntegrator integrator = new IntentIntegrator(WuliuActivity.this);
                integrator.initiateScan();
            } else {
                // 未授权
                viewDelegate.toast("你已禁止相机权限，请先手动打开");
            }
        }
    }
}
