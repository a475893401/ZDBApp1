package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.SettingD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SoundEntity;
import com.zhuandanbao.app.entity.UpVersionDataEntity;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.AppUtils;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.UpApkUtils;

import java.util.ArrayList;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.SwitchView;

/**
 * 推送声音设置
 * Created by BFTECH on 2017/3/17.
 */

public class SettingActivity extends BaseListViewActivity<SettingD> {
    private List<SoundEntity> list = new ArrayList<>();

    private TextView verShow;

    private SwitchView switchView0;
    private SwitchView switchView1;
    private SwitchView switchView2;
    private SwitchView switchView3;
    private SwitchView switchView4;

    @Override
    protected Class<SettingD> getDelegateClass() {
        return SettingD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setTitle("设置");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setOnClickListener(this, R.id.updata_layout, R.id.but);
        verShow = viewDelegate.get(R.id.updata_show);
        switchView0 = viewDelegate.get(R.id.status_0);
        switchView1 = viewDelegate.get(R.id.status_1);
        switchView2 = viewDelegate.get(R.id.status_2);
        switchView3 = viewDelegate.get(R.id.status_3);
        switchView4 = viewDelegate.get(R.id.status_4);
        verShow.setText("v" + AppUtils.getVersionName());
        init();
    }

    private void init() {
        if (PerfHelper.getStringData(Constants.REMIND_VOICE).equals("1")) {
            switchView0.setState(false);
        } else {
            switchView0.setState(true);
        }
        if (PerfHelper.getStringData(Constants.SOUND_NEW).equals("1")) {
            switchView1.setState(false);
        } else {
            switchView1.setState(true);
        }
        if (PerfHelper.getStringData(Constants.SOUND_STATUS).equals("1")) {
            switchView2.setState(false);
        } else {
            switchView2.setState(true);
        }
        if (PerfHelper.getStringData(Constants.SOUND_SALES).equals("1")) {
            switchView3.setState(false);
        } else {
            switchView3.setState(true);
        }
        if (PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE).equals("1")) {
            switchView4.setState(false);
        } else {
            switchView4.setState(true);
        }
        switchView0.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {//关闭
                viewDelegate.toast("推送声音已关闭");
                switchView0.setState(true);
                if (PerfHelper.getStringData(Constants.SOUND_NEW).equals("1")) {
                    switchView1.setState(true);
                }
                if (PerfHelper.getStringData(Constants.SOUND_STATUS).equals("1")) {
                    switchView2.setState(true);
                }
                if (PerfHelper.getStringData(Constants.SOUND_SALES).equals("1")) {
                    switchView3.setState(true);
                }
                if (PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE).equals("1")) {
                    switchView4.setState(true);
                }
                PerfHelper.setInfo(Constants.REMIND_VOICE,"0");
                PerfHelper.setInfo(Constants.SOUND_NEW,"0");
                PerfHelper.setInfo(Constants.SOUND_STATUS,"0");
                PerfHelper.setInfo(Constants.SOUND_SALES,"0");
                PerfHelper.setInfo(Constants.SOUND_ONE_BY_ONE,"0");
            }

            @Override
            public void toggleToOff(View view) {//打开
                viewDelegate.toast("推送声音已打开");
                PerfHelper.setInfo(Constants.REMIND_VOICE,"1");
                switchView0.setState(false);
            }
        });
        switchView1.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                viewDelegate.toast("新抢单提醒声音已关闭");
                PerfHelper.setInfo(Constants.SOUND_NEW,"0");
                switchView1.setState(true);
            }

            @Override
            public void toggleToOff(View view) {
                viewDelegate.toast("新抢单提醒声音已打开");
                PerfHelper.setInfo(Constants.SOUND_NEW,"1");
                if (PerfHelper.getStringData(Constants.REMIND_VOICE).equals("0")){
                    PerfHelper.setInfo(Constants.REMIND_VOICE,"1");
                    switchView0.setState(false);
                }
                switchView1.setState(false);
            }
        });
        switchView2.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                viewDelegate.toast("订单状态改变提醒声音已关闭");
                PerfHelper.setInfo(Constants.SOUND_STATUS,"0");
                switchView2.setState(true);
            }

            @Override
            public void toggleToOff(View view) {
                viewDelegate.toast("订单状态改变提醒声音已打开");
                PerfHelper.setInfo(Constants.SOUND_STATUS,"1");
                if (PerfHelper.getStringData(Constants.REMIND_VOICE).equals("0")){
                    PerfHelper.setInfo(Constants.REMIND_VOICE,"1");
                    switchView0.setState(false);
                }
                switchView2.setState(false);
            }
        });
        switchView3.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                viewDelegate.toast("售后提醒声音已关闭");
                PerfHelper.setInfo(Constants.SOUND_SALES,"0");
                switchView3.setState(true);
            }

            @Override
            public void toggleToOff(View view) {
                viewDelegate.toast("售后提醒声音已打开");
                PerfHelper.setInfo(Constants.SOUND_SALES,"1");
                if (PerfHelper.getStringData(Constants.REMIND_VOICE).equals("0")){
                    PerfHelper.setInfo(Constants.REMIND_VOICE,"1");
                    switchView0.setState(false);
                }
                switchView3.setState(false);
            }
        });
        switchView4.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(View view) {
                viewDelegate.toast("接单推送声音已关闭");
                PerfHelper.setInfo(Constants.SOUND_ONE_BY_ONE,"0");
                switchView4.setState(true);
            }

            @Override
            public void toggleToOff(View view) {
                viewDelegate.toast("接单推送声音已打开");
                PerfHelper.setInfo(Constants.SOUND_ONE_BY_ONE,"1");
                if (PerfHelper.getStringData(Constants.REMIND_VOICE).equals("0")){
                    PerfHelper.setInfo(Constants.REMIND_VOICE,"1");
                    switchView0.setState(false);
                }
                switchView4.setState(false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.updata_layout) {
            getUpdataApp();
        }
        if (view.getId() == R.id.but) {
            PerfHelper.setInfo(Constants.APP_OPENID, null);
            MainApplication.cache.clear();
            setResult(5000);
            viewDelegate.goToActivity(null, LoginActivity.class, 0);
            finish();
        }
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            UpVersionDataEntity info = MJsonUtils.personUpDataEntity(baseEntity.data);
            if (AppUtils.getVersionCode() < info.version) {
                MainApplication.cache.clear();
                UpApkUtils.showUp(this, info, false);
            } else {
                viewDelegate.toast("当前是最新版本");
            }
        }
    }

    /**
     * 獲取app升級信息
     */
    private void getUpdataApp() {
        requestId = 1;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put("appType", "0");
        params.put("version", AppUtils.getVersionCode());
        baseHttp("正在获取...", true, Constants.API_APP_VERSION_UPDATA, params);
    }
}
