package com.zhuandanbao.app.activity;

import android.os.Bundle;
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
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.fragment.OtherInfoFragment;
import com.zhuandanbao.app.fragment.OtherInfoLogFragment;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.utils.ViewHolder;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 三方订单详情/订单跟踪
 * Created by BFTECH on 2017/3/9.
 */

public class OtherOrderInfoActivity  extends BaseListViewActivity<OrderTopD> implements ViewPager.OnPageChangeListener {
    private CustomPagerSlidingTabStrip topTab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;
    private TextView back;

    private String[] tabTitle = {"三方详情", "订单跟踪"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;

    private static int VIEW_SIZE = 2;
    private OtherInfoFragment billOrderFragment = null;
    private OtherInfoLogFragment otherOrderFragment=null;

    private SkipInfoEntity skipInfoEntity;

    @Override
    protected Class<OrderTopD> getDelegateClass() {
        return OrderTopD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!=getIntent().getSerializableExtra(Constants.SKIP_INFO_ID)){
            skipInfoEntity= (SkipInfoEntity) getIntent().getSerializableExtra(Constants.SKIP_INFO_ID);
        }
        viewDelegate.setOnClickListener(this, R.id.order_info_back);
        topTab = viewDelegate.get(R.id.order_top_tab);
        myViewPager = viewDelegate.get(R.id.order_viewpager);
        back=viewDelegate.get(R.id.order_info_back);
        back.setVisibility(View.VISIBLE);
        back.setText("返回");
        myViewPager.setNoScroll(true);
        topFragmentAdapter = new TopFragmentAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        topFragmentAdapter.notifyDataSetChanged();
        topTab.setViewPager(myViewPager);
        topTab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.order_info_back){
            finish();
        }
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
                        if (null == billOrderFragment)
                            billOrderFragment = OtherInfoFragment.getInstance(skipInfoEntity.pushId);
                        return billOrderFragment;
                    case VIEW_2:
                        if (null == otherOrderFragment)
                            otherOrderFragment = OtherInfoLogFragment.getInstance(skipInfoEntity.pushId);
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