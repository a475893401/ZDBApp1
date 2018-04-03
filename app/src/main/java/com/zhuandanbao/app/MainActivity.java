package com.zhuandanbao.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.mobileim.conversation.IYWConversationService;
import com.alibaba.mobileim.conversation.IYWConversationUnreadChangeListener;
import com.baidu.mapapi.SDKInitializer;
import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.activity.SettingActivity;
import com.zhuandanbao.app.appdelegate.MainD;
import com.zhuandanbao.app.callbackinterface.ActivityToFragment;
import com.zhuandanbao.app.callbackinterface.CallbackEntity;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.SystemSettingEntity;
import com.zhuandanbao.app.entity.UpVersionDataEntity;
import com.zhuandanbao.app.fragment.BillTopFragment;
import com.zhuandanbao.app.fragment.MainFinanceFragment;
import com.zhuandanbao.app.fragment.MainShopFragment;
import com.zhuandanbao.app.fragment.SaleTopFragment;
import com.zhuandanbao.app.fragment.TakingTopFragment;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.view.HttpCallback;
import com.zhuandanbao.app.utils.AppUtils;
import com.zhuandanbao.app.utils.GDMapDis;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.UpApkUtils;

import java.util.Timer;
import java.util.TimerTask;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

public class MainActivity extends BaseHttpActivity<MainD> implements ViewPager.OnPageChangeListener {
    private ActivityToFragment buttonClickedListener;
    private MyViewPager viewPager;
    private CustomPagerSlidingTabStrip tab;
    private String[] tabTitle = {"接单", "发单", "售后", "财务", "店铺"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;
    private static final int VIEW_3 = 2;
    private static final int VIEW_4 = 3;
    private static final int VIEW_5 = 4;

    private static int VIEW_SIZE = 5;
    private TakingTopFragment takingTopFragment = null;
    private BillTopFragment billTopFragment = null;
    private SaleTopFragment saleTopFragment = null;
    private MainFinanceFragment mainFinanceFragment = null;
    private MainShopFragment shopFragment = null;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IYWConversationService mConversationService;
    private IYWConversationUnreadChangeListener mConversationUnreadChangeListener;

    private TextView num;
    private int msgNum = 0;


    /**
     * @param buttonClickedListener 写一个对外公开的方法
     */
    public void OnCliked(ActivityToFragment buttonClickedListener) {
        this.buttonClickedListener = buttonClickedListener;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (null != intent.getSerializableExtra(Constants.SKIP_INFO_ID)) {
            SkipInfoEntity info = (SkipInfoEntity) intent.getSerializableExtra(Constants.SKIP_INFO_ID);
            if (null != viewPager) {
                viewPager.setCurrentItem(info.index);
            }
        }
    }

    @Override
    protected Class<MainD> getDelegateClass() {
        return MainD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//软键盘覆盖
        SDKInitializer.initialize(getApplicationContext());
        super.onActivityCreate(savedInstanceState);
        viewPager = viewDelegate.get(R.id.main_viewpager);
        tab = viewDelegate.get(R.id.main_tab);
        viewPager.setNoScroll(true);
        MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tab.setViewPager(viewPager);
        tab.setOnPageChangeListener(this);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            SkipInfoEntity infoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
            viewPager.setCurrentItem(infoEntity.index);
        }else {
            viewPager.setCurrentItem(VIEW_1);
        }
        getUpdataApp();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            SystemSettingEntity info = MJsonUtils.jsonToSystemSettingEntity(baseEntity.data);
            MainApplication.cache.put(Constants.SYSTEM_SETTING, info);
        }
        if (code == 2) {
            UpVersionDataEntity info = MJsonUtils.personUpDataEntity(baseEntity.data);
            if (1!=AppUtils.getVersionCode()&&AppUtils.getVersionCode() < info.version) {
                MainApplication.cache.clear();
                UpApkUtils.showUp(this, info, false);
            } else {
                getSetting();
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == VIEW_5) {
            viewDelegate.setTitle("店铺");
            viewDelegate.setRight("设置", R.mipmap.img_setting, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewDelegate.goToActivity(null, SettingActivity.class, 6666);
                }
            });
        } else if (position == VIEW_4) {
            viewDelegate.setTitle("财务");
            viewDelegate.hintRight();
        } else {
            viewDelegate.getToolbar().setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MainFragmentAdapter extends FragmentStatePagerAdapter implements CustomPagerSlidingTabStrip.CustomTabProvider {

        LayoutInflater mInflater;

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
            mInflater = LayoutInflater.from(MainActivity.this);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_1:
                        if (null == takingTopFragment)
                            takingTopFragment = TakingTopFragment.getInstance();
                        return takingTopFragment;
                    case VIEW_2:
                        if (null == billTopFragment)
                            billTopFragment = BillTopFragment.getInstance();
                        return billTopFragment;
                    case VIEW_3:
                        if (null == saleTopFragment)
                            saleTopFragment = SaleTopFragment.getInstance();
                        return saleTopFragment;
                    case VIEW_4:
                        if (null == mainFinanceFragment)
                            mainFinanceFragment = MainFinanceFragment.getInstance();
                        return mainFinanceFragment;
                    case VIEW_5:
                        if (null == shopFragment)
                            shopFragment = MainShopFragment.getInstance();
                        return shopFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        /**
         * 选中
         *
         * @param position
         * @param convertView
         * @return
         */
        @Override
        public View getSelectTabView(int position, View convertView) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_select_tab_view, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.select_radio_text);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.select_radio_img);
            num = (TextView) convertView.findViewById(R.id.msg_num);
            switch (position) {
                case VIEW_1:
                    textView.setText(tabTitle[VIEW_1]);
                    imageView.setImageResource(R.mipmap.img_nvs_receiving);
                    break;
                case VIEW_2:
                    textView.setText(tabTitle[VIEW_2]);
                    imageView.setImageResource(R.mipmap.img_nvs_bill);
                    break;
                case VIEW_3:
                    textView.setText(tabTitle[VIEW_3]);
                    imageView.setImageResource(R.mipmap.img_nvs_sale);
                    break;
                case VIEW_4:
                    textView.setText(tabTitle[VIEW_4]);
                    imageView.setImageResource(R.mipmap.img_nvs_finance);
                    break;
                case VIEW_5:
                    textView.setText(tabTitle[VIEW_5]);
                    imageView.setImageResource(R.mipmap.img_nvs_shop);
                    setNum();
                    break;
            }
            return convertView;
        }

        /**
         * 未选中
         *
         * @param position
         * @param convertView
         * @return
         */
        @Override
        public View getDisSelectTabView(int position, View convertView) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_dis_select_tab_view, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.unselect_radio_text);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.unselect_radio_img);
            num = (TextView) convertView.findViewById(R.id.msg_num);
            switch (position) {
                case VIEW_1:
                    textView.setText(tabTitle[VIEW_1]);
                    imageView.setImageResource(R.mipmap.img_nvs_receiving_no);
                    break;
                case VIEW_2:
                    textView.setText(tabTitle[VIEW_2]);
                    imageView.setImageResource(R.mipmap.img_nvs_bill_no);
                    break;
                case VIEW_3:
                    textView.setText(tabTitle[VIEW_3]);
                    imageView.setImageResource(R.mipmap.img_nvs_sale_no);
                    break;
                case VIEW_4:
                    textView.setText(tabTitle[VIEW_4]);
                    imageView.setImageResource(R.mipmap.img_nvs_finance_no);
                    break;
                case VIEW_5:
                    textView.setText(tabTitle[VIEW_5]);
                    imageView.setImageResource(R.mipmap.img_nvs_shop_no);
                    setNum();
                    break;
            }
            return convertView;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loginIm(PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME));
        if (StrUtils.isNotNull(MainApplication.cache.getAsString(Constants.SHOP_INFO))) {
            ShopInfoItemEntity itemEntity = null;
            try {
                itemEntity = MJsonUtils.josnToShopInfoItemEntity(MainApplication.cache.getAsString(Constants.SHOP_INFO));
                MainApplication.cache.put(Constants.SHOP_SID, itemEntity.getSid());
                if (null != itemEntity) {
                    String[] add = GDMapDis.getDetitlsAddressInfo(itemEntity.getAreainfo(), itemEntity.getAddress());
                    if (null != add) {
                        GDMapDis.getShopLatInfo(this, add[1], add[0]);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 獲取app升級信息
     */
    private void getUpdataApp() {
        requestId = 2;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put("appType", "0");
        params.put("version", AppUtils.getVersionCode());
        baseHttp(null, true, Constants.API_APP_VERSION_UPDATA, params);
    }

    private void getSetting() {
        requestId = 1;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_SYSTEMS_SETTING, params);
    }

    private void loginIm(String sid) {
        MLog.e("sid==" + sid);
        PerfHelper.setInfo(ImLoginHelper.IM_USER_ID_NAME, sid);
        ImLoginHelper.getInstance().imLogin(this, sid + ImLoginHelper.Im_PASSWORD, new HttpCallback() {
            @Override
            public void onSuccess(String data) {
                MLog.e("" + data);
                if (null != ImLoginHelper.getInstance().getIMKit()) {
                    mConversationService = ImLoginHelper.getInstance().getIMKit().getConversationService();
                    initConversationServiceAndListener();
                    int unReadCount = mConversationService.getAllUnreadCount();
                    CallbackEntity info = new CallbackEntity();
                    info.msg = unReadCount + "";
                    if (null != buttonClickedListener) {
                        buttonClickedListener.sendMsgFragment(info);
                    }
                    MLog.e("=======unReadCount=====" + unReadCount);
                    msgNum = unReadCount;
                    setNum();
                }
            }

            @Override
            public void onError(int code, String message) {
                MLog.e("code=" + code + ";message=" + message);
            }
        });
    }

    private void setNum() {
        if (msgNum > 0) {
            num.setVisibility(View.VISIBLE);
            num.setText(msgNum + "");
        } else if (msgNum > 99) {
            num.setVisibility(View.VISIBLE);
            num.setText("99+");
        } else {
            num.setVisibility(View.GONE);
        }
    }

    /**
     * 监听阿里百川的信息变更
     */
    private void initConversationServiceAndListener() {
        mConversationUnreadChangeListener = new IYWConversationUnreadChangeListener() {
            //当未读数发生变化时会回调该方法，开发者可以在该方法中更新未读数
            @Override
            public void onUnreadChange() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mConversationService = ImLoginHelper.getInstance().getIMKit().getConversationService();
                        //获取当前登录用户的所有未读数
                        int unReadCount = mConversationService.getAllUnreadCount();
                        //设置桌面角标的未读数
                        CallbackEntity info = new CallbackEntity();
                        info.msg = unReadCount + "";
                        if (null != buttonClickedListener) {
                            buttonClickedListener.sendMsgFragment(info);
                        }
                        MLog.e("unReadCount===" + unReadCount);
                        msgNum = unReadCount;
                        setNum();
                    }
                });
            }
        };
        mConversationService.addTotalUnreadChangeListener(mConversationUnreadChangeListener);
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
        if (requestCode == 6666 && resultCode == 5000) {
            finish();
        }
    }
}
