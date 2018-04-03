package com.zhuandanbao.app.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.activity.ApproveActivity;
import com.zhuandanbao.app.activity.BillNewOrderActivity;
import com.zhuandanbao.app.appdelegate.OrderTopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.ShopInfoEntity;
import com.zhuandanbao.app.entity.ShopInfoItemEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;
import com.zhuandanbao.app.utils.StrUtils;
import com.zhuandanbao.app.utils.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 发单
 * Created by BFTECH on 2017/3/1.
 */

public class BillTopFragment extends BaseListViewFragment<OrderTopD> implements ViewPager.OnPageChangeListener {
    private CustomPagerSlidingTabStrip topTab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;
    private TextView but;

    private String[] tabTitle = {"我的发单", "三方订单"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;

    private static int VIEW_SIZE = 2;
    private BillOrderFragment billOrderFragment = null;
    private OtherOrderFragment otherOrderFragment = null;

    private LinearLayout noOtherLayout;
    private TextView hint1;
    private TextView hint2;
    private int total = 0;

    public static BillTopFragment getInstance() {
        return new BillTopFragment();
    }

    @Override
    protected Class<OrderTopD> getDelegateClass() {
        return OrderTopD.class;
    }

    @Override
    public void initView() {
        super.initView();
        topTab = viewDelegate.get(R.id.order_top_tab);
        myViewPager = viewDelegate.get(R.id.order_viewpager);
        noOtherLayout = viewDelegate.get(R.id.no_other_shop_layout);
        hint1 = viewDelegate.get(R.id.no_other_shop_hint1);
        hint2 = viewDelegate.get(R.id.no_other_shop_hint2);
        but = viewDelegate.get(R.id.bill_but);
        but.setVisibility(View.VISIBLE);
        but.setOnClickListener(this);
        myViewPager.setNoScroll(true);
        topFragmentAdapter = new TopFragmentAdapter(getChildFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        topFragmentAdapter.notifyDataSetChanged();
        topTab.setViewPager(myViewPager);
        topTab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
        hint1.setText(Html.fromHtml(StrUtils.getBlueHtml("亲，您还未接入三方订单，暂无三方订单信息！您可以电脑登录转单宝官网在“ ", "账户-接入三方店铺", " ”进行接入。")));
        hint2.setText(Html.fromHtml(StrUtils.setHtmlRed("转单宝支持 ", "淘宝/天猫/京东/一号店/外卖平台（美团、饿了么、京东到家）/独立B2C网站（api接口开发中）", " 等鲜花、蛋糕类店辅接入，实时同步订单，并可以一键转单。")));
    }

    private void getOtherShop() {
        requestId = 1;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp(null, true, Constants.API_OTHER_SANFAN_LIST, params);
    }


    /**
     * 获取店铺信息
     */
    private void getShopInfo() {
        requestId = 2;
        loadingId = 0;
        HttpParams params = new HttpParams();
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        baseHttp("", true, Constants.API_GET_SHOP_INFO, params);
    }

    @Override
    protected void initD() {
        getOtherShop();
        isFristAddData = true;
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            JSONObject object = null;
            try {
                object = new JSONObject(baseEntity.data);
                total = object.optInt("total");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (code == 2) {
            ShopInfoEntity shopInfoEntity = MJsonUtils.josnToShopInfoEntity(baseEntity.data);
            MainApplication.cache.put(Constants.SHOP_INFO, shopInfoEntity.getShopInfo());
            showDialog(MJsonUtils.josnToShopInfoItemEntity(shopInfoEntity.getShopInfo()));
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.bill_but) {
//            getShopInfo();
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.index = 3;
            viewDelegate.goToActivity(skipInfoEntity, BillNewOrderActivity.class, 0);
        }
    }

    private void showDialog(ShopInfoItemEntity itemEntity) {
        if (null == itemEntity || itemEntity.getIs_audit().equals("1")) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.index = 3;
            viewDelegate.goToActivity(skipInfoEntity, BillNewOrderActivity.class, 0);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint, null);
            final Dialog dialog = new Dialog(context, R.style.fullDialog);
            dialog.getWindow().setContentView(view);
            dialog.show();
            TextView ok = (TextView) view.findViewById(R.id.ok);
            TextView cancel = (TextView) view.findViewById(R.id.cancel);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    startActivityForResult(new Intent(activity, ApproveActivity.class), 300);
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 1 && total == 0) {
            myViewPager.setVisibility(View.GONE);
            noOtherLayout.setVisibility(View.VISIBLE);
        } else {
            myViewPager.setVisibility(View.VISIBLE);
            noOtherLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class TopFragmentAdapter extends FragmentStatePagerAdapter implements CustomPagerSlidingTabStrip.CustomTabProvider {
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
                        if (null == billOrderFragment)
                            billOrderFragment = BillOrderFragment.getInstance();
                        return billOrderFragment;
                    case VIEW_2:
                        if (null == otherOrderFragment)
                            otherOrderFragment = OtherOrderFragment.getInstance();
                        return otherOrderFragment;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        @Override
        public View getSelectTabView(int position, View convertView) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_order_top_select, null);
            }
            TextView textView = ViewHolder.get(convertView, R.id.top_text1);
            textView.setText(tabTitle[position]);
            return convertView;
        }

        @Override
        public View getDisSelectTabView(int position, View convertView) {
            if (null == convertView) {
                convertView = inflater.inflate(R.layout.item_order_top_dis_select_w, null);
            }
            TextView textView = ViewHolder.get(convertView, R.id.top_text2);
            textView.setText(tabTitle[position]);
            return convertView;
        }
    }
}
