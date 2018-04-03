package com.zhuandanbao.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by BFTECH on 2017/2/12.
 */

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    List<Fragment> data;
    String[] titles;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> data, String[] titles) {
        super(fm);
        this.data = data;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : data.size();
    }

    boolean isEmpty() {
        return data == null || data.size() == 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return this.data.get(position);
    }
}
