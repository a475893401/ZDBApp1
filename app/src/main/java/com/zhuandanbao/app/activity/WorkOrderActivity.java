package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.WorkOrderD;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.fragment.WorkOrderListFragment;
import com.zhuandanbao.app.mvp.BaseHttpActivity;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 工单
 * Created by BFTECH on 2017/3/15.
 */

public class WorkOrderActivity extends BaseHttpActivity<WorkOrderD> implements ViewPager.OnPageChangeListener{

    private AdvancedPagerSlidingTabStrip tab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;

    private String[] tabTitle = {"进行中","已完成"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;

    private static int VIEW_SIZE = 2;
    private WorkOrderListFragment fragment1 = null, fragment2 = null;

    @Override
    protected Class<WorkOrderD> getDelegateClass() {
        return WorkOrderD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setTitle("我的工单");
        viewDelegate.setBack("返回", R.mipmap.img_back_icon,null);
        viewDelegate.setRight("+ 创建工单",0,this);
        tab = viewDelegate.get(R.id.work_order_tab);
        myViewPager = viewDelegate.get(R.id.work_order_viewpager);
        topFragmentAdapter = new TopFragmentAdapter(getSupportFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        tab.setViewPager(myViewPager);
        tab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId()==R.id.nvs_right){
            SkipInfoEntity skipInfoEntity=new SkipInfoEntity();
            skipInfoEntity.index=0;
            skipInfoEntity.item=0;
            viewDelegate.goToActivity(skipInfoEntity,AddWorkOrderActivity.class,0);
        }
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
                            fragment1 = WorkOrderListFragment.getInstance(VIEW_1);
                        return fragment1;
                    case VIEW_2:
                        if (null == fragment2)
                            fragment2 = WorkOrderListFragment.getInstance(VIEW_2);
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
