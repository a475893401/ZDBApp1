package com.zhuandanbao.app.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.classic.common.MultipleStatusView;
import com.lhh.apst.library.CustomPagerSlidingTabStrip;
import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.MainApplication;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.appdelegate.HelpD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.HelpTitleEntity;
import com.zhuandanbao.app.entity.SkipInfoEntity;
import com.zhuandanbao.app.entity.SystemSettingEntity;
import com.zhuandanbao.app.fragment.HelperFragment;
import com.zhuandanbao.app.mvp.BaseListViewActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.CallPhone;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MUtils;

import java.util.ArrayList;
import java.util.List;

import app.zhuandanbao.com.reslib.widget.MyViewPager;

/**
 * 帮助中心
 * Created by BFTECH on 2017/4/10.
 */

public class HelperActivity extends BaseListViewActivity<HelpD> implements ViewPager.OnPageChangeListener {

    private MyViewPager viewPager;
    private CustomPagerSlidingTabStrip tab;
    private String[] tabTitle = null;
    private static final int VIEW_1 = 0;
    private static final int VIEW_2 = 1;
    private static final int VIEW_3 = 2;
    private static final int VIEW_4 = 3;

    private static int VIEW_SIZE = 4;
    private List<HelpTitleEntity> list=new ArrayList<>();

    private HelperFragment fragment1 = null, fragment2 = null, fragment3 = null, fragment4 = null;
    private SystemSettingEntity settingEntity=null;
    @Override
    protected Class<HelpD> getDelegateClass() {
        return HelpD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        if (null!= MainApplication.cache.getAsObject(Constants.SYSTEM_SETTING)){
            settingEntity= (SystemSettingEntity) MainApplication.cache.getAsObject(Constants.SYSTEM_SETTING);
        }
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("帮助中心");
        viewPager = viewDelegate.get(R.id.help_viewpager);
        tab = viewDelegate.get(R.id.help_tab);
        viewDelegate.setOnClickListener(this, R.id.help_mobile, R.id.help_online, R.id.help_workorder);
        getHelpList();
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
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code == 1) {
            list.clear();
            list.addAll(MJsonUtils.personHelpTitleEntity(baseEntity.data));
            if (list.size()>0) {
                tabTitle = new String[list.size()];
                for (int i=0;i<list.size();i++) {
                    tabTitle[i]=list.get(i).getName();
                }
                viewPager.setNoScroll(true);
                MainFragmentAdapter adapter = new MainFragmentAdapter(getSupportFragmentManager());
                viewPager.setAdapter(adapter);
                tab.setViewPager(viewPager);
                tab.setOnPageChangeListener(this);
            }else {
                viewDelegate.showMultipleStatusView(multipleStatusView, MultipleStatusView.STATUS_EMPTY,"");
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view.getId() == R.id.help_mobile) {
            if (null!=settingEntity){
                CallPhone.callPhone(context, settingEntity.getZD_CUSTOMER_SERVICE_HOTLINE());
            }else {
                CallPhone.callPhone(context, "023-61216555");
            }
        }
        if (view.getId() == R.id.help_online) {
            if (MUtils.checkApkExist(this, "com.tencent.mobileqq")){
                if (null!=settingEntity) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+settingEntity.getZD_CUSTOMER_SERVICE_QQ()+"&version=1")));
                }else {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin="+"2480309486"+"&version=1")));
                }
            }else{
                viewDelegate.toast("请先下载QQ应用");
            }
        }
        if (view.getId() == R.id.help_workorder) {
            SkipInfoEntity skipInfoEntity = new SkipInfoEntity();
            skipInfoEntity.index = 0;
            skipInfoEntity.item = 0;
            viewDelegate.goToActivity(skipInfoEntity, AddWorkOrderActivity.class, 0);
        }
    }

    class MainFragmentAdapter extends FragmentStatePagerAdapter implements CustomPagerSlidingTabStrip.CustomTabProvider {

        LayoutInflater mInflater;

        public MainFragmentAdapter(FragmentManager fm) {
            super(fm);
            mInflater = LayoutInflater.from(HelperActivity.this);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_SIZE) {
                switch (position) {
                    case VIEW_1:
                        if (null == fragment1)
                            fragment1 = HelperFragment.getInstance(list.get(VIEW_1).getAcid());
                        return fragment1;
                    case VIEW_2:
                        if (null == fragment2)
                            fragment2 = HelperFragment.getInstance(list.get(VIEW_2).getAcid());
                        return fragment2;
                    case VIEW_3:
                        if (null == fragment3)
                            fragment3 = HelperFragment.getInstance(list.get(VIEW_3).getAcid());
                        return fragment3;
                    case VIEW_4:
                        if (null == fragment4)
                            fragment4 = HelperFragment.getInstance(list.get(VIEW_4).getAcid());
                        return fragment4;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_SIZE;
        }

        /**
         * 选中
         *
         * @param position
         * @param convertView
         * @return
         */
        @Override
        public View getSelectTabView(int position, View convertView) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_help_select_tab_view, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.select_radio_text);
            int color = Color.parseColor("#666666");
            textView.setTextColor(color);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.select_radio_img);
            textView.setText(tabTitle[position]);
            if (tabTitle[position].equals("常见问题")){
                imageView.setImageResource(R.mipmap.img_help_problem_y);
            }else if (tabTitle[position].equals("接单指南")){
                imageView.setImageResource(R.mipmap.img_help_taking_y);
            }else if (tabTitle[position].equals("发单指南")){
                imageView.setImageResource(R.mipmap.img_help_bill_y);
            }else if (tabTitle[position].equals("店铺相关")){
                imageView.setImageResource(R.mipmap.img_help_shop_y);
            }
            return convertView;
        }

        /**
         * 未选中
         *
         * @param position
         * @param convertView
         * @return
         */
        @Override
        public View getDisSelectTabView(int position, View convertView) {
            if (null == convertView) {
                convertView = mInflater.inflate(R.layout.item_help_dis_select_tab_view, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.unselect_radio_text);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.unselect_radio_img);
            textView.setText(tabTitle[position]);
            if (tabTitle[position].equals("常见问题")){
                imageView.setImageResource(R.mipmap.img_help_problem_b);
            }else if (tabTitle[position].equals("接单指南")){
                imageView.setImageResource(R.mipmap.img_help_taking_b);
            }else if (tabTitle[position].equals("发单指南")){
                imageView.setImageResource(R.mipmap.img_help_bill_b);
            }else if (tabTitle[position].equals("店铺相关")){
                imageView.setImageResource(R.mipmap.img_help_shop_b);
            }
            return convertView;
        }
    }

    private void getHelpList() {
        requestId = 1;
        loadingId = 2;
        HttpParams params = new HttpParams();
        params.put("page", page);
        params.put("page_size", page_size);
        baseHttp(null, true, Constants.API_HELP_LIST, params);
    }
}
