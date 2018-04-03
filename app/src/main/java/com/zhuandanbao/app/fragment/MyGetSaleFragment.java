package com.zhuandanbao.app.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.OrderListTopD;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.utils.StrUtils;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 *我收到的售后状态
 * Created by BFTECH on 2017/3/2.
 */

public class MyGetSaleFragment extends BaseListViewFragment<OrderListTopD> implements ViewPager.OnPageChangeListener {

    private AdvancedPagerSlidingTabStrip tab;
    private MyViewPager myViewPager;
    private TopFragmentAdapter topFragmentAdapter;
    private EditText content;//搜索内容

    private String[] tabTitle = {"处理中", "已驳回", "仲裁中", "已完成"};
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;
    private static final int VIEW_3 = 2;
    private static final int VIEW_4 = 3;
    private static int VIEW_SIZE = 4;
    private int showIndex = 0;//當前顯示fragment的位置
    private MyGetSaleListFragment fragment1 = null, fragment2 = null, fragment3 = null, fragment4 = null;

    public static MyGetSaleFragment getInstance() {
        return new MyGetSaleFragment();
    }

    @Override
    protected void initD() {

    }

    @Override
    public void initView() {
        super.initView();
        tab = viewDelegate.get(R.id.order_list_top_tab);
        myViewPager = viewDelegate.get(R.id.order_top_viewpager);
        content = viewDelegate.get(R.id.search_content);
        topFragmentAdapter = new TopFragmentAdapter(getChildFragmentManager());
        myViewPager.setAdapter(topFragmentAdapter);
        tab.setViewPager(myViewPager);
        tab.setOnPageChangeListener(this);
        myViewPager.setCurrentItem(VIEW_1);
        viewDelegate.setOnClickListener(this, R.id.search_but);
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
        showIndex = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.search_but) {
            if (StrUtils.isNull(content.getText().toString())) {
                viewDelegate.showErrorHint("请输入搜索内容", 1, null);
                return;
            }
            switch (showIndex) {
                case 0:
                    if (null != fragment1) {
                        fragment1.search(content.getText().toString().trim());
                    }
                    break;
                case 1:
                    if (null != fragment2) {
                        fragment2.search(content.getText().toString().trim());
                    }
                    break;
                case 2:
                    if (null != fragment3) {
                        fragment3.search(content.getText().toString().trim());
                    }
                    break;
                case 3:
                    if (null != fragment4) {
                        fragment4.search(content.getText().toString().trim());
                    }
                    break;
            }
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
                            fragment1 = MyGetSaleListFragment.getInstance(VIEW_1);
                        return fragment1;
                    case VIEW_2:
                        if (null == fragment2)
                            fragment2 = MyGetSaleListFragment.getInstance(VIEW_2);
                        return fragment2;
                    case VIEW_3:
                        if (null == fragment3)
                            fragment3 = MyGetSaleListFragment.getInstance(VIEW_3);
                        return fragment3;
                    case VIEW_4:
                        if (null == fragment4)
                            fragment4 = MyGetSaleListFragment.getInstance(VIEW_4);
                        return fragment4;
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
                    case VIEW_4:
                        return tabTitle[VIEW_4];
                    default:
                        break;
                }
            }
            return super.getPageTitle(position);
        }
    }
}
