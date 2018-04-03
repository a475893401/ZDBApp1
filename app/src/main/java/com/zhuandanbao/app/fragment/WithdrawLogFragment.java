package com.zhuandanbao.app.fragment;

import android.graphics.Color;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.model.HttpParams;
import com.superlibrary.adapter.BaseViewHolder;
import com.zhuandanbao.app.R;
import com.zhuandanbao.app.adapter.RvBaseAdapter;
import com.zhuandanbao.app.appdelegate.ShopD;
import com.zhuandanbao.app.constant.Constants;
import com.zhuandanbao.app.entity.WithdrawLogEntity;
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

public class WithdrawLogFragment extends BaseListViewFragment<ShopD> {
    private RvBaseAdapter adapter;
    private List<WithdrawLogEntity> logEntities=new ArrayList<>();

    public static WithdrawLogFragment getInstance() {
        WithdrawLogFragment fragment = new WithdrawLogFragment();
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
                WithdrawLogEntity info= (WithdrawLogEntity) item;
                holder.setText(R.id.view1,"编码："+info.getApply_sn());
                holder.setText(R.id.view2,info.getAmount());
                holder.setText(R.id.view3,info.getBank_name()+"("+info.getBank_card().substring(info.getBank_card().length() - 4, info.getBank_card().length())+")"
                        +"\n"+info.getApply_time_format());
                holder.setText(R.id.view4,info.getStatus_reamrk());
                if (info.getStatus().equals("-1")) {
                    holder.setTextColor(R.id.view4, Color.parseColor("#CD0000"));
                }
                if (info.getStatus().equals("0")||info.getStatus().equals("1")) {
                    holder.setTextColor(R.id.view4, Color.parseColor("#CD8500"));
                }
                if (info.getStatus().equals("2")) {
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
            List<WithdrawLogEntity> itemList= JSON.parseArray(MJsonUtils.jsonToTradeLog(baseEntity.data),WithdrawLogEntity.class);
            logEntities.addAll(JSON.parseArray(MJsonUtils.jsonToTradeLog(baseEntity.data),WithdrawLogEntity.class));
            completeRefresh(itemList.size(),logEntities.size(),false);
        }
    }

    private void getLog() {
        requestId = 1;
        HttpParams params = new HttpParams();
        params.put("type", "withdraw");
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
