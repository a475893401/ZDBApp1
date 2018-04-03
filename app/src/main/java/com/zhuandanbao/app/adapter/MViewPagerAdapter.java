package com.zhuandanbao.app.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();

    public MViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> arrayList) {
        super(fragmentManager);
        this.fragmentList = arrayList;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragmentList.get(arg0);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

}
