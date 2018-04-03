package com.zhuandanbao.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.OrderTopD;
import com.zhuandanbao.app.mvp.BaseHttpFragment;
import com.zhuandanbao.app.utils.ViewHolder;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 接单/抢单
 * Created by BFTECH on 2017/3/1.
 */

public class TakingTopFragment extends BaseHttpFragment<OrderTopD> implements ViewPager.OnPageChangeListener {

    private CustomPagerSlidingTabStrip topTab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;

    private String[] tabTitle = {"抢单", "接单"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;

    private static int VIEW_SIZE = 2;

    private GradOrderFragment gradOrderFragment=null;
    private TakingOrderFragment takingOrderFragment=null;

    public static TakingTopFragment getInstance() {
        return new TakingTopFragment();
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
        myViewPager.setNoScroll(true);
        topFragmentAdapter = new TopFragmentAdapter(getChildFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        topFragmentAdapter.notifyDataSetChanged();
        topTab.setViewPager(myViewPager);
        topTab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
    }

    @Override
    protected void initD() {

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
                        if (null == gradOrderFragment)
                            gradOrderFragment = GradOrderFragment.getInstance();
                        return gradOrderFragment;
                    case VIEW_2:
                        if (null == takingOrderFragment)
                            takingOrderFragment = TakingOrderFragment.getInstance();
                        return takingOrderFragment;
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
