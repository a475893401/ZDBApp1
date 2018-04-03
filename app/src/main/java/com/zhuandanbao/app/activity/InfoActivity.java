package com.zhuandanbao.app.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.lzy.okgo.model.HttpParams;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.TabPagerAdapter;
import com.zhuandanbao.app.appdelegate.InfoD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.NewsEntity;
import com.zhuandanbao.app.fragment.NewsListFragment;
import com.zhuandanbao.app.mvp.BaseHttpActivity;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.MLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import app.zhuandanbao.com.reslib.widget.MyViewPager;
import app.zhuandanbao.com.reslib.widget.TabPagerIndicator;

/**
 * 资讯
 * Created by BFTECH on 2017/2/28.
 */

public class InfoActivity extends BaseHttpActivity<InfoD> {

    private TabPagerIndicator tabPagerIndicator;
    private MyViewPager myViewPager;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private String[] mTitles;
    private TabPagerAdapter adapter;
    private List<NewsEntity> list=new ArrayList<>();

    @Override
    protected Class<InfoD> getDelegateClass() {
        return InfoD.class;
    }

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);
        viewDelegate.setBack("返回", R.mipmap.img_back_icon, null);
        viewDelegate.setTitle("资讯");
        tabPagerIndicator = viewDelegate.get(R.id.info_tabPagerIndicator);
        myViewPager = viewDelegate.get(R.id.info_viewPager);
        getInfoTitle();
    }

    /**
     * 獲取資訊信息
     */
    private void getInfoTitle(){
        requestId=1;
        loadingId=2;
        HttpParams params=new HttpParams();
        baseHttp(null,true, Constants.API_ACTION_TYPE_INFOMATION,params);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            HashMap<String, Object> valueObje = MJsonUtils.perJson(baseEntity.data);
            Map<String, Object> treeMap = new TreeMap<>(valueObje);
            mTitles=new String[valueObje.size()];
            int index=0;
            for (Map.Entry entry : treeMap.entrySet()) {
                MLog.e("key=="+entry.getKey().toString());
                mTitles[index]=entry.getValue().toString();
                NewsEntity info=new NewsEntity();
                info.newsTypeId=entry.getKey().toString();
                info.newsTitleContent=entry.getValue().toString();
                list.add(info);
                mFragments.add(NewsListFragment.getInstance(Integer.parseInt(entry.getKey().toString())));
                index++;
            }
            adapter = new TabPagerAdapter(getSupportFragmentManager(), mFragments, mTitles);
            myViewPager.setAdapter(adapter);
            tabPagerIndicator.setViewPager(myViewPager);
            tabPagerIndicator.setIndicatorMode(TabPagerIndicator.IndicatorMode.MODE_WEIGHT_EXPAND_NOSAME, true);
            int color = Color.parseColor("#2383cd");
            int textColor = Color.parseColor("#666666");
            tabPagerIndicator.setIndicatorColor(color);
            tabPagerIndicator.setTextColor(textColor);
            tabPagerIndicator.setTextSize(38);
            tabPagerIndicator.setTypeface(null, Typeface.NORMAL);
        }
    }


}
