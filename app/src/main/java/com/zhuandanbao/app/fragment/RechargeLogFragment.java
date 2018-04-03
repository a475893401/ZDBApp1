package com.zhuandanbao.app.fragment;

import android.graphics.Color;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.RechargeLogEntity;
import com.zhuandanbao.app.mvp.BaseListViewFragment;
import com.zhuandanbao.app.mvp.model.BaseEntity;
import com.zhuandanbao.app.utils.MJsonUtils;
import com.zhuandanbao.app.utils.PerfHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 充值记录
 * Created by BFTECH on 2017/3/14.
 */

public class RechargeLogFragment extends BaseListViewFragment<ShopD> {
    private RvBaseAdapter adapter;
    private List<RechargeLogEntity> logEntities=new ArrayList<>();

    public static RechargeLogFragment getInstance() {
        RechargeLogFragment fragment = new RechargeLogFragment();
        return fragment;
    }

    @Override
    protected void initD() {
        loadingId = 2;
        getLog();
    }

    @Override
    protected Class<ShopD> getDelegateClass() {
        return ShopD.class;
    }

    @Override
    public void initView() {
        super.initView();
        superRecyclerView = viewDelegate.get(R.id.recycler_view);
        adapter=new RvBaseAdapter(context, logEntities, R.layout.item_trade_log, new RvBaseAdapter.RefreshAdapterImpl() {
            @Override
            public void convertImp(BaseViewHolder holder, Object item, int position) {
                RechargeLogEntity info= (RechargeLogEntity) item;
                holder.setText(R.id.view1,info.getR_sn());
                holder.setText(R.id.view2,info.getAmount());
                holder.setText(R.id.view3,info.getPayname());
                holder.setText(R.id.view4,info.getPay_status_reamrk());
                if (info.getPaystatus().equals("-1")) {
                    holder.setTextColor(R.id.view4, Color.parseColor("#CD0000"));
                }
                if (info.getPaystatus().equals("0")) {
                    holder.setTextColor(R.id.view4, Color.parseColor("#CD8500"));
                }
                if (info.getPaystatus().equals("1")) {
                    holder.setTextColor(R.id.view4, Color.parseColor("#32CD32"));
                }
            }
        });
        initVerticalRecyclerView(adapter,true,false,1,0);
    }

    @Override
    public void initData(int code, BaseEntity baseEntity) {
        super.initData(code, baseEntity);
        if (code==1){
            if (page==1){
                logEntities.clear();
            }
            List<RechargeLogEntity> itemList= JSON.parseArray(MJsonUtils.jsonToTradeLog(baseEntity.data),RechargeLogEntity.class);
            logEntities.addAll(JSON.parseArray(MJsonUtils.jsonToTradeLog(baseEntity.data),RechargeLogEntity.class));
            completeRefresh(itemList.size(),logEntities.size(),false);
        }
    }

    /**
     * 获取充值日志
     */
    private void getLog() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put("type", "recharge");
        params.put(Constants.APP_OPENID, PerfHelper.getStringData(Constants.APP_OPENID));
        params.put("page", page);
        params.put("page_size", page_size);
        baseHttp(null, true, Constants.API_GET_FINANCE_TRADE_LOG, params);
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getLog();
    }

    @Override
    public void onLoadMore() {
        super.onLoadMore();
        page=(logEntities.size()/page_size)+1;
        getLog();
    }
}
