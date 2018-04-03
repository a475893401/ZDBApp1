package com.zhuandanbao.app.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.ViewPagerAdapter;
import com.zhuandanbao.app.appdelegate.GuideD;
import com.zhuandanbao.app.fragment.Fragment1;
import com.zhuandanbao.app.fragment.Fragment2;
import com.zhuandanbao.app.fragment.Fragment3;
import com.zhuandanbao.app.mvp.BaseHttpActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 引导页
 * Created by BFTECH on 2016/4/20.
 */
public class GuideActivity extends BaseHttpActivity<GuideD> {

    private ViewPager viewPager;
    private RadioGroup radioGroup;
    private ViewPagerAdapter mPgAdapter;
    private List<Fragment> mListFragment;

    @Override
    protected Class<GuideD> getDelegateClass() {
        return GuideD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        init();
    }

    private void init() {
        mListFragment = new ArrayList<>();
        mListFragment.add(new Fragment1());
        mListFragment.add(new Fragment2());
        mListFragment.add(new Fragment3());
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.advertise_point_group);
        mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(), mListFragment);
        viewPager.setAdapter(mPgAdapter);
        viewPager.addOnPageChangeListener(new MyPagerChangeListener());
    }

    class MyPagerChangeListener implements ViewPager.OnPageChangeListener {

        public void onPageSelected(int position) {

        }

        public void onPageScrollStateChanged(int arg0) {
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            ((RadioButton) radioGroup.getChildAt(position)).setChecked(true);
        }
    }
}
