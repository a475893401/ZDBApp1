package com.zhuandanbao.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.OrderListTopD;
import com.zhuandanbao.app.mvp.BaseListViewFragment;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 抢单区域
 * Created by BFTECH on 2017/3/1.
 */

public class GradOrderFragment extends BaseListViewFragment<OrderListTopD> implements ViewPager.OnPageChangeListener {

    private AdvancedPagerSlidingTabStrip tab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;

    private String[] tabTitle = {"同区域抢单", "全部抢单"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;

    private static int VIEW_SIZE = 2;

    private GradOrderListFragment fragment1 = null, fragment2 = null;

    public static GradOrderFragment getInstance() {
        return new GradOrderFragment();
    }

    @Override
    protected void initD() {

    }

    @Override
    public void initView() {
        super.initView();
        LinearLayout layout=viewDelegate.get(R.id.layout_search);
        layout.setVisibility(View.GONE);
        tab = viewDelegate.get(R.id.order_list_top_tab);
        myViewPager = viewDelegate.get(R.id.order_top_viewpager);
        topFragmentAdapter = new TopFragmentAdapter(getChildFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        tab.setViewPager(myViewPager);
        tab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
    }

    @Override
    protected Class<OrderListTopD> getDelegateClass() {
        return OrderListTopD.class;
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
                            fragment1 = GradOrderListFragment.getInstance(VIEW_1);
                        return fragment1;
                    case VIEW_2:
                        if (null == fragment2)
                            fragment2 = GradOrderListFragment.getInstance(VIEW_2);
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
