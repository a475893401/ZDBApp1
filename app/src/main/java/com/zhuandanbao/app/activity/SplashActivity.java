package com.zhuandanbao.app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuandanbao.app.MainActivity;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.SplashD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.TokenEntity;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.AppUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.NetworkUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 启动也
 * Created by BFTECH on 2017/2/17.
 */

public class SplashActivity extends BaseHttpActivity<SplashD> {
    private ImageView imageView;

    @Override
    protected Class<SplashD> getDelegateClass() {
        return SplashD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        getResImg();
        ImageLoader.getInstance().clearMemoryCache();
        ImageLoader.getInstance().clearDiskCache();
        PerfHelper.setInfo(Constants.APP_VERSION, AppUtils.getVersionName());
        MainApplication.cache.remove(Constants.PAY_TIME);
        impower();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        init(baseEntity);
    }

    private void init(BaseEntity baseEntity) {
        final Class goclass;
        TokenEntity tokenEntity = JSON.parseObject(baseEntity.data, TokenEntity.class);
        PerfHelper.setInfo(Constants.ACCESS_TOKEN, tokenEntity.access_token);
        PerfHelper.setInfo(Constants.TOKEN_EXPORES, tokenEntity.expires);
        //初始化声音提醒 默认打开
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.REMIND_VOICE))) {
            PerfHelper.setInfo(Constants.REMIND_VOICE, "1");
        }
        //初始化振动提醒 默认打开
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.REMIND_LIBRATE))) {
            PerfHelper.setInfo(Constants.REMIND_LIBRATE, "1");
        }
        //新訂單
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.SOUND_NEW))) {
            PerfHelper.setInfo(Constants.SOUND_NEW, "1");
        }
        //改變
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.SOUND_STATUS))) {
            PerfHelper.setInfo(Constants.SOUND_STATUS, "1");
        }
        //售後
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.SOUND_SALES))) {
            PerfHelper.setInfo(Constants.SOUND_SALES, "1");
        }
        //接單
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.SOUND_ONE_BY_ONE))) {
            PerfHelper.setInfo(Constants.SOUND_ONE_BY_ONE, "1");
        }
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.REMIND_START_TIME))) {
            PerfHelper.setInfo(Constants.REMIND_START_TIME, "8");
        }
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.REMIND_STOP_TIME))) {
            PerfHelper.setInfo(Constants.REMIND_STOP_TIME, "22");
        }
        if (StrUtils.isNull(PerfHelper.getStringData(Constants.APP_NAME))) {
            //第一次安装
            PerfHelper.setInfo(Constants.APP_CODE, AppUtils.getVersionCode());
            PerfHelper.setInfo(Constants.APP_NAME, AppUtils.getVersionName());
            goclass = GuideActivity.class;
        } else {
            goclass = MainActivity.class;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewDelegate.goToActivity(null, goclass, 0);
                finish();
            }
        }, 2000);
    }

    private void getResImg() {
        imageView = viewDelegate.get(R.id.splash_img_hg);
        if (NetworkUtils.isNetworkAvailable(activity)) {
            MyImage.scaleImageRes(this, imageView, R.mipmap.img_splash);
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
     * 授权
     */
    private void impower() {
        if (Build.VERSION.SDK_INT >= 23) {
            // 手机信息功能
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                //申请CAMERA权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 4);
            } else {
                PerfHelper.setInfo(Constants.DEVICEID, AppUtils.getDeviceId(this));
                getToken();
            }
        } else {
            PerfHelper.setInfo(Constants.DEVICEID, AppUtils.getDeviceId(this));
            getToken();
        }
    }

    /**
     * 获取token
     */
    private void getToken() {
        requestId = 0;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put(Constants.APPID_NAME, Constants.APPID);
        params.put("secret", Constants.SECRET);
        params.put("grant_type", Constants.GRANT_TYPE);
        params.put("device_id", "865174028511639");
        baseHttp("请稍等...", true, Constants.API_GET_TOKEN, params);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (4 == requestCode) {
            if (grantResults.length>0&&grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 授权
                PerfHelper.setInfo(Constants.DEVICEID, AppUtils.getDeviceId(this));
            } else {
                // 未授权
                PerfHelper.setInfo(Constants.DEVICEID, "865174028511639");
            }
            getToken();
        }
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
            finish();
            System.exit(0);
        }
    }
}
