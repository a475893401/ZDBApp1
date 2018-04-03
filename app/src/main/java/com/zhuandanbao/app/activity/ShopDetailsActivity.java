package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lzy.okgo.model.HttpParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.ShopDetailsD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopDetailsEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.fragment.ShopDetailsFragment;
import com.zhuandanbao.app.fragment.ShopDetailsTouchFragment;
import com.zhuandanbao.app.im.utils.ImLoginHelper;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.KProgressDismissClickLister;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.mvp.view.HttpCallback;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;
import com.zhuandanbao.app.utils.MyImage;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 店铺详情
 * Created by BFTECH on 2017/3/9.
 */

public class ShopDetailsActivity extends BaseListViewActivity<ShopDetailsD> implements ViewPager.OnPageChangeListener {

    private AdvancedPagerSlidingTabStrip tab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;
    private SkipInfoEntity skipInfoEntity;
    private String jsonData;
    private ShopDetailsEntity detailsEntity;
    private RadioButton shopBlack;
    private RadioButton workOrder;

    private String[] tabTitle = {"店铺基本信息", "店铺联系人"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;

    private static int VIEW_SIZE = 2;

    private boolean isBlackShop = false;

    private ShopDetailsFragment fragment1 = null;
    private ShopDetailsTouchFragment fragment2 = null;

    @Override
    protected Class<ShopDetailsD> getDelegateClass() {
        return ShopDetailsD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null != getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)) {
            skipInfoEntity = (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("店铺详情");
        viewDelegate.setOnClickListener(this,R.id.shop_detatil_xiadan,R.id.shop_detatil_online_msg,R.id.shop_detatil_complain);
        getShopInfo();
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            jsonData = baseEntity.data;
            tab = viewDelegate.get(R.id.shop_detail_tabs);
            myViewPager = viewDelegate.get(R.id.shop_detail_viewpager);
            topFragmentAdapter = new TopFragmentAdapter(getSupportFragmentManager());
            myViewPager.setAdapter(topFragmentAdapter);
            tab.setViewPager(myViewPager);
            tab.setOnPageChangeListener(this);
            myViewPager.setCurrentItem(VIEW_1);
            detailsEntity = MJsonUtils.jsonToShopDetailsEntity(baseEntity.data);
            initShopInfo();
        }
        if (code == 2) {
            viewDelegate.showSuccessHint("已将该店铺加入黑名单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    getShopInfo();
                }
            });
        }
        if (code==3){
            viewDelegate.showSuccessHint("已将该店铺移除黑名单", new KProgressDismissClickLister() {
                @Override
                public void onDismiss() {
                    getShopInfo();
                }
            });
        }
    }

    /**
     * 初始化店铺信息
     */
    private void initShopInfo() {
        ImageView shopLogo = viewDelegate.get(R.id.shop_detail_logo);
        TextView shopYiye = viewDelegate.get(R.id.shop_detail_yiye_status);
        shopBlack = viewDelegate.get(R.id.shop_detatil_blacklist);
        workOrder=viewDelegate.get(R.id.shop_detatil_complain);
        TextView shopName = viewDelegate.get(R.id.shop_detail_name);
        TextView jiedan = viewDelegate.get(R.id.shop_detail_jiedan_num);
        TextView fadan = viewDelegate.get(R.id.shop_detail_fadan_num);
        ImageLoader.getInstance().displayImage(detailsEntity.getShop_logo(), shopLogo, MyImage.deployMemory());
        if (detailsEntity.getYingye_state().equals("0")) {
            shopYiye.setText("正常营业");
        } else if (detailsEntity.getYingye_state().equals("1")) {
            shopYiye.setText("隐藏店铺");
        } else if (detailsEntity.getYingye_state().equals("3")) {
            shopYiye.setText("大量接单");
        } else if (detailsEntity.getYingye_state().equals("2")) {
            shopYiye.setText("订单饱和");
        }
        if ("1".equals(detailsEntity.getIs_black_shop())) {
            shopBlack.setText("移除黑名单");
            shopBlack.setChecked(true);
            isBlackShop = true;
        } else {
            shopBlack.setText("加入黑名单");
            shopBlack.setChecked(false);
            isBlackShop = false;
        }
        shopName.setText(detailsEntity.getShop_name());
        jiedan.setText(detailsEntity.getJiedan());
        fadan.setText(detailsEntity.getFadan());
        shopBlack.setOnClickListener(this);
        workOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.shop_detatil_blacklist) {
            if (isBlackShop) {//移除
                deteleBlack();
            } else {//加入
                View reason = LayoutInflater.from(activity).inflate(R.layout.reason_layout, null);
                final EditText input = (EditText) reason.findViewById(R.id.reason_input);
                input.setHint("加入黑名单后，则此店辅不能再抢您发布的抢单，请输入加入黑名单原因");
                input.setHintTextColor(Color.parseColor("#666666"));
                MUtils.showItemViewDialog(activity, reason, "加入黑名单", "返回", "确认", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MUtils.dialog.cancel();
                        if ("1".equals(detailsEntity.getIs_black_shop())) {
                            shopBlack.setText("移除黑名单");
                            shopBlack.setChecked(true);
                            isBlackShop = true;
                        } else {
                            shopBlack.setText("加入黑名单");
                            shopBlack.setChecked(false);
                            isBlackShop = false;
                        }
                    }
                }, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StrUtils.isNull(input.getText().toString())) {
                            Toast.makeText(activity, "请填写拒绝原因", Toast.LENGTH_LONG).show();
                            return;
                        }
                        MUtils.dialog.cancel();
                        addBlack(input.getText().toString());
                    }
                });
            }
        }
        if (view.getId()==R.id.shop_detatil_xiadan){
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.index=1;//指定下單
            skipInfoEntity.data=detailsEntity;
            viewDelegate.goToActivity(skipInfoEntity,BillNewOrderActivity.class,0);
            finish();
        }
        if (view.getId()==R.id.shop_detatil_complain){
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.index=4;
            skipInfoEntity.item=3;
            skipInfoEntity.msg=detailsEntity.getShop_name();
            viewDelegate.goToActivity(skipInfoEntity,AddWorkOrderActivity.class,0);
        }
        if(view.getId()==R.id.shop_detatil_online_msg){
            try {
                final String target = skipInfoEntity.pushId; //消息接收者ID
                final String appkey = ImLoginHelper.IM_APP_KEY; //消息接收者appKey
                Intent intent2 = ImLoginHelper.getInstance().getIMKit().getChattingActivityIntent(target, appkey);
                startActivity(intent2);
            } catch (Exception e) {
                e.printStackTrace();
                ImLoginHelper.getInstance().imLogin(activity, PerfHelper.getStringData(ImLoginHelper.IM_USER_ID_NAME) + ImLoginHelper.Im_PASSWORD, new HttpCallback() {
                    @Override
                    public void onSuccess(String data) {
                        final String target = skipInfoEntity.pushId; //消息接收者ID
                        final String appkey = ImLoginHelper.IM_APP_KEY; //消息接收者appKey
                        Intent intent2 = ImLoginHelper.getInstance().getIMKit().getChattingActivityIntent(target, appkey);
                        startActivity(intent2);
                    }
                    @Override
                    public void onError(int code, String message) {
                    }
                });
            }
        }
    }

    /**
     * 删除银行卡
     */
    private void deteleBlack(){
        requestId = 3;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("StoreId", detailsEntity.getSid());
        baseHttp("正在提交...",true,Constants.API_DELETE_BLACK_SHOP,params);
    }

    /**
     * 添加银行卡
     * @param reason
     */
    private void addBlack(String reason) {
        requestId = 2;
        loadingId = 1;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("reason", reason);
        params.put("StoreId", detailsEntity.getSid());
        baseHttp("正在提交...",true,Constants.API_PUSH_BLACK_SHOP,params);
    }

    /**
     * 獲取店鋪信息
     */
    private void getShopInfo() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("StoreId", skipInfoEntity.pushId);
        baseHttp(null, true, Constants.API_SHOP_DETATIL_INFO, params);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TopFragmentAdapter extends FragmentStatePagerAdapter {
        private LayoutInflater inflater;

        public TopFragmentAdapter(FragmentManager fm) {
            super(fm);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_1:
                        if (null == fragment1)
                            fragment1 = ShopDetailsFragment.getInstance(jsonData);
                        return fragment1;
                    case VIEW_2:
                        if (null == fragment2)
                            fragment2 = ShopDetailsTouchFragment.getInstance(jsonData);
                        return fragment2;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_1:
                        return tabTitle[VIEW_1];
                    case VIEW_2:
                        return tabTitle[VIEW_2];
                    default:
                        break;
                }
            }
            return super.getPageTitle(position);
        }
    }
}
