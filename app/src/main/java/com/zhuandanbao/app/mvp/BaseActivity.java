package com.zhuandanbao.app.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.baidu.mobstat.StatService;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.mvp.presenter.ActivityPresenter;
import com.zhuandanbao.app.mvp.view.IDelegate;
import com.zhuandanbao.app.utils.SystemBar;


/**
 * Created by BFTECH on 2017/1/23.
 */

public abstract class BaseActivity<T extends IDelegate> extends ActivityPresenter<T> implements View.OnClickListener {

    protected Context context;
    protected Activity activity;
    protected int width;
    protected int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        activity = this;
        //设置状态颜色
        SystemBar.initSystemBar(activity, R.color.actionbar_bg);
        //写死竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myDevice();
        onActivityCreate(savedInstanceState);
    }

    /**
     * 获取设备宽、高
     */
    protected void myDevice() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        width = dm.widthPixels;
        height = dm.heightPixels;
    }

    protected abstract void onActivityCreate(Bundle savedInstanceState);


    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
