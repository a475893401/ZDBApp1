package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.TradeListTopD;
import com.zhuandanbao.app.fragment.RechargeLogFragment;
import com.zhuandanbao.app.fragment.TradeLogFragment;
import com.zhuandanbao.app.fragment.WithdrawLogFragment;
import com.zhuandanbao.app.mvp.BaseListViewActivity;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 交易日志
 * Created by BFTECH on 2017/3/14.
 */

public class TradeLogActivity extends BaseListViewActivity<TradeListTopD> implements ViewPager.OnPageChangeListener{
    private AdvancedPagerSlidingTabStrip tab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;
    private TextView hint;

    private String[] tabTitle = {"交易明细", "充值记录", "提现记录"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;
    private static final int VIEW_3 = 2;

    private static int VIEW_SIZE = 3;
    private TradeLogFragment fragment1 = null;
    private RechargeLogFragment fragment2 = null;
    private WithdrawLogFragment fragment3 = null;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回",R.mipmap.img_back_icon,null);
        viewDelegate.setTitle("交易明细");
        tab = viewDelegate.get(R.id.trade_list_top_tab);
        myViewPager = viewDelegate.get(R.id.trade_top_viewpager);
        hint=viewDelegate.get(R.id.trade_top_hint);
        topFragmentAdapter =new  TopFragmentAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        tab.setViewPager(myViewPager);
        tab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
    }

    @Override
    protected Class<TradeListTopD> getDelegateClass() {
        return TradeListTopD.class;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position){
            case 0:
                viewDelegate.setTitle("交易明细");
                hint.setText("近3个月订单交易明细");
                break;
            case 1:
                viewDelegate.setTitle("充值记录");
                hint.setText("近3个月充值记录");
                break;
            case 2:
                viewDelegate.setTitle("提现记录");
                hint.setText("近3个月提现记录");
                break;
        }
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
                            fragment1 = TradeLogFragment.getInstance();
                        return fragment1;
                    case VIEW_2:
                        if (null == fragment2)
                            fragment2 = RechargeLogFragment.getInstance();
                        return fragment2;
                    case VIEW_3:
                        if (null == fragment3)
                            fragment3 = WithdrawLogFragment.getInstance();
                        return fragment3;
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
                    case VIEW_3:
                        return tabTitle[VIEW_3];
                }
            }
            return super.getPageTitle(position);
        }
    }
}
