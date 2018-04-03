package com.zhuandanbao.app.utils;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zhuandanbao.app.entity.WuLiuEntity;

import java.util.List;

import app.zhuandanbao.com.reslib.R;
import app.zhuandanbao.com.reslib.widget.area.ArrayWheelAdapter;
import app.zhuandanbao.com.reslib.widget.area.OnWheelChangedListener;
import app.zhuandanbao.com.reslib.widget.area.SelectAreaWheelPopWOnClick;
import app.zhuandanbao.com.reslib.widget.area.WheelView;

/**
 * Created by BFTECH on 2017/3/16.
 */

public class PopWuliu {

    private Context context;
    private PopupWindow popW;
    private SelectAreaWheelPopWOnClick onClick;

    private WheelView wheelView;
    private String defatValue;//默认的物流
    private List<WuLiuEntity> wuLiuEntities;
    private String[] wuliuDatas;
    private int isDefat=0;
    private WuLiuEntity wuLiuEntity;

    /**
     * 显示窗口
     *
     * @param v
     * @param onClick
     * @return
     */
    public PopupWindow showPopw(View v, String defatValue, SelectAreaWheelPopWOnClick onClick) {
        MLog.e("defatValue=="+defatValue);
        this.onClick = onClick;
        this.defatValue=defatValue;
        if (popW == null) {
            initView();
        }
        popW.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        return popW;
    }

    public static PopWuliu getInstance(List<WuLiuEntity> wuLiuEntities, Context context) {
        PopWuliu pop = new PopWuliu(wuLiuEntities, context);
        return pop;
    }

    private PopWuliu(List<WuLiuEntity> wuLiuEntities, Context context) {
        this.context = context;
        this.wuLiuEntities=wuLiuEntities;
    }

    private void initView() {
        wuliuDatas=new String[wuLiuEntities.size()];
        for (int i=0;i<wuLiuEntities.size();i++){
            WuLiuEntity info=wuLiuEntities.get(i);
            wuliuDatas[i]=info.getName();
            if (info.getName().equals(defatValue)){
                isDefat=i;
            }
        }
        wuLiuEntity=wuLiuEntities.get(isDefat);
        LayoutInflater lf = LayoutInflater.from(context);
        View v = lf.inflate(R.layout.pop_select_wuliu, null);
        popW = new PopupWindow(v, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        popW.setFocusable(true);
        popW.setOutsideTouchable(true);
        popW.setBackgroundDrawable(new BitmapDrawable());
        wheelView = (WheelView) v.findViewById(R.id.wheel2);
        wheelView.addChangingListener(new PopWuliu.wheelListener());
        TextView yes = (TextView) v.findViewById(R.id.yes);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popW.dismiss();
                onClick.sureOnClick(Integer.parseInt(wuLiuEntity.getId()),0,0,wuLiuEntity.getName(),null,null);
            }
        });
        wheelView.setViewAdapter(new ArrayWheelAdapter<>(context, wuliuDatas));
        // 设置可见条目数量
        wheelView.setVisibleItems(7);
        wheelView.setCurrentItem(isDefat);
    }

    class wheelListener implements OnWheelChangedListener {
        @Override
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (wheel==wheelView){
                wuLiuEntity=wuLiuEntities.get(newValue);
            }
        }
    }
}
