package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.alibaba.mobileim.IYWLoginService;
import com.alibaba.mobileim.channel.event.IWxCallback;
import com.alibaba.mobileim.utils.IYWCacheService;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.MViewPagerAdapter;
import com.zhuandanbao.app.appdelegate.LoginD;
import com.zhuandanbao.app.fragment.LoginAccountFragment;
import com.zhuandanbao.app.fragment.LoginMobileFragment;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 登录
 * Created by BFTECH on 2017/2/20.
 */

public class LoginActivity extends BaseHttpActivity<LoginD> {
    private MyViewPager viewPager;
    private RadioGroup group;
    private List<Fragment> fragments;

    @Override
    protected Class<LoginD> getDelegateClass() {
        return LoginD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        fragments=new ArrayList<>();
        viewPager = viewDelegate.get(R.id.login_viewpager);
        group = viewDelegate.get(R.id.login_btn);
        fragments.add(LoginAccountFragment.getInstance());
        fragments.add(LoginMobileFragment.getInstance());
        viewPager.setAdapter(new MViewPagerAdapter(getSupportFragmentManager(),fragments));
        viewPager.setNoScroll(true);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                RadioButton button = (RadioButton) group.getChildAt(position);
                button.setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.login_account:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.login_mobile:
                        viewPager.setCurrentItem(1);
                        break;
                }
            }
        });
        loginOut();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==200&&resultCode==200){
            activity.finish();
        }
    }

    /**
     * 登出Im
     */
    private void loginOut() {
        if (ImLoginHelper.getInstance().getIMKit() == null) {
            return;
        }
        // openIM SDK提供的登录服务
        IYWLoginService mLoginService = ImLoginHelper.getInstance().getIMKit().getLoginService();
        mLoginService.logout(new IWxCallback() {
            @Override
            public void onSuccess(Object... arg0) {
                MLog.e("退出成功");
                IYWCacheService mCacheService=ImLoginHelper.getInstance().getIMKit().getCacheService();
                mCacheService.clearCache(new IWxCallback() {
                    @Override
                    public void onSuccess(Object... objects) {
                        MLog.e("==clearCache===onSuccess=");
                    }

                    @Override
                    public void onError(int i, String s) {
                        MLog.e("==clearCache===onError="+s);
                    }

                    @Override
                    public void onProgress(int i) {

                    }
                });
            }
            @Override
            public void onProgress(int arg0) {
            }
            @Override
            public void onError(int arg0, String arg1) {
                MLog.e("arg0=" + arg0 + ";arg1=" + arg1);
            }
        });
    }
}
